package com.muve.muve_it_driver.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.muve.muve_it_driver.R;
import com.wang.avi.AVLoadingIndicatorView;

public class AppProgressBar {

    public static AVLoadingIndicatorView avi;
    private static Context context;
    private static Dialog m_Dialog;

    public AppProgressBar(Context context) {
        AppProgressBar.context = context;

    }

    public static Dialog getInstance() {
        if (m_Dialog == null) {
            m_Dialog = new Dialog(context);
        }
        return m_Dialog;
    }


    public static void openLoader(Activity activity, String message) {

        if (m_Dialog != null) {
            m_Dialog.dismiss();
        }
        m_Dialog = new Dialog(activity);
        m_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_Dialog.setContentView(R.layout.dialog_loader);
        avi = m_Dialog.findViewById(R.id.avi);
        TextView avi_text = m_Dialog.findViewById(R.id.avi_text);
        avi_text.setText(message);
        m_Dialog.setCancelable(false);
        avi.show();
        m_Dialog.show();
    }



    public static void closeLoader() {

        if (m_Dialog != null) {
            m_Dialog.dismiss();
        }
    }

    public static boolean isShowing() {
        boolean ret = false;
        if (m_Dialog != null && m_Dialog.isShowing()) {
            ret = true;
        }
        return ret;
    }
}
