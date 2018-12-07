package com.wanhao.wms.http;

import com.wanhao.wms.C;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.utils.DataUtils;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
public abstract class BaseResultCallback extends ResultCallback {

    @Override
    protected void onSuccess(String response, int id) {
        BaseResult resultObj = C.sGson.fromJson(response, BaseResult.class);
        if (!resultObj.isRs()) {
            onFailed(resultObj.getMessage(),id);
            return;
        }
        onResult(resultObj,id);
    }

    protected abstract void onResult(BaseResult resultObj, int id);
}
