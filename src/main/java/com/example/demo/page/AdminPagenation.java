package com.example.demo.page;

import lombok.Data;

@Data
public class AdminPagenation {

	private int totalCount;   // 전체 글 수
    private int currentPage;  // 현재 페이지
    private int pageSize;     // 한 페이지당 글 수
    private int blockSize;    // 한 블럭에 표시할 페이지 수

    private int startRow;
    private int endRow;
    private int pageCount;
    private int startPage;
    private int endPage;

    public AdminPagenation(int totalCount, int currentPage, int pageSize, int blockSize) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.blockSize = blockSize;

        this.pageCount = (int) Math.ceil((double) totalCount / pageSize);
        this.startRow = (currentPage - 1) * pageSize + 1;
        this.endRow = startRow + pageSize - 1;
        if (endRow > totalCount) endRow = totalCount;

        this.startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        this.endPage = startPage + blockSize - 1;
        if (endPage > pageCount) endPage = pageCount;
    }
}
