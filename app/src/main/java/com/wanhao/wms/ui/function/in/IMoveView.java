package com.wanhao.wms.ui.function.in;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/12
 *
 * @author ql
 */
public interface IMoveView {

    void setRackLabel(@StringRes int label);

    void setRack(CharSequence targetRack);

    void setAdapter(RecyclerView.Adapter adapter);

    RecyclerView getRecyclerView();

    void setTitle(@StringRes int  title);

}
