package com.zinc.jrecycleview.mix.dialog;

/**
 * author       : zinc
 * time         : 2019-08-24 23:33
 * desc         :
 * version      :
 */
public class MixShowInfoData {

    private int type;
    private String name;
    private boolean isSelect;

    public MixShowInfoData(int type, String name) {
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
