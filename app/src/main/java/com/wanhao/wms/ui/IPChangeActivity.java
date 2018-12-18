package com.wanhao.wms.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.Ip;
import com.wanhao.wms.info.UrlApi;

@BindLayout(layoutRes = R.layout.activity_ipchange, title = "ip修改", addStatusBar = true)
public class IPChangeActivity extends BaseActivity {

    @BindView(R.id.ip_et)
    EditText mIpEt;
    @BindView(R.id.ip_port_et)
    EditText mPortEt;
    @BindView(R.id.ip_save_action_tv)
    View mSaveActionTv;


    @Override
    public void initWidget() {
        super.initWidget();
        Ip lastIp = Ip.getLastIp();
        mIpEt.setText(lastIp.getIp());
        mPortEt.setText(String.valueOf(lastIp.getPort()));

        mSaveActionTv.setOnClickListener(this);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == mSaveActionTv.getId()) {
            String s = mIpEt.getText().toString();
            if (TextUtils.isEmpty(s)) {
                return;
            }
            String s1 = mPortEt.getText().toString();
            if (TextUtils.isEmpty(s1)) {
                return;
            }
            Ip ip = new Ip();
            ip.setIp(s);
            ip.setPort(Integer.parseInt(s1));
            ip.save();

            UrlApi.changeIp();
            finish();
        }
    }
}
