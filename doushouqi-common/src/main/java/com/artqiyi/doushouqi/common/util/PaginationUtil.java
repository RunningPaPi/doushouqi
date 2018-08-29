package com.artqiyi.doushouqi.common.util;

/** 
 * 分页有关的计算工具
 */
public class PaginationUtil {


    public static Integer getStartNum(Integer pageNum, Integer pageSize, Integer defaultPageSize){
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? defaultPageSize : pageSize;

        Integer startNum = (pageNum - 1) * pageSize;

        return  startNum;
    }
	

}
