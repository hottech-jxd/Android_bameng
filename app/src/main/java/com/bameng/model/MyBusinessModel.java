package com.bameng.model;

/**
 * Created by Administrator on 2016/11/18.
 */

public class MyBusinessModel {
    /***
     * 订单数量
     */
    private int orderAmount;
    /***
     * 兑换数量
     */
    private int exchangeAmount;
    /***
     * 客户数量
     */
    private  int customerAmount;
    /***
     * 现金卷数量
     */
    private int cashCouponAmount;

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(int exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    public int getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(int customerAmount) {
        this.customerAmount = customerAmount;
    }

    public int getCashCouponAmount() {
        return cashCouponAmount;
    }

    public void setCashCouponAmount(int cashCouponAmount) {
        this.cashCouponAmount = cashCouponAmount;
    }
}
