package com.bameng.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class BeanFlowModel {
    /***
     * 收入
     */
    private BigDecimal income;
    /***
     * 支出
     */
    private BigDecimal outcome;

    private List<BeanRecordModel> list;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }

    public List<BeanRecordModel> getList() {
        return list;
    }

    public void setList(List<BeanRecordModel> list) {
        this.list = list;
    }
}
