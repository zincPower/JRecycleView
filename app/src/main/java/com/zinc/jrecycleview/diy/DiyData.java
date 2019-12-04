package com.zinc.jrecycleview.diy;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-19 12:13
 * desc         : 自定义联动数据
 * version      : 1.0.0
 */
public class DiyData {

    private int type;
    private String content;

    DiyData(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
