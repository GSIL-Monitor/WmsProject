package com.wanhao.wms.ui;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BaseFragment;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.WarehouseBean;
import com.wanhao.wms.bean.base.Token;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.ui.fragment.FunctionFragment;
import com.wanhao.wms.ui.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@BindLayout(layoutRes = R.layout.activity_home, bindTopBar = false)
public class HomeActivity extends BaseActivity {

    public static final int R_SELECT_WAREHOUSE = 2;

    @BindView(R.id.activity_home_nav_my)
    ViewGroup navMy;
    @BindView(R.id.activity_home_nav_index)
    ViewGroup navIndex;//首页

    ViewGroup lastNav;

    private List<BaseFragment> fragments = new ArrayList<>();

    @Override
    public void initStatusBar() {
        super.initStatusBar();

    }

    @Override
    public void initData() {
        super.initData();
        fragments.add(FunctionFragment.newInstance());
        fragments.add(MyFragment.newInstance());
        WarehouseBean selectWarehouse = WarehouseBean.getSelectWarehouse();
        Token token = Token.getToken();
        Map<String, String> map = token.getMap();
        if (selectWarehouse != null) {

            map.put("whCode",selectWarehouse.getWhCode());
        }
        OkHttpHeader.HeaderSetting.setHeaderMap(map);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        navIndex.setTag(0);
        navIndex.setOnClickListener(this);
        navMy.setTag(1);
        navMy.setOnClickListener(this);


        myChangeFragment(R.id.function_fl_content, fragments.get(0));
        setNavSelect(navIndex);
        lastNav = navIndex;
        WarehouseBean selectWarehouse = WarehouseBean.getSelectWarehouse();
        if (selectWarehouse == null) {
            startActivity(WarehouseActivity.class, R_SELECT_WAREHOUSE);
        }
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        switch (v.getId()) {
            case R.id.activity_home_nav_index:
            case R.id.activity_home_nav_my:
                if (v.equals(lastNav)) {
                    return;
                }
                setNavNormal(lastNav);
                int tag = (int) v.getTag();
                lastNav = (ViewGroup) v;
                setNavSelect(lastNav);
                myChangeFragment(R.id.function_fl_content, fragments.get(tag));

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R_SELECT_WAREHOUSE:
                if (WarehouseBean.getSelectWarehouse() == null) {
                    QMUIDialog a = new QMUIDialog.MessageDialogBuilder(this)
                            .setMessage("如不选择仓库则进行退出操作!")
                            .addAction("选择仓库", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    startActivity(WarehouseActivity.class, R_SELECT_WAREHOUSE);
                                    dialog.cancel();
                                }
                            })
                            .addAction("退出登录", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    startActivity(LoginActivity.class);
                                    dialog.cancel();
                                    finish();
                                }
                            }).show();
                    a.setCancelable(false);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setNavNormal(ViewGroup vp) {
        vp.setSelected(false);
    }

    private void setNavSelect(ViewGroup vp) {
        vp.setSelected(true);
    }
}
