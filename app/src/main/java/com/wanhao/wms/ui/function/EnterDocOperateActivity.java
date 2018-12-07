package com.wanhao.wms.ui.function;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.wanhao.wms.C;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.PurchaseOrder;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

@BindLayout(layoutRes = R.layout.activity_enter_doc_operate, title = "采购入库操作", addStatusBar = true)
public class EnterDocOperateActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.enter_storage_operate_submit_tv)
    TextView mSubmitTv;
    @BindView(R.id.enter_storage_operate_rv)
    RecyclerView mGoodsRv;
    @BindView(R.id.enter_storage_rack_tv)
    TextView mRackTv;

    private DocAdapter mDocAdapter = new DocAdapter();
    private List<IDoc> mGoods = new ArrayList<>();
    private PurchaseOrder mOperateOrder;


    public static void put(PurchaseOrder purchaseOrder, Bundle bundle) {
        purchaseOrder.setLabels(null);
        bundle.putString("a", C.sGson.toJson(purchaseOrder));
    }


    @Override
    public void initData() {
        super.initData();
        mOperateOrder = C.sGson.fromJson(getBundle().getString("a"), PurchaseOrder.class);


    }

    @Override
    public void initWidget() {
        super.initWidget();
        mSubmitTv.setOnClickListener(this);

        mGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mGoodsRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20, Color.GRAY));
        mGoodsRv.setAdapter(mDocAdapter);

        mDocAdapter.setOnItemClickListener(this);

        mTopBar.addRightTextButton("详情", R.id.top_bar_right_btn).setOnClickListener(this);
    }

    private void decode(String code) {
        DecodeHelper.decode(code, new DecodeCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                cancelLoadingDialog();
            }

            @Override
            protected void onFailed(String error, int code) {

            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(this);
        final EditText editText = editTextDialogBuilder
                .getEditText();
        editTextDialogBuilder.setPlaceholder("请输入数量");
        editTextDialogBuilder.setTitle("请输入数量")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        QMUIKeyboardHelper.hideKeyboard(editText);
                        dialog.cancel();
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                QMUIKeyboardHelper.hideKeyboard(editText);
                dialog.cancel();
            }
        }).show();
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == R.id.top_bar_right_btn) {
            EnterDocDetailsActivity.put(mOperateOrder, getBundle());
            startActivity(EnterDocDetailsActivity.class, getBundle());
        }
    }
}
