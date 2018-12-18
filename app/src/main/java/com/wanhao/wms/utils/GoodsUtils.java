package com.wanhao.wms.utils;

import android.text.TextUtils;

import com.wanhao.wms.bean.EnterOrderDetails;
import com.wanhao.wms.bean.OutGoodsSubParams;
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.PickingOrderDetails;
import com.wanhao.wms.bean.base.IGoodsDecode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public class GoodsUtils {

    public static boolean isSame(EnterOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }

    public static boolean isSame(OutOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }
    public static boolean checkTotal(IGoodsDecode goods, EnterOrderDetails d) {
        return d.getOpQty() >= goods.getPLN_QTY() + d.getNowQty();
    }

    public static boolean isSame(PickingOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }
}
