package com.wanhao.wms.ui.function;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.TransferOutDetailsBean;
import com.wanhao.wms.bean.Sn;
import com.wanhao.wms.bean.TransferOutDetailsBean;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.SnAdapter;
import com.wanhao.wms.ui.widget.InputSearchView;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@BindLayout(layoutRes = R.layout.activity_sn_list, titleRes = R.string.sn_title, addStatusBar = true)
public class TransferSnListActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {
    public static final String SN_LIST = "snLIst";
    public static final String GOODS = "goods";
    @BindView(R.id.snList_goodsDetails_rv)
    RecyclerView mGoodsDetailsRv;
    @BindView(R.id.snList_rv)
    RecyclerView mSnRv;
    @BindView(R.id.snList_searchView)
    InputSearchView mSearchView;
    @BindView(R.id.snList_save_tv)
    TextView mSaveTv;

    private DocAdapter mDocAdapter = new DocAdapter();
    private SnAdapter mSnAdapter = new SnAdapter();

    private List<Sn> mSnList = new ArrayList<>();
    private TransferOutDetailsBean mGoods;
    private int id;

    DecodeCallback callback = new DecodeCallback(MarkRules.GOODS_MARK_CODE) {

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            cancelLoadingDialog();
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            if (GoodsUtils.isSame(mGoods, data)) {
                String sn_no = data.getSN_NO();
                if (TextUtils.isEmpty(sn_no)) {
                    displayMessageDialog("序列号不正确");
                    return;
                }

                for (Sn sn : mSnList) {
                    if (sn.getSnNo().equals(sn_no)) {
                        displayMessageDialog("已经添加过了");
                        return;
                    }
                }

                Sn e = new Sn();
                e.setSnNo(sn_no);
                e.setPorderId(id);
                mSnList.add(e);
                mSnAdapter.notifyDataSetChanged();
            } else {
                displayMessageDialog("任务单不存在该货品->sku=" + data.getSKU_CODE());
            }

        }

        @Override
        public void onOtherCode(DecodeBean data) {
            displayMessageDialog("解码类型不匹配");
        }

        @Override
        protected void onFailed(String error, int code) {
            displayMessageDialog(error);
        }
    };

    public static void put(List<Sn> mSnList, TransferOutDetailsBean purchaseOrderDetails, Bundle bundle) {
        purchaseOrderDetails.setLabels(null);
        bundle.putString(GOODS, JsonUtils.toJson(purchaseOrderDetails));
        bundle.putString(SN_LIST, JsonUtils.toJson(mSnList));
    }

    @Override
    public void initData() {
        super.initData();
        Type t = new TypeToken<List<Sn>>() {
        }.getType();
        mSnList = JsonUtils.fromTypeJson(getBundle().getString(SN_LIST), t);
        id = mSnList.get(0).getPorderId();
        mGoods = JsonUtils.fromJson(getBundle().getString(GOODS), TransferOutDetailsBean.class);
        mDocAdapter.addData(mGoods);
        mSnAdapter.setNewData(mSnList);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mSaveTv.setOnClickListener(this);

        mGoodsDetailsRv.setLayoutManager(new LinearLayoutManager(this));
        mGoodsDetailsRv.setAdapter(mDocAdapter);

        mSnRv.setLayoutManager(new LinearLayoutManager(this));
        mSnRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20, Color.GRAY));
        mSnRv.setAdapter(mSnAdapter);

        mSnAdapter.setOnItemChildClickListener(this);

        mSearchView.setOnSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSearchView.getText().toString().trim())) {
                    return;
                }
                onToDecode(mSearchView.getText().toString().trim());
            }
        });
        mSearchView.setOnScanningListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScanningActivity();
            }
        });
    }

    @Override
    public void onResultCode(String resultContent) {
        super.onResultCode(resultContent);
        onToDecode(resultContent);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == mSaveTv.getId()) {
            mGoods.setSnList(mSnList);
            EventBus.getDefault().post(mGoods);
            finish();
        }
    }

    @Override
    public void onClickKeyToBack() {
        super.onClickKeyToBack();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        mSnList.remove(position);
        mGoods.setNowQty(mSnList.size());
        mGoods.setSnList(mSnList);
        mDocAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onToDecode(String code) {
        super.onToDecode(code);
        displayLoadingDialog("解码中");
        DecodeHelper.decode(code, callback);
    }
}
