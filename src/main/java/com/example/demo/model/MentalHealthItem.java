package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MentalHealthItem {

	private int id;
	
	@JsonProperty("chtTtlNm")
    private String chtTtlNm;

    @JsonProperty("chtSeNm")
    private String chtSeNm;

    @JsonProperty("chtXCn")
    private String chtXCn;

    @JsonProperty("chtYCn")
    private String chtYCn;

    @JsonProperty("xIdx")
    private String xIdx;

    @JsonProperty("yIdx")
    private String yIdx;

    @JsonProperty("chtVl")
    private String chtVl;
}

//{"chtTtlNm":"대상별 호소문제 현황 호소문제유형 X 학교/학년별","chtSeNm":"정신건강","chtXCn":"user","chtYCn":"2010","xIdx":1,"yIdx":1,"chtVl":"57846"}