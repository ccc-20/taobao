package com.example.baekhyun.utils;

import android.widget.Toast;

import com.example.baekhyun.base.BaseApplication;

public class ToastUtil {

    private static Toast sToast;

    public static void getToast(String tips) {
        if (sToast == null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(tips);
        }
        sToast.show();
    }

}
