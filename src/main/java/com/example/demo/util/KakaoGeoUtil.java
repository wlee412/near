package com.example.demo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KakaoGeoUtil {

    @Value("${kakao.rest.api.key}")
    private String restApiKey;

    public double[] getLatLngFromAddress(String address) {
        try {
            String query = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/local/search/address.json?query=" + query;

            HttpURLConnection conn = (HttpURLConnection) new URL(apiURL).openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + restApiKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.lines().collect(Collectors.joining());
            JSONObject json = new JSONObject(response);
            JSONArray documents = json.getJSONArray("documents");

            if (documents.length() > 0) {
                JSONObject loc = documents.getJSONObject(0);
                return new double[]{
                    Double.parseDouble(loc.getString("y")), // lat
                    Double.parseDouble(loc.getString("x"))  // lng
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[]{0.0, 0.0};
    }
}
