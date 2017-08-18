package com.bameng.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/1.
 */
public class CustomerRecord {
    private Long CID;
    private String AssertContent;
    private String CreateTime;
    private Long ID;
    private Long UserId;


    public Long getCID() {
        return CID;
    }

    public void setCID(Long CID) {
        this.CID = CID;
    }

    public String getAssertContent() {
        return AssertContent;
    }

    public void setAssertContent(String assertContent) {
        AssertContent = assertContent;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }
}
