package com.bameng.model;

import com.bameng.widgets.custom.AdBannerWidget;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class AdBannerConfig {
    private List<SlideListModel> images;
    /**
     * 是否自动播放
     */
    private boolean autoPlay=false;
    private int width;
    private int height;

    public List<SlideListModel> getImages() {
        return images;
    }

    public void setImages(List<SlideListModel> images) {
        this.images = images;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
