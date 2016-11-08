package com.bameng.model;

/**
 * 下拉列表上拉下拉选择器
 */
public enum OperateTypeEnum {

    REFRESH("刷新",1),LOADMORE("加载更多",2);

    private OperateTypeEnum(String name , int index){
        this.name=name;
        this.index=index;
    }

    // 普通方法
    public static String getName(int index) {
        for (OperateTypeEnum c : OperateTypeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String name;
    private int index;
}
