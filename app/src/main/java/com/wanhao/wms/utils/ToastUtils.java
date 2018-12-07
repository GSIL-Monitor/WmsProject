package com.wanhao.wms.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.wanhao.wms.widget.dialog.AlarmDialog;


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/6/1
 *
 * @author ql
 */

public class ToastUtils {
    private static AlarmDialog alarmDialog;

    public ToastUtils() {
    }

    public static void showShortToast(Context context, CharSequence showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }

        alarmDialog = new AlarmDialog(context);
        alarmDialog.setShowText(showMsg);
        alarmDialog.setDuration(Toast.LENGTH_SHORT);
        alarmDialog.show();
    }

    public static void showLongToast(Context context, CharSequence showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }

        alarmDialog = new AlarmDialog(context);
        alarmDialog.setShowText(showMsg);
        alarmDialog.show();
    }

    public static void showMomentToast(Activity activity, final Context context, final CharSequence showMsg) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (null == ToastUtils.alarmDialog) {
                    ToastUtils.alarmDialog = new AlarmDialog(context);
                    ToastUtils.alarmDialog.setShowText(showMsg);
                    ToastUtils.alarmDialog.setDuration(Toast.LENGTH_SHORT);
                    ToastUtils.alarmDialog.show();
                } else {
                    ToastUtils.alarmDialog.setShowText(showMsg);
                    ToastUtils.alarmDialog.show();
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (null != ToastUtils.alarmDialog) {
                            ToastUtils.alarmDialog.cancel();
                        }

                    }
                }, 2000L);
            }
        });
    }
}
