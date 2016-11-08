package com.bameng.model;

/**
 * Created by 47483 on 2016.11.07.
 */

public class TopArticleIdModel {
    int ArticleId;
    String ArticleCover;
    String ArticleTitle;
    String ArticleIntro;
    int BrowseAmount;
    String PublishTime;
    String ArticleUrl;

    public int getArticleId() {
        return ArticleId;
    }

    public void setArticleId(int articleId) {
        ArticleId = articleId;
    }

    public String getArticleCover() {
        return ArticleCover;
    }

    public void setArticleCover(String articleCover) {
        ArticleCover = articleCover;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getArticleIntro() {
        return ArticleIntro;
    }

    public void setArticleIntro(String articleIntro) {
        ArticleIntro = articleIntro;
    }

    public int getBrowseAmount() {
        return BrowseAmount;
    }

    public void setBrowseAmount(int browseAmount) {
        BrowseAmount = browseAmount;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

    public String getArticleUrl() {
        return ArticleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        ArticleUrl = articleUrl;
    }
}
