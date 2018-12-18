package com.wanhao.wms;

import android.content.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.bean.Ip;
import com.wanhao.wms.info.OkHttpInfo;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.utils.ActivityUtils;

import org.litepal.LitePalApplication;

import java.lang.reflect.Field;

/**
 * 文件下载地址 HttpDownloadHelper ----Utils
 * http://www.wanandroid.com/blog/show/2080
 * Created by mrqiu on 2017/10/15.
 */
public class MyApp extends LitePalApplication {
    private static MyApp instance;
    private static final String TAG = "MyApp";

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        OkHttpInfo.initOkHttpCard(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        C.SCREEN_WIDTH = displayMetrics.widthPixels;
        C.SCREEN_HEIGHT = displayMetrics.heightPixels;
        C.SCREEN_HEIGHT_3 = (int) (C.SCREEN_WIDTH * 3.0f / 4);
        C.SCREEN_HEIGHT_9 = (int) (C.SCREEN_WIDTH * 9.0f / 16);
        registerClipEvents();
        if (Ip.getLastIp() == null) {
            //    public static  String baseUrl = "http://112.64.179.46:8086/sl-wms-app/sl/pt/api/app/v1";
            Ip ip = new Ip();
            ip.setPort(8086);
            ip.setIp("112.64.179.46");
            ip.save();
        }
        UrlApi.changeIp();

    }


    private void registerClipEvents() {

        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {

                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();

                    if (addedText != null) {
                        BaseActivity top = ActivityUtils.getTop();
                        if (top == null) {
                            return;
                        }
                        top.onToDecode(addedText.toString());
                    }
                }
            }
        });
    }


}
