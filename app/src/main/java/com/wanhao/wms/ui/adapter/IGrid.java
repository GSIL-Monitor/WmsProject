package com.wanhao.wms.ui.adapter;

import android.support.annotation.StringRes;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/8/6
 *
 * @author ql
 */
public interface IGrid {
    int getTag();

    int getIconRes();

    CharSequence getLabel();
    @StringRes int getLabelRes();
}
