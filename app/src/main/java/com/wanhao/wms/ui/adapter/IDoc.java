package com.wanhao.wms.ui.adapter;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public interface IDoc {
    CharSequence getDocNoLabel();

    CharSequence getDocNo();

    List<ILabel> getTables();

    boolean isShowSn();
}
