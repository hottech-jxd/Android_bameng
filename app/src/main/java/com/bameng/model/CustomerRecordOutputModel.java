package com.bameng.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class CustomerRecordOutputModel extends BaseModel {
    private CustomerRecordList data;

    public CustomerRecordList getData() {
        return data;
    }

    public void setData(CustomerRecordList data) {
        this.data = data;
    }

    public class CustomerRecordList {
        private List<CustomerRecord> Rows;
        private int PageCount;
        private  int PageIndex;
        private int PageSize;

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

        public List<CustomerRecord> getRows() {
            return Rows;
        }

        public void setRows(List<CustomerRecord> rows) {
            Rows = rows;
        }
    }
}
