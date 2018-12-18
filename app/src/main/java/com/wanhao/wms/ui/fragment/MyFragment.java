package com.wanhao.wms.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseFragment;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.LoginResult;
import com.wanhao.wms.bean.WarehouseBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.Token;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.LoginActivity;
import com.wanhao.wms.ui.WarehouseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
@BindLayout(layoutRes = R.layout.frag_my, title = "我的", addStatusBar = true,backRes = 0)
public class MyFragment extends BaseFragment {


    @BindView(R.id.frag_my_code_tv)
    TextView mCodeTv;
    @BindView(R.id.frag_my_name_tv)
    TextView mNameTv;
    @BindView(R.id.frag_my_logout_tv)
    TextView mLogoutTv;
    @BindView(R.id.frag_my_select_warehouse_tv)
    TextView mSelectWarehouseTv;

    public static MyFragment newInstance() {

        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);

        LoginResult user = LoginResult.getUser();
        if (user != null) {
            mNameTv.setText(user.getUserName());
            mCodeTv.setText(user.getUserCode());
        }

        mLogoutTv.setOnClickListener(this);

        mSelectWarehouseTv.setOnClickListener(this);
        changeWarehouse(WarehouseBean.getSelectWarehouse());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeWarehouse(WarehouseBean warehouseBean) {
        if (warehouseBean == null) {
            mSelectWarehouseTv.setText("请选择仓库");
            return;
        }

        mSelectWarehouseTv.setText("当前仓库:" + warehouseBean.getWhCode() + "-" + warehouseBean.getWhName());
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == mSelectWarehouseTv.getId()) {
            startActivity(WarehouseActivity.class);
        } else if (v.getId() == mLogoutTv.getId()) {
            QMUIDialog dialog = new QMUIDialog.MessageDialogBuilder(getContext())
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.cancel();
                        }
                    }).addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.cancel();
                            OkHttpHeader.post(UrlApi.logout, null, new BaseResultCallback() {
                                @Override
                                protected void onResult(BaseResult resultObj, int id) {

                                }

                                @Override
                                protected void onFailed(String error, int code) {

                                }
                            });
                            Token.getToken().delete();
                            LoginResult.getUser().delete();
                            startActivity(LoginActivity.class);
                            getActivity().finish();
                        }
                    }).setTitle("提示")
                    .setMessage("确定退出登录吗？")
                    .show();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
