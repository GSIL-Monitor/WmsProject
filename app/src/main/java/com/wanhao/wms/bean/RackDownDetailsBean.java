package com.wanhao.wms.bean;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/25
 *
 * @author ql
 */
public class RackDownDetailsBean implements IDoc, Cloneable  {

    /**
     * id : 20011
     * lineNo : 1
     * adjustCode : LA201812240001
     * skuCode : 6-70818
     * skuName : 瓷管/23×660
     * skuStd :
     * unitCode : J001001
     * unitName : PCS
     * locCode : 0002
     * locName : 现场仓-零部件货位2
     * plnQty : 30
     * opQty : 30
     * serialFlag : N
     */

    private int id;
    private int lineNo;
    private String adjustCode;//单据编号
    private String skuCode;
    private String skuName;
    private String skuStd;
    private String unitCode;
    private String unitName;
    private String locCode;
    private String locName;
    private double plnQty;//计划数量
    private double opQty;//可下架数量
    private String serialFlag;
    private String lotNo;
    private double transOutQty;//累计下架数量

    private double nowQty;
    private List<ILabel> labels;
    private List<Sn> snList;


    @Override
    public Object clone() {
        EnterOrderDetails pd = null;
        try {
            pd = (EnterOrderDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return pd;
    }


    public List<Sn> getSnList() {
        return snList;
    }

    public void setSnList(List<Sn> snList) {
        this.snList = snList;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public double getTransOutQty() {
        return transOutQty;
    }

    public void setTransOutQty(double transOutQty) {
        this.transOutQty = transOutQty;
    }

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public double getPlnQty() {
        return plnQty;
    }

    public double getOpQty() {
        return opQty;
    }

    public void setOpQty(double opQty) {
        this.opQty = opQty;
    }

    public double getNowQty() {
        return nowQty;
    }

    public void setNowQty(double nowQty) {
        this.nowQty = nowQty;
    }

    public void setPlnQty(double plnQty) {
        this.plnQty = plnQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getAdjustCode() {
        return adjustCode;
    }

    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuStd() {
        return skuStd;
    }

    public void setSkuStd(String skuStd) {
        this.skuStd = skuStd;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }


    public String getSerialFlag() {
        return serialFlag;
    }

    public void setSerialFlag(String serialFlag) {
        this.serialFlag = serialFlag;
    }


    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getInstance().getString(R.string.label_sku_code);
    }

    @Override
    public CharSequence getDocNo() {
        return skuCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }

        labels = new ArrayList<>();

        labels.add(new LabelBean(R.string.sku_name, skuName, 6));
        labels.add(new LabelBean(R.string.lotNo, lotNo));
        labels.add(new LabelBean(R.string.unitName, unitName));
        labels.add(new LabelBean(R.string.locName, locName));
        labels.add(new LabelBean(R.string.plnQty, String.format("%.3f", plnQty)));
        labels.add(new LabelBean(R.string.invQty, String.format("%.3f", transOutQty)));
        labels.add(new LabelBean(R.string.nowQty, String.format("%.3f", nowQty)));

        return labels;
    }

    /**
     * 是否是序列号管理
     *
     * @return
     */
    public boolean isSerial() {
        return "Y".equals(serialFlag);
    }

    @Override
    public boolean isShowSn() {
        return isSerial();
    }

    public boolean isAutoSn() {
        return "0".equals(serialFlag) || "1".equals(serialFlag);
    }
}
