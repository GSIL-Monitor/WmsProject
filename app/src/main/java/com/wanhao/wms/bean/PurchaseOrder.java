package com.wanhao.wms.bean;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;
import com.wanhao.wms.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：采购入库 任务单
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/5
 *
 * @author ql
 */
public class PurchaseOrder implements IDoc {

    /**
     * id : 598
     * asnCode : PO201812010022
     * asnDate : 1543507200000
     * supCode : 1000060
     * supName : 苏州市爱业电子元件厂
     */

    private int id;//订单号
    private String asnCode;//订单编号
    private Long asnDate;//单据日期
    private String supCode;//供应商编码
    private String supName;//供应商名称
    private String recCode;//采购订单号

    private List<ILabel> labels;

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public String getRecCode() {
        return recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsnCode() {
        return asnCode;
    }

    public void setAsnCode(String asnCode) {
        this.asnCode = asnCode;
    }

    public Long getAsnDate() {
        return asnDate;
    }

    public void setAsnDate(Long asnDate) {
        this.asnDate = asnDate;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }


    @Override
    public CharSequence getDocNoLabel() {
        return "订单号";
    }

    @Override
    public CharSequence getDocNo() {
        return id + "";
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }
        labels = new ArrayList<>();
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.recCode), recCode));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.supName), supName));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.date), DateUtils.getStringDate(asnDate)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
