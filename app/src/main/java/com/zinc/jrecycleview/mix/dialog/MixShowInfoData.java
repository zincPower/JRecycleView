package com.zinc.jrecycleview.mix.dialog;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-24 23:33
 * email        : 56002982@qq.com
 * desc         : 混合选择列表数据
 * version      : 1.0.0
 */
public class MixShowInfoData {

    private int type;
    private String name;
    private boolean isSelect;

    MixShowInfoData(int type, String name) {
        this.type = type;
        this.name = name;
        this.isSelect = false;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
