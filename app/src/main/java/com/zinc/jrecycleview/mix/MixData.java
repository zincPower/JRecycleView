package com.zinc.jrecycleview.mix;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-24 23:57
 * email        : 56002982@qq.com
 * desc         : 混合适配器
 * version      : 1.0.0
 */
public class MixData {

    private int type;
    private String name;

    MixData(int type, String name) {
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
