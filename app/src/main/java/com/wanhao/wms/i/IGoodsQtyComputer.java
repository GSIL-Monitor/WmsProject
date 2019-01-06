package com.wanhao.wms.i;

import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.EnterOrderDetails;
import com.wanhao.wms.bean.base.IGoodsDecode;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/6
 *
 * @author ql
 */
public interface IGoodsQtyComputer {

    void addGoods(IGoods goods);

    /**
     * 获得还可以添加的数量
     *
     * @return
     */
    double getCanAddQty(IGoods goods);

    List getGoodsKey(IGoods goods);

    ComGoods getGoods(IGoods data);

    void setNowQty(IGoods data, double qty);
}
