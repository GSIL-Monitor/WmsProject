package com.wanhao.wms.base;

import android.os.Bundle;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public interface IToAction {
    void startActivity(Class t);

    void startActivity(Class t, int requestCode);

    void startActivity(Class t, Bundle bundle);

    void startActivity(Class t, Bundle bundle, int requestCode);

    void activityFinish();
}
