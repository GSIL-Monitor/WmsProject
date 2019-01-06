package com.wanhao.wms.ui.function.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.OutOrderBean;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.OutOrderBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.Page;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.base.doc.DefaultDocPresenter;
import com.wanhao.wms.ui.function.base.goods.GoodsLIstActivity;
import com.wanhao.wms.utils.JsonUtils;

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
public class ExitCommOrderPresenter extends DefaultDocPresenter {
    private List<IDoc> mDocs = new ArrayList<>();

    private Page page = new Page();
    private Map<String, Object> mParams = new HashMap<>();
    private EnterCommBean enterCommBean;

    public static void put(EnterCommBean enterCommBean, Bundle bundle) {
        bundle.putString("enter_comm_bean", JsonUtils.toJson(enterCommBean));
    }

    @Override
    public void init(Bundle bundle) {
        enterCommBean = JsonUtils.fromJson(bundle.getString("enter_comm_bean"), EnterCommBean.class);
        super.init(bundle);
        iDocView.setTopbarTitle(enterCommBean.getTitleRes());
        EventBus.getDefault().register(this);
    }

    public void loadData() {
        page.put(mParams);
        mDocAdapter.setEnableLoadMore(false);
        OkHttpHeader.post(enterCommBean.getOrderUrl(), mParams, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {

                ArrayList<OutOrderBean> list = resultObj.getList(OutOrderBean.class);
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
    public void handlerClickSearch(CharSequence text) {
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
                iDialog.displayMessageDialog(R.string.decode_other);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OutOrderBean iDoc = (OutOrderBean) mDocs.get(position);
        Bundle bundle = getBundle();
        GoodsLIstActivity.putPresenter(ExitCommOperatePresenter.class, bundle);
        ExitCommOperatePresenter.putDoc(iDoc, bundle);
        iToAction.startActivity(GoodsLIstActivity.class, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeData(Integer i) {
        if (i == 0) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        loadFilter("");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
