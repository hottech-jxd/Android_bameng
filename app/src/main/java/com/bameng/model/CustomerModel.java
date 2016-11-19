package com.bameng.model;

import java.io.Serializable;

/**
 * Created by 47483 on 2016.11.08.
 */
public class CustomerModel implements Serializable {

    String Addr;
    String 	BelongOne;
    String BelongOneName;
    String BelongTwo;
    String BelongTwoName;
    String CreateTime;
    int ID;
    int InShop;
    int IsDel;
    String Mobile;
    String Name;
    String Remark;
    int ShopId;
    String ShopName;
    int  Status;
    boolean doing=false;
    boolean selected = false;

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getBelongOne() {
        return BelongOne;
    }

    public void setBelongOne(String belongOne) {
        BelongOne = belongOne;
    }

    public String getBelongOneName() {
        return BelongOneName;
    }

    public void setBelongOneName(String belongOneName) {
        BelongOneName = belongOneName;
    }

    public String getBelongTwo() {
        return BelongTwo;
    }

    public void setBelongTwo(String belongTwo) {
        BelongTwo = belongTwo;
    }

    public String getBelongTwoName() {
        return BelongTwoName;
    }

    public void setBelongTwoName(String belongTwoName) {
        BelongTwoName = belongTwoName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getInShop() {
        return InShop;
    }

    public void setInShop(int inShop) {
        InShop = inShop;
    }

    public int getIsDel() {
        return IsDel;
    }

    public void setIsDel(int isDel) {
        IsDel = isDel;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getShopId() {
        return ShopId;
    }

    public void setShopId(int shopId) {
        ShopId = shopId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isDoing() {
        return doing;
    }

    public void setDoing(boolean doing) {
        this.doing = doing;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
