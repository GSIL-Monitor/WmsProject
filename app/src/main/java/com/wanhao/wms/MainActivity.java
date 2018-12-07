package com.wanhao.wms;

import android.os.Bundle;

import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.bean.LoginResult;
import com.wanhao.wms.bean.base.Token;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.ui.HomeActivity;
import com.wanhao.wms.ui.LoginActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Token token = Token.getToken();
        LoginResult user = LoginResult.getUser();
        if (user != null && token != null) {
            OkHttpHeader.HeaderSetting.setHeaderMap(token.getMap());
            startActivity(HomeActivity.class);
            finish();
            return;
        }

        startActivity(LoginActivity.class);
//        startActivity(EnterStorageActivity.class);
        finish();
    }
}
