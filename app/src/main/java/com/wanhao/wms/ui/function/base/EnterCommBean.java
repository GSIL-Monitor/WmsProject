package com.wanhao.wms.ui.function.base;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/24
 *
 * @author ql
 */
public class EnterCommBean {

    private int titleRes;
    private String orderUrl;
    private String orderDetailUrl;
    private String submitUrl;

    public EnterCommBean() {
    }

    public EnterCommBean(int titleRes, String orderUrl, String orderDetailUrl, String submitUrl) {
        this.titleRes = titleRes;
        this.orderUrl = orderUrl;
        this.orderDetailUrl = orderDetailUrl;
        this.submitUrl = submitUrl;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getOrderDetailUrl() {
        return orderDetailUrl;
    }

    public void setOrderDetailUrl(String orderDetailUrl) {
        this.orderDetailUrl = orderDetailUrl;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }
}
