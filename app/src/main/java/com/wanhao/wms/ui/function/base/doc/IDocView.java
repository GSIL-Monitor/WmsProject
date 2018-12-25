package com.wanhao.wms.ui.function.base.doc;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public interface IDocView {

    void setAdapter(RecyclerView.Adapter adapter);

    void setInputTextHint(@StringRes int hint);

    void setInputText(CharSequence charSequence);

    RecyclerView getListView();

    void setRefresh(boolean refresh);

    void setTopbarTitle(int titleRes);

    void setBottomTvVisibility(int i);

    void setBottomTvTextRes(int new_box);
}
