package com.example.demo.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.demo.mapper.PharmacyMapper;
import com.example.demo.model.PharmacyInfo;
import com.example.demo.util.KakaoGeoUtil;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyMapper mapper;

    @Autowired
    private KakaoGeoUtil kakaoGeo;

    // ✅ API 키를 외부 설정에서 불러오기
    @Value("${publicdata.api.key}")
    private String apiKey;

    public void fetchAndSaveAllPharmacies() throws Exception {
        int page = 1;
        int totalCount = 0;
        int numOfRows = 1000;

        do {
            String urlStr = "https://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList"
                    + "?serviceKey=" + apiKey
                    + "&pageNo=" + page
                    + "&numOfRows=" + numOfRows;

            List<PharmacyInfo> list = parsePharmaciesFromXml(urlStr);

            for (PharmacyInfo p : list) {
                if (p.getPharmId() == null || p.getPharmId().isEmpty()) {
                    System.out.println("⚠️ pharmId가 null이거나 비어 있음 → 저장 생략");
                    continue;
                }

                PharmacyInfo existing = mapper.findById(p.getPharmId());
                if (existing == null) {
                    double[] latLng = kakaoGeo.getLatLngFromAddress(p.getPharmAddress());
                    p.setPharmLat(latLng[0]);
                    p.setPharmLng(latLng[1]);
                    mapper.insert(p);
                    System.out.println("✅ 저장 완료: " + p.getPharmName() + " (" + p.getPharmId() + ")");
                } else {
                    System.out.println("✅ 중복 생략: " + p.getPharmName() + " (" + p.getPharmId() + ")");
                }
            }

            if (page == 1) {
                totalCount = getTotalCount(urlStr);
            }
            page++;
        } while ((page - 1) * numOfRows < totalCount);
    }

    private int getTotalCount(String urlStr) throws Exception {
        Document doc = getXmlDoc(urlStr);
        return Integer.parseInt(doc.getElementsByTagName("totalCount").item(0).getTextContent());
    }

    private List<PharmacyInfo> parsePharmaciesFromXml(String urlStr) throws Exception {
        Document doc = getXmlDoc(urlStr);
        NodeList nodes = doc.getElementsByTagName("item");
        List<PharmacyInfo> result = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            PharmacyInfo p = new PharmacyInfo();
            p.setPharmId(getTagValue("ykiho", e));
            p.setPharmName(getTagValue("yadmNm", e));
            p.setPharmAddress(getTagValue("addr", e));
            p.setPharmTel(getTagValue("telno", e));
            result.add(p);
        }
        return result;
    }

    private Document getXmlDoc(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        return DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(url.openStream());
    }

    private String getTagValue(String tag, Element e) {
        NodeList nl = e.getElementsByTagName(tag);
        if (nl.getLength() == 0) return null;
        Node n = nl.item(0).getFirstChild();
        return (n != null) ? n.getNodeValue() : null;
    }

    public List<Map<String, Object>> getPharmacies(String name, String area) {
        return mapper.searchPharmacies(name, area);
    }
}