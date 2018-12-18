package com.wanhao.wms.ui.function.base.goods;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.wanhao.wms.ui.function.base.IDecode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public interface IGoodsListPresenter extends IDecode {

    void setAdapter(RecyclerView mGoodsRv);

    void actionSubmit();

    void init(Bundle bundle);

    void actionDocDetails();

    void handleClickSearch(CharSequence text);

    void onDestroy();
}
