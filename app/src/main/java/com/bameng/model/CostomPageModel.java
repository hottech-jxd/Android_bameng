package com.bameng.model;

import java.util.List;

/**
 * Created by 47483 on 2016.11.08.
 */

public class CostomPageModel {

    int PageCount;
    int PageIndex;

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

    public List<CustomerModel> getRows() {
        return Rows;
    }

    public void setRows(List<CustomerModel> rows) {
        Rows = rows;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    int PageSize;
    List<CustomerModel> Rows;
    int Total;
}
