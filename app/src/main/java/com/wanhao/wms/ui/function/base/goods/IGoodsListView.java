package com.wanhao.wms.ui.function.base.goods;

import android.widget.TextView;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public interface IGoodsListView {


    void setRackTextView(CharSequence charSequence);


    void setTopbarTitle(int titleRes);

    void setSelectGroupVisibility(int v);

    void setSelectText1(CharSequence t1);

    void setSelectText2(CharSequence t2);

    TextView getSelectTv1();

    TextView getSelectTv2();

    void setRackViewGroupVisibility(int v);

    void setBottomGroupVisibility(int v);

    void setTopbarRightBtnVisibility(int v);
}
