package com.wanhao.wms.ui.function;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.C;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.PickingOrder;
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.PickingOrder;
import com.wanhao.wms.bean.PickingOrderDetails;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BindLayout(layoutRes = R.layout.activity_enter_doc_details, title = "任务单列表", addStatusBar = true)
public class PickingDocDetailsActivity extends BaseActivity {

    @BindView(R.id.docDetails_rv)
    RecyclerView mDocDetailsRv;
    @BindView(R.id.enterDocDetails_docNo_tv)
    TextView mDocNoTv;

    private DocAdapter docAdapter = new DocAdapter();
    private Map<String, Object> mParams = new HashMap<>();
    private List<IDoc> mGoods = new ArrayList<>();
    private PickingOrder purchaseOrder;
    private String url;

    public static void put(PickingOrder purchaseOrder, Bundle bundle) {
        bundle.putString("a", C.sGson.toJson(purchaseOrder));
    }

    public static void putLoadUrl(String url, Bundle bundle) {
        bundle.putString("url", url);
    }

    @Override
    public void initData() {
        super.initData();
        purchaseOrder = C.sGson.fromJson(getBundle().getString("a"), PickingOrder.class);
        url = getBundle().getString("url");
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mDocNoTv.setText(String.valueOf(purchaseOrder.getPickCode()));
        mDocDetailsRv.setLayoutManager(new LinearLayoutManager(this));
        mDocDetailsRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20, Color.GRAY));
        mDocDetailsRv.setAdapter(docAdapter);
        displayLoadingDialog("加载数据");
        loadData();
        docAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData();
            }
        }, mDocDetailsRv);
    }


    private void loadData() {
        mParams.put("pickCode", purchaseOrder.getPickCode());
        docAdapter.loadMoreEnd(false);
        OkHttpHeader.post(url, mParams, new BaseResultCallback() {
            @Override
            protected void onResult(BaseResult resultObj, int id) {
                cancelLoadingDialog();
                ArrayList<PickingOrderDetails> list = resultObj.getList(PickingOrderDetails.class);
                if (list != null) {
                    mGoods.addAll(list);
                }
                docAdapter.setNewData(mGoods);
                docAdapter.notifyDataSetChanged();

                docAdapter.loadMoreEnd();

            }

            @Override
            protected void onFailed(String error, int code) {
                cancelLoadingDialog();
                displayMessageDialog(error);
                docAdapter.loadMoreFail();
            }
        });
    }
}
