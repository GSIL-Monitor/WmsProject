package com.wanhao.wms.utils;

import android.text.TextUtils;

import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.i.IGoods;
import com.wanhao.wms.i.IGoodsQtyByBindRackComputer;
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
public class CommGoodsBindRackComputer implements IGoodsQtyByBindRackComputer {


    private Map<String, ComGoods> mQtyMap = new HashMap<>();//相同货品的数量
    private boolean bindLotNo = true;
    private boolean bindSn = false;

    public void setSrcList(List list) {
        if (list == null) {
            return;
        }
        init(list);
    }

    private void init(List list) {
        mQtyMap.clear();
        for (Object o : list) {
            IGoods ig = (IGoods) o;
            String key = getKey(ig, ig.getSaveRack());
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


    private String getKey(IGoods goods, String rackNo) {
        String lotNo = TextUtils.isEmpty(goods.getGoodsLotNo()) ? "-" : goods.getGoodsLotNo();
        String skuCode = goods.getGoodsSkuCode();
        String rn = TextUtils.isEmpty(rackNo) ? "-" : rackNo;
        String sn = TextUtils.isEmpty(goods.getGoodsSn()) ? "-" : goods.getGoodsSn();
        lotNo = bindLotNo ? lotNo : "-";
        sn = bindSn ? sn : "-";

        return String.format("%s-%s-%s-%s", skuCode, lotNo, rn, sn);
    }

    @Override
    public void addGoods(IGoods goods, String rack) {
        String key = getKey(goods, rack);
        ComGoods comGoods = mQtyMap.get(key);
        double addQty = comGoods.getNowQty() + goods.getGoodsQty();
        comGoods.setNowQty(addQty);
    }


    @Override
    public double getCanAddQty(IGoods goods, String rack) {
        ComGoods comGoods = mQtyMap.get(getKey(goods, rack));
        return comGoods.getTotal() - comGoods.getNowQty();
    }


    @Override
    public List getGoodsKey(IGoods goods, String rack) {
        String key = getKey(goods, rack);
        ComGoods comGoods = mQtyMap.get(key);
        return comGoods.getListKey();
    }

    @Override
    public ComGoods getGoods(IGoods data, String mRackCode) {
        String key = getKey(data, mRackCode);
        return mQtyMap.get(key);
    }


    public Map<String, ComGoods> getmQtyMap() {
        return mQtyMap;
    }

    public void setmQtyMap(Map<String, ComGoods> mQtyMap) {
        this.mQtyMap = mQtyMap;
    }

    public boolean isBindLotNo() {
        return bindLotNo;
    }

    public void setBindLotNo(boolean bindLotNo) {
        this.bindLotNo = bindLotNo;
    }

    public boolean isBindSn() {
        return bindSn;
    }

    public void setBindSn(boolean bindSn) {
        this.bindSn = bindSn;
    }

}
