package com.example.demo.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.demo.mapper.HospitalMapper;
import com.example.demo.model.HospitalInfo;
import com.example.demo.util.KakaoGeoUtil;

@Service
public class HospitalService {

    @Autowired
    private HospitalMapper mapper;

    @Autowired
    private KakaoGeoUtil kakaoGeo;

    // ✅ application.properties에서 API 키 주입
    @Value("${publicdata.api.key}")
    private String apiKey;

    public void fetchAndSaveAllHospitals() throws Exception {
        int page = 1;
        int totalCount = 0;
        int numOfRows = 1000;

        do {
            String urlStr = "https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList"
                    + "?serviceKey=" + apiKey
                    + "&pageNo=" + page
                    + "&numOfRows=" + numOfRows;

            List<HospitalInfo> list = parseHospitalsFromXml(urlStr);

            for (HospitalInfo h : list) {
                h.setHospId(h.getHospId().trim());

                if (h.getHospDepartment() == null || h.getHospDepartment().isBlank()) {
                    h.setHospDepartment("없음");
                }

                HospitalInfo existing = mapper.findById(h.getHospId());
                System.out.println(">> 삽입 시도 ID: " + h.getHospId());
                System.out.println(">> DB에 존재 여부: " + (existing != null));

                if (existing == null) {
                    if (h.getHospAddress() != null && !h.getHospAddress().isEmpty()) {
                        double[] latLng = kakaoGeo.getLatLngFromAddress(h.getHospAddress());
                        h.setHospLat(latLng[0]);
                        h.setHospLng(latLng[1]);
                    }

                    if (h.getHospLat() != 0.0 && h.getHospLng() != 0.0) {
                        System.out.println(">> 삽입 전: " + h.toString());
                        mapper.insert(h);
                    }
                } else {
                    System.out.println(">> 이미 존재하는 병원 ID: " + h.getHospId());
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

    private List<HospitalInfo> parseHospitalsFromXml(String urlStr) throws Exception {
        Document doc = getXmlDoc(urlStr);
        NodeList nodes = doc.getElementsByTagName("item");
        List<HospitalInfo> result = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            HospitalInfo h = new HospitalInfo();
            h.setHospId(getTagValue("ykiho", e));
            h.setHospName(getTagValue("yadmNm", e));
            h.setHospAddress(getTagValue("addr", e));
            h.setHospTel(getTagValue("telno", e));
            h.setHospType(getTagValue("clCdNm", e));
            h.setHospDepartment(getTagValue("dgsbjtCdNm", e));
            result.add(h);
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

    public List<Map<String, Object>> getHospitals(String name, String area, String dept, String type) {
        return mapper.searchHospitals(name, area, dept, type);
    }
    
    public List<Map<String, Object>> findHospitalsByDistance(double lat, double lng, int radius,
            String name, String area, String type) {
    	return mapper.findNearbyHospitals(lat, lng, radius, name, area, type);
}
}