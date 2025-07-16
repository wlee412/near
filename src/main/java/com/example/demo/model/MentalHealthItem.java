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

	// 생성자 추가 (연령대/값만 전달할 때 사용)
	public MentalHealthItem(String chtXCn, String chtVl) {
		this.chtXCn = chtXCn;
		this.chtVl = chtVl;
	}

	// 기본 생성자 필요 (Lombok이 자동 생성하긴 하지만 명시적으로 있어도 무방)
	public MentalHealthItem() {
	}
}
