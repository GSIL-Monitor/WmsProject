package com.wanhao.wms.ui.function.base.doc;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;

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
    private Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        this.bundle = bundle;

        iDocView.setAdapter(mDocAdapter);

        mDocAdapter.setOnLoadMoreListener(this, iDocView.getListView());
        mDocAdapter.setOnItemClickListener(this);
        mDocAdapter.setNewData(mDocs);

        mDocAdapter.setEmptyView(R.layout.empty_view);

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

    @Override
    public void actionClickBottomTv() {

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
