package com.wanhao.wms.utils;

import android.text.TextUtils;

import com.wanhao.wms.bean.BoxDetails;
import com.wanhao.wms.bean.EnterOrderDetails;
import com.wanhao.wms.bean.OutGoodsSubParams;
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.PickingOrderDetails;
import com.wanhao.wms.bean.RackDownDetailsBean;
import com.wanhao.wms.bean.TransferOutDetailsBean;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.ui.function.base.IDecode;
import com.wanhao.wms.ui.function.in.CheckQtyOperatePresenter;

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
            if (!TextUtils.isEmpty(goods.getLOT_NO())) {
                return false;
            }
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }

    public static boolean isSame(OutOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            if (!TextUtils.isEmpty(goods.getLOT_NO())) {
                return false;
            }
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }

    public static boolean checkTotal(IGoodsDecode goods, EnterOrderDetails d) {
        return d.getOpQty() >= d.getNowQty() + goods.getPLN_QTY();
    }

    public static boolean checkTotal(IGoodsDecode goods, RackDownDetailsBean d) {
        return d.getOpQty() >= d.getNowQty() + goods.getPLN_QTY();
    }

    public static boolean checkTotal(IGoodsDecode goods, TransferOutDetailsBean d) {
        return d.getOpQty() >= d.getNowQty() + goods.getPLN_QTY();
    }

    public static boolean checkTotal(IGoodsDecode goods, OutOrderDetails d) {
        return d.getOpQty() >= d.getNowQty() + goods.getPLN_QTY();
    }

    public static boolean isSame(PickingOrderDetails d, IGoodsDecode goods) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            if (!TextUtils.isEmpty(goods.getLOT_NO())) {
                return false;
            }
            return d.getSkuCode().equals(goods.getSKU_CODE());
        }
        return d.getSkuCode().equals(goods.getSKU_CODE()) && d.getLotNo().equals(goods.getLOT_NO());
    }

    public static boolean isSame(BoxDetails boxGoods, IGoodsDecode data) {
        if (TextUtils.isEmpty(boxGoods.getLotNo())) {
            if (!TextUtils.isEmpty(data.getLOT_NO())) {
                return false;
            }
            return boxGoods.getSkuCode().equals(data.getSKU_CODE());
        }
        return boxGoods.getSkuCode().equals(data.getSKU_CODE()) && boxGoods.getLotNo().equals(data.getLOT_NO());
    }

    public static boolean isSame(RackDownDetailsBean d, IGoodsDecode data) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            if (!TextUtils.isEmpty(data.getLOT_NO())) {
                return false;
            }
            return d.getSkuCode().equals(data.getSKU_CODE());
        }
        return d.getSkuCode().equals(data.getSKU_CODE()) && d.getLotNo().equals(data.getLOT_NO());

    }

    public static boolean isSame(TransferOutDetailsBean d, IGoodsDecode data) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            if (!TextUtils.isEmpty(data.getLOT_NO())) {
                return false;
            }
            return d.getSkuCode().equals(data.getSKU_CODE());
        }
        return d.getSkuCode().equals(data.getSKU_CODE()) && d.getLotNo().equals(data.getLOT_NO());

    }


    public static boolean isSame(EnterOrderDetails d, EnterOrderDetails data) {
        if (TextUtils.isEmpty(d.getLotNo())) {
            if (!TextUtils.isEmpty(data.getLotNo())) {
                return false;
            }
            return d.getSkuCode().equals(data.getSkuCode());
        }
        return d.getSkuCode().equals(data.getSkuCode()) && d.getLotNo().equals(data.getLotNo());
    }

}
