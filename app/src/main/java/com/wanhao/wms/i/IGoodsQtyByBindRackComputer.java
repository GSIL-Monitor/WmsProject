package com.wanhao.wms.i;

import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.base.IGoodsDecode;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/6
 *
 * @author ql
 */
public interface IGoodsQtyByBindRackComputer {

    void addGoods(IGoods goods);

    void addGoods(IGoods goods, String rack);

    /**
     * 获得还可以添加的数量
     *
     * @return
     */
    double getCanAddQty(IGoods goods, String rack);

    List getGoodsKey(IGoods goods, String rack);

    ComGoods getGoods(IGoods data, String rack);

}
