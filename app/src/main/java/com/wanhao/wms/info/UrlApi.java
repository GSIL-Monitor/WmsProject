package com.wanhao.wms.info;


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/8/18
 * 018200000104
 *
 * @author ql
 */
public class UrlApi {

    public static final String baseUrl = "http://112.64.179.46:8086/sl-wms-app/sl/pt/api/app/v1";

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

}
