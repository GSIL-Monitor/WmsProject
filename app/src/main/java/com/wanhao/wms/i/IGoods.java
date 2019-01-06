package com.wanhao.wms.i;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/6
 *
 * @author ql
 */
public interface IGoods {


    String getGoodsSkuCode();

    String getGoodsLotNo();

    String getGoodsSn();

    boolean isGoodsSn();

    double getGoodsQty();

    String getSaveRack();

}
