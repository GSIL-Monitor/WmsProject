package com.wanhao.wms.utils;

import android.text.TextUtils;

import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.i.IGoods;
import com.wanhao.wms.i.IGoodsQtyComputer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：用于货品倒冲
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/6
 *
 * @author ql
 */
public class CommGoodsComputer implements IGoodsQtyComputer {


    private Map<String, ComGoods> mQtyMap = new HashMap<>();//相同货品的数量

    private boolean keyBindSn = false;
    private boolean bindLotNo = true;

    public void setSrcList(List list) {
        if (list == null) {
            return;
        }
        init(list);
    }


    public boolean isKeyBindSn() {
        return keyBindSn;
    }

    public void setKeyBindSn(boolean keyBindSn) {
        this.keyBindSn = keyBindSn;
    }

    private void init(List list) {
        mQtyMap.clear();
        for (Object o : list) {
            IGoods ig = (IGoods) o;
            String key = getKey(ig);
            ComGoods comGoods = mQtyMap.get(key);
            if (comGoods == null) {
                comGoods = new ComGoods();
                mQtyMap.put(key, comGoods);
            }

            comGoods.setGoods(ig);
            List listKey = comGoods.getListKey();
            if (listKey == null) {
                listKey = new ArrayList();
                comGoods.setListKey(listKey);
            }
            listKey.add(ig);
            double comQty = comGoods.getTotal() + ig.getGoodsQty();
            comGoods.setTotal(comQty);
        }
    }


    private String getKey(IGoods goods) {
        String lotNo = TextUtils.isEmpty(goods.getGoodsLotNo()) ? "-" : goods.getGoodsLotNo();
        String skuCode = goods.getGoodsSkuCode();
        String sn = TextUtils.isEmpty(goods.getGoodsSn()) ? "-" : goods.getGoodsSn();
        lotNo = bindLotNo ? lotNo : "-";
        sn = keyBindSn ? sn : "-";

        return String.format("%s-%s-%s", skuCode, lotNo, sn);


    }

    public boolean isBindLotNo() {
        return bindLotNo;
    }

    public void setBindLotNo(boolean bindLotNo) {
        this.bindLotNo = bindLotNo;
    }

    @Override
    public void addGoods(IGoods goods) {
        String key = getKey(goods);
        ComGoods comGoods = mQtyMap.get(key);
        double addQty = comGoods.getNowQty() + goods.getGoodsQty();
        comGoods.setNowQty(addQty);
    }


    @Override
    public double getCanAddQty(IGoods goods) {
        ComGoods comGoods = mQtyMap.get(getKey(goods));
        return comGoods.getTotal() - comGoods.getNowQty();
    }

    @Override
    public List getGoodsKey(IGoods goods) {
        String key = getKey(goods);
        ComGoods comGoods = mQtyMap.get(key);
        return comGoods.getListKey();
    }

    @Override
    public ComGoods getGoods(IGoods data) {
        String key = getKey(data);
        return mQtyMap.get(key);
    }

    @Override
    public void setNowQty(IGoods data, double i) {
        getGoods(data).setNowQty(i);
    }


}
