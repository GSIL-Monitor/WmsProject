package com.wanhao.wms.ui.function.base.doc;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.wanhao.wms.ui.function.base.IDecode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public interface DocPresenter extends IDecode, SwipeRefreshLayout.OnRefreshListener {

    void init(Bundle bundle);

    void handlerClickSearch(CharSequence text);

    @Override
    void onRefresh();

    @Override
    void decode(String decode);

    void onDestroy();

    void actionClickBottomTv();
}
