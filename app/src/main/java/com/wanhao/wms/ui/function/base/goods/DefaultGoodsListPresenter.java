package com.wanhao.wms.ui.function.base.goods;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.IDoc;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public class DefaultGoodsListPresenter extends AbsGoodsListPresenter implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    public DocAdapter mDocAdapter = new DocAdapter();
    private Bundle bundle;

    @Override
    public void setAdapter(RecyclerView mGoodsRv) {
        mGoodsRv.setAdapter(mDocAdapter);
        mDocAdapter.setOnItemClickListener(this);
        mDocAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void actionSubmit() {

    }

    @Override
    public void init(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void actionDocDetails() {

    }

    @Override
    public void handleClickSearch(CharSequence text) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void actionClickSelectTv1() {

    }

    @Override
    public void actionClickSelectTv2() {

    }


    @Override
    public void decode(String decode) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Object o = adapter.getData().get(position);
        if (o instanceof IDoc) {
            IDoc d = (IDoc) o;
            if (d.isShowSn()) {
                handleClickSeeSnList(d);
            }
        }
    }

    public void handleClickSeeSnList(IDoc d) {

    }

    public Bundle getBundle() {
        return bundle;
    }
}
