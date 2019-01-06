package com.wanhao.wms.info;


import com.wanhao.wms.bean.Ip;

import java.lang.reflect.Field;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/8/18
 * 018200000104
 *
 * @author ql
 */
public class UrlApi {

    public static void changeIp() {
        Ip lastIp = Ip.getLastIp();
        Class<UrlApi> urlApiClass = UrlApi.class;
        String ip = lastIp.getIp() + ":" + lastIp.getPort();
        Field[] declaredFields = urlApiClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                String o = (String) declaredField.get(urlApiClass);
                String[] split = o.split("/");
                String replace = o.replace(split[2], ip);
                declaredField.set(urlApiClass, replace);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String baseUrl = "http://112.64.179.46:8086/sl-wms-app/sl/pt/api/app/v1";

    public static String login = baseUrl + "/sec/auth/in";//登录
    public static String token = baseUrl + "/sec/token/get";//获得token
    public static String warehouseList = baseUrl + "/base/warehouse/list";//获得仓库列表
    public static String logout = baseUrl + "/sec/auth/out";//退出登录

    public static String markRules = baseUrl + "/sys/markRules/list";//获得标志类型
    public static String decode = baseUrl + "/base/barcode/decode";//解码

    public static String purchaseOrder = baseUrl + "/wms/asn/purchaseOrder/page";//采购入库通知单->未入库
    public static String purchaseOrderDetails = baseUrl + "/wms/asn/purchaseOrder/pageDetail";//采购入库通知单（未入库）明细清单
    public static String purchaseOrderDetails_submit = baseUrl + "/wms/asn/purchaseOrder/execStockIn";//提交入库

    public static String productionOrder = baseUrl + "/wms/asn/productionOrder/page";//生产入库通知单（未入库）清单
    public static String productionOrderDetails = baseUrl + "/wms/asn/productionOrder/pageDetail";//生产入库通知单（未入库）明细清单
    public static String productionOrderDetails_submit = baseUrl + "/wms/asn/productionOrder/execStockIn";//生产入库通知单（未入库）明细清单


    public static String other_enter = baseUrl + "/wms/asn/otherStockin/page";//其他入库
    public static String other_enter_details = baseUrl + "/wms/asn/otherStockin/pageDetail";//其他入库详情
    public static String other_enter_details_submit = baseUrl + "/wms/asn/otherStockin/execStockIn";//其他入库详情

    /**
     * 销售出库
     */
    public static String sales_order = baseUrl + "/wms/so/salesOrder/page";
    public static String sales_order_details = baseUrl + "/wms/so/salesOrder/pageDetail";
    public static String sales_submit = baseUrl + "/wms/so/salesOrder/execStockIn";

    /**
     * 材料出库
     */
    public static String picking_list = baseUrl + "/wms/op/pickingList/page";
    public static String picking_list_details = baseUrl + "/wms/op/pickingList/pageDetail";
    public static String picking_submit = baseUrl + "/wms/op/pickingList/execStockIn";

    /**
     * 其他出库
     */
    public static String other_order_out = baseUrl + "/wms/so/otherStockut/page";
    public static String other_order_details_out = baseUrl + "/wms/so/otherStockut/pageDetail";
    public static String other_order_details_submit_out = baseUrl + "/wms/so/otherStockut/execStockIn";
    /**
     * 材料退库
     */
    public static String materials_cancel_order = baseUrl + "/wms/asn/issueRt/page";
    public static String materials_cancel_order_details = baseUrl + "/wms/asn/issueRt/pageDetail";
    public static String materials_cancel_submit = baseUrl + "/wms/asn/issueRt/execStockIn";

    /**
     * 销售退库
     */
    public static String sales_cancel_order = baseUrl + "/wms/asn/salesOrderRt/page";
    public static String sales_cancel_order_details = baseUrl + "/wms/asn/salesOrderRt/pageDetail";
    public static String sales_cancel_submit = baseUrl + "/wms/asn/salesOrderRt/execStockIn";

    /**
     * 采购退库
     */
    public static String storage_cancel_order = baseUrl + "/wms/so/purchaseOrderRt/page";
    public static String storage_cancel_order_details = baseUrl + "/wms/so/purchaseOrderRt/pageDetail";
    public static String storage_cancel_submit = baseUrl + "/wms/so/purchaseOrderRt/execStockOut";

    /**
     * 装箱
     */
    public static String take_box_order = baseUrl + "/wms/lm/packingList/page";
    public static String take_box_order_new = baseUrl + "/wms/lm/packingList/saveHead";
    public static String take_box_order_details = baseUrl + "/wms/lm/packingList/pageDetail";
    public static String take_box_order_pack_in = baseUrl + "/wms/lm/packingList/execPackingIn";
    public static String take_box_order_pack_out = baseUrl + "/wms/lm/packingList/execPackingOut";

    /**
     * 下架操作
     */
    public static String rack_down_order = baseUrl + "/wms/lm/locAdjust/pageOut";//pageOutDetail
    public static String rack_down_order_details = baseUrl + "/wms/lm/locAdjust/pageOutDetail";
    public static String rack_down_order_submit = baseUrl + "/wms/lm/locAdjust/execStockOut";

    /**
     * 上架架操作
     */
    public static String rack_up_order = baseUrl + "/wms/lm/locAdjust/pageIn";
    public static String rack_up_order_details = baseUrl + "/wms/lm/locAdjust/pageInDetail";
    public static String rack_up_order_submit = baseUrl + "/wms/lm/locAdjust/execStockIn";

    /**
     * 调拨出库
     */
    public static String transfer_out_order = baseUrl + "/wms/lm/transfer/pageOut";
    public static String transfer_out_order_details = baseUrl + "/wms/lm/transfer/pageOutDetail";
    public static String transfer_out_order_submit = baseUrl + "1/wms/lm/transfer/execStockOut";


    /**
     * 调拨入库
     */
    public static String transfer_in_order = baseUrl + "/wms/lm/transfer/pageIn";
    public static String transfer_in_order_details = baseUrl + "/wms/lm/transfer/pageInDetail";
    public static String transfer_in_order_submit = baseUrl + "/wms/lm/transfer/execStockIn";

    /**
     * 盘点
     */
    public static String check_qty_order = baseUrl + "/wms/lm/inventoryCount/pageInvCount";
    public static String check_qty_order_details = baseUrl + "/wms/lm/inventoryCount/listDetail";
    public static String check_qty_order_submit = baseUrl + "/wms/lm/inventoryCount/execSaveData";


    /**
     * 库存查询
     */
    public static String query_goods = baseUrl + "/rpt/stock/stockLoc/list";
}
