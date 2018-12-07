package com.wanhao.wms.utils;

import android.text.TextUtils;

import com.wanhao.wms.bean.PurchaseOrderDetails;
import com.wanhao.wms.bean.base.IGoodsDecode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public class GoodsUtils {

    public static boolean isSame(PurchaseOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }
}
