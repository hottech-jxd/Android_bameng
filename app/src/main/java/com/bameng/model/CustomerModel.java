package com.bameng.model;

import java.io.Serializable;

/**
 * Created by 47483 on 2016.11.08.
 */
public class CustomerModel implements Serializable {
    /***
     * 客户地址
     */
    String Addr;
    /***
     * 所属盟友
     */
    String 	BelongOne;
    /***
     * 盟友姓名
     */
    String BelongOneName;
    /***
     * 所属盟主
     */
    String BelongTwo;
    /***
     * 盟主姓名
     */
    String BelongTwoName;
    /***
     * 创建时间
     */
    String CreateTime;
    int ID;
    /***
     * 1进店 0未进店
     */
    int InShop;
    /***
     * 是否删除
     */
    int IsDel;
    /***
     * 客户手机
     */
    String Mobile;
    /***
     * 客户姓名
     */
    String Name;
    /***
     * 备注
     */
    String Remark;
    /***
     * 所属门店ID
     */
    int ShopId;
    /***
     * 门店名称
     */
    String ShopName;
    /***
     * 0 审核中，1已同意 2已拒绝
     */
    int  Status;
    boolean doing=false;
    boolean selected = false;
    /***
     * 1:客户图片，0：客户信息
     */
    int isSave=0;
    /***
     * 客户图片
     */
    String DataImg;

//    /***
//     * 是否在界面显示状态信息
//     */
//    boolean showStatus=true;
//    /***
//     * 是否在界面显示进店情况
//     */
//    boolean showInShop=true;
//    /***
//     * 是否在界面显示所属盟友信息
//     */
//    boolean showMengYou=true;

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

    public int getIsSave() {
        return isSave;
    }

    public void setIsSave(int isSave) {
        this.isSave = isSave;
    }

    public String getDataImg() {
        return DataImg;
    }

    public void setDataImg(String dataImg) {
        DataImg = dataImg;
    }

//    public boolean isShowStatus() {
//        return showStatus;
//    }
//
//    public void setShowStatus(boolean showStatus) {
//        this.showStatus = showStatus;
//    }
//
//    public boolean isShowInShop() {
//        return showInShop;
//    }
//
//    public void setShowInShop(boolean showInShop) {
//        this.showInShop = showInShop;
//    }
//
//    public boolean isShowMengYou() {
//        return showMengYou;
//    }
//
//    public void setShowMengYou(boolean showMengYou) {
//        this.showMengYou = showMengYou;
//    }
}
