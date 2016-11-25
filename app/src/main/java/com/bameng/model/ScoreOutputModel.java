package com.bameng.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ScoreOutputModel extends BaseModel {
    private ScoreList data;

    public ScoreList getData() {
        return data;
    }

    public void setData(ScoreList data) {
        this.data = data;
    }

    public class ScoreList{
        private List<ScoreModel> list;

        public List<ScoreModel> getList() {
            return list;
        }

        public void setList(List<ScoreModel> list) {
            this.list = list;
        }
    }
}
