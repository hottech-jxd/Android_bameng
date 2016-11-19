package com.bameng.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MyPageModel  {
    int PageCount;
    int PageIndex;
    int PageSize;
    List<UserData> Rows;
    int Total;

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public List<UserData> getRows() {
        return Rows;
    }

    public void setRows(List<UserData> rows) {
        Rows = rows;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
