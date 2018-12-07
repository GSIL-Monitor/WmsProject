package com.wanhao.wms.ui.function.enter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.PurchaseOrder;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.Page;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.EnterDocOperateActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.doc.DefaultDocPresenter;
import com.wanhao.wms.ui.function.base.goods.GoodsLIstActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.production)
public class ProductionOrderPresenter extends DefaultDocPresenter {
    private List<IDoc> mDocs = new ArrayList<>();

    private Page page = new Page();
    private Map<String, Object> mParams = new HashMap<>();


    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        EventBus.getDefault().register(this);
    }

    public void loadData() {
        page.put(mParams);
        mDocAdapter.setEnableLoadMore(false);
        OkHttpHeader.post(UrlApi.productionOrder, mParams, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {

                ArrayList<PurchaseOrder> list = resultObj.getList(PurchaseOrder.class);
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
        PurchaseOrder iDoc = (PurchaseOrder) mDocs.get(position);
        Bundle bundle = new Bundle();
        GoodsLIstActivity.putPresenter(ProductionGoodsPresenter.class, bundle);
        ProductionGoodsPresenter.putDoc(iDoc, bundle);
        iToAction.startActivity(GoodsLIstActivity.class, bundle);
    }

    @Override
    public void handlerClickSearch(CharSequence text) {
        super.handlerClickSearch(text);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        loadFilter(text.toString());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        loadFilter("");
    }

    private void loadFilter(String doc_value) {
        mDocs.clear();
        mDocAdapter.notifyDataSetChanged();
        page.setPage(1);
        mParams.put("qsparam", doc_value);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeData(Integer i) {
        if (i == 0) {
            onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
