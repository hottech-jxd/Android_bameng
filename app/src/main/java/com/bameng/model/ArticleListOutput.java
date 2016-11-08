package com.bameng.model;

import android.widget.ListView;

import java.util.List;

/**
 * Created by 47483 on 2016.11.07.
 */

public class ArticleListOutput extends BaseModel {

    private  ArticleListModel  data;

    public ArticleListModel getData() {
        return data;
    }

    public void setData(ArticleListModel data) {
        this.data = data;
    }

    public  class ArticleListModel{
        List<TopArticleIdModel> top;
        ResultPageModel list;

        public List<TopArticleIdModel> getTop() {
            return top;
        }

        public void setTop(List<TopArticleIdModel> top) {
            this.top = top;
        }

        public ResultPageModel getList() {
            return list;
        }

        public void setList(ResultPageModel list) {
            this.list = list;
        }
    }


}
