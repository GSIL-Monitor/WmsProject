package com.wanhao.wms.ui.function.base.doc;

import android.os.Bundle;

import com.wanhao.wms.base.IDialog;
import com.wanhao.wms.base.IToAction;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public abstract class AbsDocPresenter implements DocPresenter {


    public IToAction iToAction;
    public IDocView iDocView;
    public IDialog iDialog;


    @Override
    public void init(Bundle bundle) {

    }

    @Override
    public void decode(String decode) {

    }

    @Override
    public void actionClickBottomTv() {

    }
}
