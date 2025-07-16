package com.example.demo.page;

import lombok.Data;

@Data
public class AdminPagenation {

	private int totalCount;   // 전체 글 수
	private int currentPage;  // 현재 페이지
	private int pageSize;     // 한 페이지당 글 수
	private int blockSize;    // 한 블럭에 표시할 페이지 수

	private int startRow;     // LIMIT offset
	private int endRow;       // LIMIT 개수 (pageSize와 동일)
	private int pageCount;
	private int startPage;
	private int endPage;

	public AdminPagenation(int totalCount, int currentPage, int pageSize, int blockSize) {
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.blockSize = blockSize;

		this.pageCount = (int) Math.ceil((double) totalCount / pageSize);

		// ✅ MySQL에 맞는 LIMIT 처리
		this.startRow = (currentPage - 1) * pageSize;
		this.endRow = pageSize;

		this.startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
		this.endPage = startPage + blockSize - 1;
		if (endPage > pageCount) endPage = pageCount;
	}
}
