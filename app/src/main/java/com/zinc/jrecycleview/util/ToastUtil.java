package com.zinc.jrecycleview.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

/**
 *
 * @date 创建时间：2018/4/12
 * @author Jiang zinc
 * @description
 *
 */

public class ToastUtil {

    private static Toast sToast;

    @SuppressLint("ShowToast")
    public static void init(Application context) {
        sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void show(String message) {
        sToast.setText(message);
        sToast.show();
    }
}
