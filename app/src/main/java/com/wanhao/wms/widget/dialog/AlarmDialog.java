package com.wanhao.wms.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wanhao.wms.R;


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/6/1
 *
 * @author ql
 */

public class AlarmDialog extends Toast {
    private Toast toast;
    private Context context;
    private TextView noticeText;

    public AlarmDialog(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_alarm_ui, (ViewGroup)null);
        this.noticeText = (TextView)layout.findViewById(R.id.noticeText);
        this.toast = new Toast(context);
        this.toast.setGravity(17, 0, 0);
        this.toast.setDuration(0);
        this.toast.setView(layout);
    }

    public void setShowText(CharSequence dialogNotice) {
        this.noticeText.setText(dialogNotice);
    }

    public void show() {
        this.toast.show();
    }
}
