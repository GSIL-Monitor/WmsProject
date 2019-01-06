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
 * 创建时间 2018/12/28
 *
 * @author ql
 */
public class CheckQtyDetails implements IDoc {

    /**
     * id : 20199
     * whCode : 100
     * locCode : H03
     * skuCode : test001
     * lotNo : LOT20181201000001
     * accQty : 10
     * firstQty : 0
     * actQty : 0
     * diffQty : 0
     * whName : 花卉仓库
     * locName : 华为03
     */

    private int id;
    private String whCode;//仓库编号
    private String locCode;//货位编号
    private String skuCode;//货品编号
    private String skuClassCode;//货品大类编码
    private String lotNo;//批次号
    private double accQty;//账面数量
    private double firstQty;//初盘数量
    private double actQty;//实盘数量
    private double diffQty;//差异数量
    private String whName;//仓库名称
    private String locName;//货位名称
    private String skuName;//货品名称
    private String clsName;//货品大类名称
    private String batchFlag;//批次管控标志位（Y:是，N：否）
    private String batchNoFlag;//批次号获取方式（0：自动生成；1：手工录入；2：外部采集）

    private String serialFlag;//序列号管控标志位（Y:是，N：否）

    private String serialNoFlag;//序列号获取方式（0：自动生成；1：手工录入；2：外部采集）

    private List<ILabel> labels;

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public String getSkuClassCode() {
        return skuClassCode;
    }

    public void setSkuClassCode(String skuClassCode) {
        this.skuClassCode = skuClassCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }

    public String getBatchNoFlag() {
        return batchNoFlag;
    }

    public void setBatchNoFlag(String batchNoFlag) {
        this.batchNoFlag = batchNoFlag;
    }

    public String getSerialFlag() {
        return serialFlag;
    }

    public void setSerialFlag(String serialFlag) {
        this.serialFlag = serialFlag;
    }

    public String getSerialNoFlag() {
        return serialNoFlag;
    }

    public void setSerialNoFlag(String serialNoFlag) {
        this.serialNoFlag = serialNoFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public double getAccQty() {
        return accQty;
    }

    public void setAccQty(double accQty) {
        this.accQty = accQty;
    }

    public double getFirstQty() {
        return firstQty;
    }

    public void setFirstQty(double firstQty) {
        this.firstQty = firstQty;
    }

    public double getActQty() {
        return actQty;
    }

    public void setActQty(double actQty) {
        this.actQty = actQty;
    }

    public double getDiffQty() {
        return diffQty;
    }

    public void setDiffQty(double diffQty) {
        this.diffQty = diffQty;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_sku_code);
    }

    @Override
    public CharSequence getDocNo() {
        return skuCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels !=null) {
            return labels;
        }
        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.lotNo,lotNo));
        labels.add(new LabelBean(R.string.locName,locName));
        labels.add(new LabelBean(R.string.sku_name,getSkuName(),6));
        labels.add(new LabelBean(R.string.acc_qty,String.valueOf(accQty)));
        labels.add(new LabelBean(R.string.first_qty,String.valueOf(firstQty)));
        labels.add(new LabelBean(R.string.act_qty,String.valueOf(actQty)));
        labels.add(new LabelBean(R.string.diff_qty,String.valueOf(diffQty)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
