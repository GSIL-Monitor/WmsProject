package com.wanhao.wms.ui;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDirection;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.wanhao.wms.C;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.LoginResult;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.Token;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.http.OkHttpHelper;
import com.wanhao.wms.http.ResultCallback;
import com.wanhao.wms.info.UrlApi;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@BindLayout(layoutRes = R.layout.activity_login, bindTopBar = false)
public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_logo_iv)
    ImageView mLogoIv;
    @BindView(R.id.login_action_tv)
    TextView mLoginTv;
    @BindView(R.id.login_account_et)
    EditText mAccountEt;
    @BindView(R.id.login_pw_et)
    EditText mPwEt;
    @BindView(R.id.login_ip_change_action)
    TextView mIpChangeActionTv;

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarDarkMode(this);

    }

    @Override
    public void initWidget() {
        super.initWidget();
        mIpChangeActionTv.setOnClickListener(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(C.SCREEN_WIDTH / 3, C.SCREEN_WIDTH / 3);
        layoutParams.topMargin = C.SCREEN_WIDTH / 4;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mLogoIv.setLayoutParams(layoutParams);

        mLoginTv.setOnClickListener(this);
        mAccountEt.setText("admin");
        mPwEt.setText("123456");
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == mIpChangeActionTv.getId()) {
            startActivity(IPChangeActivity.class);
        } else if (v.getId() == mLoginTv.getId()) {
            final String account = mAccountEt.getText().toString();
            final String pw = mPwEt.getText().toString();

            if (TextUtils.isEmpty(account)) {
                return;
            }
            if (TextUtils.isEmpty(pw)) {
                return;
            }

            displayLoadingDialog("登录中");
            OkHttpHelper.post(UrlApi.token, null, new BaseResultCallback() {
                @Override
                protected void onResult(BaseResult resultObj, int id) {
                    if (!resultObj.isRs()) {
                        cancelLoadingDialog();
                        displayMessageDialog(resultObj.getMessage());
                        return;
                    }
                    Token data = resultObj.getData(Token.class);
                    data.saveSingle();
                    OkHttpHeader.HeaderSetting.setHeaderMap(data.getMap());
                    Map<String, String> loginMap = new HashMap<>();
                    loginMap.put("username", account);
                    loginMap.put("userpass", pw);
                    OkHttpHeader.post(UrlApi.login, loginMap, new BaseResultCallback() {
                        @Override
                        protected void onResult(BaseResult resultObj, int id) {


                            cancelLoadingDialog();
                            if (!resultObj.isRs()) {
                                displayMessageDialog(resultObj.getMessage());
                                return;
                            }
                            LoginResult loginResult = resultObj.getData(LoginResult.class);
                            loginResult.saveSingle();
                            Token token = Token.getToken();
                            token.setAppToken(loginResult.getToken());
                            token.setAppTs(loginResult.getTs());
                            token.saveSingle();
                            OkHttpHeader.HeaderSetting.setHeaderMap(token.getMap());
                            OkHttpHeader.post(UrlApi.markRules, null, new BaseResultCallback() {
                                @Override
                                protected void onResult(BaseResult resultObj, int id) {
                                    if (resultObj.isRs()) {
                                        ArrayList<MarkRules> list = resultObj.getList(MarkRules.class);
                                        for (MarkRules markRules : list) {
                                            markRules.saveSingle();
                                        }
                                    }
                                }

                                @Override
                                protected void onFailed(String error, int code) {

                                }
                            });
                            startActivity(HomeActivity.class);
                            finish();
                        }

                        @Override
                        protected void onFailed(String error, int code) {
                            cancelLoadingDialog();
                            displayMessageDialog(error);
                        }
                    });
                }

                @Override
                protected void onFailed(String error, int code) {
                    cancelLoadingDialog();
                    displayMessageDialog(error);
                }

            });
        }
    }
}
