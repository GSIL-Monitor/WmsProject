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
import com.wanhao.wms.bean.QueryGoods;
import com.wanhao.wms.bean.QueryGoods;
import com.wanhao.wms.bean.Sn;
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
public class SnListActivity extends BaseActivity {
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
    private QueryGoods mGoods;


    public static void put(List<Sn> mSnList, QueryGoods purchaseOrderDetails, Bundle bundle) {
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
        mGoods = JsonUtils.fromJson(getBundle().getString(GOODS), QueryGoods.class);
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


        mSearchView.setVisibility(View.GONE);
        mSnAdapter.setCanRemove(false);
    }


    @Override
    public void onClickKeyToBack() {
        super.onClickKeyToBack();
    }


}
