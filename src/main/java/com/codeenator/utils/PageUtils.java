package com.codeenator.utils;

import com.ithows.ResultMap;

/**
 * 페이징 Utils
 */
public class PageUtils {
    private static final int DEFAULT_CONTENT_COUNT = 15;        // 페이지 당 출력할 목록 개수
    
    /**
     * 페이징 정보 가져오기
     * @param count             전체 개수
     * @param currentPage       현재 페이지
     * @return                  페이징 정보
     */
    public static ResultMap getPagination(int count, int currentPage) {
        return getPagination(count, currentPage, DEFAULT_CONTENT_COUNT);
    }
    
    
    /**
     * 페이징 정보 가져오기
     * @param count             전체 개수
     * @param currentPage       현재 페이지
     * @param contentCount      페이지 당 출력할 목록 개수
     * @return                  페이징 정보
     */
    public static ResultMap getPagination(int count, int currentPage, int contentCount) {
        int totalPage = count / contentCount;                           // 전체 페이지        
        
        if (count % contentCount > 0) {
            totalPage++;
        }
        
        int endPage = (int) (Math.ceil(currentPage / 10.0)) * 10;       // 마지막 페이지
        int startPage = endPage - 9;                                    // 시작 페이지
        
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        
        boolean prev = startPage > 1;                                   // 페이지 이동 버튼 활성화 여부
        boolean next = endPage < totalPage;
        
        int startCount = (currentPage - 1) * contentCount;              // 페이지 첫 게시글
        
        ResultMap resultMap = new ResultMap();
        resultMap.put("count", count);
        resultMap.put("startCount", startCount);
        resultMap.put("totalPage", totalPage);
        resultMap.put("startPage", startPage);
        resultMap.put("currentPage", currentPage);
        resultMap.put("endPage", endPage);
        resultMap.put("prev", prev);
        resultMap.put("next", next);
        
        return resultMap;
    }
}
