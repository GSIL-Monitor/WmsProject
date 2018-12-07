package com.wanhao.wms.ui.function.base.doc;

import android.os.Bundle;
import android.util.EventLog;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.base.doc.AbsDocPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public class DefaultDocPresenter extends AbsDocPresenter implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    protected DocAdapter mDocAdapter = new DocAdapter();
    protected List<IDoc> mDocs = new ArrayList<>();

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);

        iDocView.setAdapter(mDocAdapter);

        mDocAdapter.setOnLoadMoreListener(this, iDocView.getListView());
        mDocAdapter.setOnItemClickListener(this);
        mDocAdapter.setNewData(mDocs);

        iDialog.displayLoadingDialog("加载数据中");
        loadData();
    }

    @Override
    public void handlerClickSearch(CharSequence text) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroy() {
    }

    protected void loadData() {

    }


    @Override
    public void onLoadMoreRequested() {
        mDocAdapter.setEnableLoadMore(false);
        loadData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
