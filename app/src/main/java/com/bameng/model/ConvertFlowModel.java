package com.bameng.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ConvertFlowModel  extends BeanRecordModel{
    //private int ID;
    //private BigDecimal money;
    private String name;

    //private int status;
    //private long time;

    private String headimg;

    private boolean isDoing=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDoing() {
        return isDoing;
    }

    public void setDoing(boolean doing) {
        isDoing = doing;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
