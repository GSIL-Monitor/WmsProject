package com.wanhao.wms.ui;

import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.GridBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/4
 *
 * @author ql
 */
public class FunctionsHelper {
    public static final int f_receive = 6;//收货
    public static final int F_enter_storage = 0;//采购入库
    public static final int f_enter_production = 1;//生产入库
    public static final int f_enter_other = 2;//其他入库
    public static final int f_exit_sales = 3;//销售出库
    public static final int f_exit_other = 4;//其他出库
    public static final int f_exit_material = 5;//材料出库
    public static final int f_cancel_material = 30;//材料退库
    public static final int f_cancel_sales = 31;//采购退库
    public static final int f_cancel_storage = 32;//采购退库


    public static final int f_picking_box = 41;//装箱
    public static final int f_rack_down = 42;//货位下架
    public static final int f_rack_up = 43;//货位下架
    public static final int f_transfer_out = 44;//调拨出库
    public static final int f_transfer_in = 45;//调拨入库
    public static final int f_check_qty = 46;//盘点
    public static final int f_query_goods = 47;//库存查询
    private List<GridBean> allFunctions = new ArrayList<>();

    public FunctionsHelper() {
        a();
    }

    private void a() {
        allFunctions.add(new GridBean(R.string.enter_storage, F_enter_storage, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.production, f_enter_production, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.f_receive, f_receive, R.drawable.icon_nav_enter));


        allFunctions.add(new GridBean(R.string.pick_box, f_picking_box, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.rack_down, f_rack_down, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.rack_up, f_rack_up, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.transfer_out, f_transfer_out, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.transfer_in, f_transfer_in, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.check_qty, f_check_qty, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.query_goods, f_query_goods, R.drawable.icon_nav_enter));


        allFunctions.add(new GridBean(R.string.enter_storage, F_enter_storage, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.production, f_enter_production, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.other_enter, f_enter_other, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.marketing_exit, f_exit_sales, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.material_exit, f_exit_material, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.other_exit, f_exit_other, R.drawable.icon_nav_enter));


        allFunctions.add(new GridBean(R.string.material_cancel, f_cancel_material, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.sales_cancel, f_cancel_sales, R.drawable.icon_nav_enter));
        allFunctions.add(new GridBean(R.string.storage_cancel, f_cancel_storage, R.drawable.icon_nav_enter));
    }

    public List<GridBean> getType(int... type) {
        List<GridBean> list = new ArrayList<>();
        int[] types = type;
        OK:
        for (GridBean allFunction : allFunctions) {
            if (types.length == list.size()) {
                break;
            }
            for (int i : types) {
                if (allFunction.getTag() == i) {
                    list.add(allFunction);
                    continue OK;
                }
            }
        }

        return list;
    }

    public List<GridBean> getAllFunctions() {
        return allFunctions;
    }
}
