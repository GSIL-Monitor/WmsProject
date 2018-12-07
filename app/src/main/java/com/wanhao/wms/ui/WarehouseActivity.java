package com.wanhao.wms.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.MemoryFile;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.WarehouseBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.widget.InputSearchView;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

@BindLayout(layoutRes = R.layout.activity_warehouse, title = "仓库选择", addStatusBar = true)
public class WarehouseActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.warehouse_rv)
    RecyclerView mWarehouseRv;
    @BindView(R.id.empty)
    QMUIEmptyView qmuiEmptyView;
    @BindView(R.id.warehouse_inputSearchView)
    InputSearchView mSearchView;

    private DocAdapter mDocAdapter = new DocAdapter();

    private List<IDoc> mWarehouseList = new ArrayList<>();

    private List<IDoc> mFilterList = new ArrayList<>();

    private List<IDoc> mSrcList = new ArrayList<>();

    @Override
    public void initWidget() {
        super.initWidget();
        mWarehouseRv.setLayoutManager(new LinearLayoutManager(this));
        mWarehouseRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20, Color.GRAY));
        mWarehouseRv.setAdapter(mDocAdapter);

        qmuiEmptyView.show(true);
        loadData();

        mDocAdapter.setOnItemClickListener(this);

        mSearchView.setOnSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = mSearchView.getText();
                if (TextUtils.isEmpty(text)) {
                    if (mFilterList.size() == mSrcList.size()) {
                        return;
                    }
                    mFilterList.clear();
                    mFilterList.addAll(mSrcList);
                    mDocAdapter.notifyDataSetChanged();
                    return;
                }
                mFilterList.clear();
                for (IDoc iDoc : mSrcList) {
                    WarehouseBean b = (WarehouseBean) iDoc;
                    if (b.getWhName().indexOf(text.toString()) != -1
                            || b.getWhCode().indexOf(text.toString()) != -1
                            || b.getWhDescr().indexOf(text.toString()) != -1) {
                        mFilterList.add(iDoc);
                    }
                }
                mDocAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadData() {
        OkHttpHeader.post(UrlApi.warehouseList, null, new BaseResultCallback() {
            @Override
            protected void onResult(BaseResult resultObj, int id) {
                cancelLoadingDialog();
                if (!resultObj.isRs()) {
                    return;
                }
                ArrayList<WarehouseBean> list = resultObj.getList(WarehouseBean.class);
                mSrcList.addAll(list);
                mFilterList.addAll(list);
                mDocAdapter.setNewData(mFilterList);
                if (list == null || list.isEmpty()) {
                    qmuiEmptyView.show(false, "当前无数据", "", "点击重新获得数据", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qmuiEmptyView.show(true);
                            loadData();
                        }
                    });
                    return;
                }
                qmuiEmptyView.hide();

            }

            @Override
            protected void onFailed(String error, int code) {
                cancelLoadingDialog();
                displayMessageDialog(error);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        IDoc iDoc = mWarehouseList.get(position);
        WarehouseBean warehouseBean = (WarehouseBean) iDoc;
        warehouseBean.saveSingle();
        setResult(Activity.RESULT_OK);
        EventBus.getDefault().post(warehouseBean);
        finish();

    }
}
