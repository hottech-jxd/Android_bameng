package com.bameng.model;

/**
 * Created by Administrator on 2017/1/3.
 */

public class BadgeCommentEvent {
    private boolean hasComment=false;
    public BadgeCommentEvent(boolean hasComment){
        this.hasComment = hasComment;
    }

    public boolean isHasComment() {
        return hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }
}
