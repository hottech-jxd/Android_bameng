package com.bameng.model;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MyOutputModel<T> extends BaseModel{
    private  MyPageModel<T> data;

    public MyPageModel getData() {
        return data;
    }

    public void setData(MyPageModel data) {
        this.data = data;
    }
}
