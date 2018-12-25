package com.wanhao.wms.ui.function.in;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.BoxOrder;
import com.wanhao.wms.bean.LoginResult;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.WarehouseBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.Page;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.doc.DefaultDocPresenter;
import com.wanhao.wms.ui.function.base.goods.GoodsLIstActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/24
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.label_box)
public class TakeBoxDocPresenter extends DefaultDocPresenter {

    private Page page = new Page();
    private Map<String, Object> mParams = new HashMap<>();

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iDocView.setBottomTvVisibility(View.VISIBLE);
        iDocView.setBottomTvTextRes(R.string.new_box);
    }

    @Override
    protected void loadData() {
        super.loadData();
        page.put(mParams);
        mDocAdapter.setEnableLoadMore(false);
        OkHttpHeader.post(UrlApi.take_box_order, mParams, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {

                ArrayList<BoxOrder> list = resultObj.getList(BoxOrder.class);
                if (list != null) {
                    mDocs.addAll(list);
                }

                mDocAdapter.setNewData(mDocs);
                mDocAdapter.notifyDataSetChanged();
                if (mDocs.size() < resultObj.getTotal()) {
                    page.add();
                } else {
                    mDocAdapter.loadMoreEnd();
                }
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
                mDocAdapter.setEnableLoadMore(true);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        super.onItemClick(adapter, view, position);
        BoxOrder iDoc = (BoxOrder) mDocs.get(position);
        TakeBoxOperatePresenter.put(iDoc, getBundle());
        GoodsLIstActivity.putPresenter(TakeBoxOperatePresenter.class, getBundle());
        iToAction.startActivity(GoodsLIstActivity.class, getBundle());
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        mDocs.clear();
        mDocAdapter.notifyDataSetChanged();
        page.setPage(1);
        loadData();

    }

    @Override
    public void handlerClickSearch(CharSequence text) {
        super.handlerClickSearch(text);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        decode(text.toString());
    }

    @Override
    public void decode(String decode) {
        iDialog.displayLoadingDialog("解码中");
        DecodeHelper.decode(decode, new DecodeCallback(MarkRules.DOC_MARK_CODE) {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            public void onOtherCode(DecodeBean data) {
                super.onOtherCode(data);
                iDialog.displayMessageDialog("解码类型不匹配");
            }

            @Override
            public void onDocCode(DecodeBean data) {
                super.onDocCode(data);
                loadFilter(data.getDOC_VALUE());
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
                mDocAdapter.loadMoreFail();
            }
        });
    }


    private void loadFilter(String doc_value) {
        mDocs.clear();
        mDocAdapter.notifyDataSetChanged();
        page.setPage(1);
        mParams.put("qsparam", doc_value);
        loadData();
    }

    @Override
    public void actionClickBottomTv() {
        super.actionClickBottomTv();
        iDialog.displayLoadingDialog("创建箱子中...");
        Map<String, Object> map = new HashMap<>();
        map.put("whCode", WarehouseBean.getSelectWarehouse().getWhCode());
        map.put("empCode", LoginResult.getUser().getUserCode());
        OkHttpHeader.post(UrlApi.take_box_order_new, map, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                iDialog.displayMessageDialog("创建成功");
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }
}
