package com.wanhao.wms.bean;

import com.wanhao.wms.i.IGoods;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/6
 *
 * @author ql
 */
public class ComGoods {
    private IGoods goods;
    private List listKey;
    private double total;

    private double nowQty;

    public IGoods getGoods() {
        return goods;
    }

    public void setGoods(IGoods goods) {
        this.goods = goods;
    }

    public List getListKey() {
        return listKey;
    }

    public void setListKey(List listKey) {
        this.listKey = listKey;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getNowQty() {
        return nowQty;
    }

    public void setNowQty(double nowQty) {
        this.nowQty = nowQty;
    }
}
