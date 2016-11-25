package com.bameng.model;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ConvertFlowOutputModel extends BaseModel {
    private ConvertFlowList data;

    public ConvertFlowList getData() {
        return data;
    }

    public void setData( ConvertFlowList data) {
        this.data = data;
    }


    public class ConvertFlowList{
        private List<ConvertFlowModel> list;

        public List<ConvertFlowModel> getList() {
            return list;
        }

        public void setList(List<ConvertFlowModel> list) {
            this.list = list;
        }
    }
}
