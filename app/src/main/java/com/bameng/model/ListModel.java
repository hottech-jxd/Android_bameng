package com.bameng.model;

/**
 * Created by 47483 on 2016.11.07.
 */

public class ListModel {
    int ArticleId;
    String ArticleCover;
    String ArticleTitle;
    String ArticleIntro;
    int BrowseAmount;
    String PublishTime;
    String ArticleUrl;
    String PublishTimeText;
    int IsRead=0;
    boolean isTop =false;

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

    public String getPublishTimeText() {
        return PublishTimeText;
    }

    public void setPublishTimeText(String publishTimeText) {
        PublishTimeText = publishTimeText;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }
}
