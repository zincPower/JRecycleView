package com.zinc.jrecycleview.anim.dialog;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-24 22:07
 * email        : 56002982@qq.com
 * desc         : 动画数据
 * version      : 1.0.0
 */
public class AnimData {

    private int type;
    private String name;

    public AnimData(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
