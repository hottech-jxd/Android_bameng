package com.bameng.model;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/11/22.
 */

public class BeanFlowOutputModel extends BaseModel {
    private BeanFlowList data;

    public BeanFlowList getData() {
        return data;
    }

    public void setData( BeanFlowList data) {
        this.data = data;
    }

    public class BeanFlowList{
        private List<BeanRecordModel> list;
        private int income;
        private int outcome;

        public List<BeanRecordModel> getList() {
            return list;
        }

        public void setList(List<BeanRecordModel> list) {
            this.list = list;
        }

        public int getIncome() {
            return income;
        }

        public void setIncome(int income) {
            this.income = income;
        }

        public int getOutcome() {
            return outcome;
        }

        public void setOutcome(int outcome) {
            this.outcome = outcome;
        }
    }
}
