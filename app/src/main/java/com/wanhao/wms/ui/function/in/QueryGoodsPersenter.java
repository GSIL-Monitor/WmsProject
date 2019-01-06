package com.wanhao.wms.ui.function.in;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.QueryGoods;
import com.wanhao.wms.bean.RackDownDetailsBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.RackDownDocDetailsActivity;
import com.wanhao.wms.ui.function.RackSnListActivity;
import com.wanhao.wms.ui.function.SnListActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/4
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.query_goods)
public class QueryGoodsPersenter extends DefaultGoodsListPresenter {

    private QueryGoods mToChangeGoods;
    Map<String, String> params = new HashMap<>();

    DecodeCallback callback = new DecodeCallback(MarkRules.RACK_MARK_CODE, MarkRules.GOODS_MARK_CODE) {

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            iDialog.cancelLoadingDialog();
        }

        @Override
        public void onRackCode(DecodeBean data) {
            params.clear();
            params.put("locCode", data.getDOC_CODE());
            loadGoodsList();
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            params.clear();
            params.put("skuCode", data.getSKU_CODE());
            loadGoodsList();
        }

        @Override
        public void onOtherCode(DecodeBean data) {
            iDialog.displayMessageDialog(R.string.decode_other);
        }

        @Override
        protected void onFailed(String error, int code) {
            iDialog.displayMessageDialog(error);
        }
    };
    BaseResultCallback loadGoodsCallback = new BaseResultCallback() {
        @Override
        protected void onResult(BaseResult resultObj, int id) {
            ArrayList<QueryGoods> list = resultObj.getList(QueryGoods.class);
            if (list == null || list.size() == 0) {
                iDialog.displayMessageDialog("当前库存为0");
            } else {
                mGoodsList.addAll(list);
            }
            mDocAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onFailed(String error, int code) {
            iDialog.displayMessageDialog(error);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            iDialog.cancelLoadingDialog();
        }
    };
    private List<IDoc> mGoodsList = new ArrayList<>();


    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        mDocAdapter.setNewData(mGoodsList);
        iGoodsListView.setRackViewGroupVisibility(View.GONE);
        iGoodsListView.setBottomGroupVisibility(View.GONE);
        iGoodsListView.setTopbarRightBtnVisibility(View.GONE);
    }


    private void loadGoodsList() {
        mGoodsList.clear();
        mDocAdapter.notifyDataSetChanged();

        OkHttpHeader.post(UrlApi.query_goods, params, loadGoodsCallback);
    }


    @Override
    public void handleClickSearch(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        decode(text.toString());
    }


    @Override
    public void decode(String decode) {
        iDialog.displayLoadingDialog("解码中");
        DecodeHelper.decode(decode, callback);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (QueryGoods) d;
        Bundle bundle = new Bundle();
        SnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(SnListActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
