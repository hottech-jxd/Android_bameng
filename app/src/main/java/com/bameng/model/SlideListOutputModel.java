package com.bameng.model;

import java.util.List;

/**
 * Created by 47483 on 2016.11.07.
 */

public class SlideListOutputModel extends BaseModel {
    public List<SlideListModel> getData() {
        return data;
    }

    public void setData(List<SlideListModel> data) {
        this.data = data;
    }

    private List<SlideListModel> data;

}