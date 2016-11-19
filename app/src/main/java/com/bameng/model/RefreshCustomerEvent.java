package com.bameng.model;

/**
 * Created by Administrator on 2016/11/15.
 */

public class RefreshCustomerEvent {
    private CustomerModel customerModel;
    private String tabName;
    public RefreshCustomerEvent( CustomerModel customerModel , String tabName){
        this.customerModel = customerModel;
        this.tabName = tabName;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
