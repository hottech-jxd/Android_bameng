package com.bameng.model;

/**
 * Created by Administrator on 2016/12/30.
 */

public class CustomerResourceModel {
    private int UserId;

    private String Name ;

    private String Mobile;

    private String Addr;

    private String Remark;

    private String DataImg;

    private String SubmitName ;

    /// <summary>
    /// 1 图片资料
    /// </summary>
    /// <value>The type.</value>
    private int Type;

    //public long CreateTime;


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDataImg() {
        return DataImg;
    }

    public void setDataImg(String dataImg) {
        DataImg = dataImg;
    }

    public String getSubmitName() {
        return SubmitName;
    }

    public void setSubmitName(String submitName) {
        SubmitName = submitName;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
