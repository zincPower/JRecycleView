package com.zinc.jrecycleview.data;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description
 */

public class SwipeData {

    private int type;
    private String content;

    private int readNum;

    public SwipeData(int type, String content, int readNum) {
        this.type = type;
        this.content = content;
        this.readNum = readNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }
}
