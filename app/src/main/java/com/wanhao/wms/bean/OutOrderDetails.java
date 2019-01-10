package com.wanhao.wms.bean;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.i.IGoods;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/17
 *
 * @author ql
 */
public class OutOrderDetails implements IDoc, Cloneable, IGoods {


    /**
     * id : 173
     * soLineNo : 1
     * soCode : OSO201812140001
     * skuCode : 0001X035B140
     * skuName : 电阻丝/6J40/0.35*0.1
     * skuStd :
     * unitCode : J002002
     * unitName : KG
     * whCode : 06005
     * whName : 现场仓-阻尼
     * locCode : 06005-001
     * locName : 现场仓-阻尼存货位
     * lotNo :
     * plnQty : 1
     * serialFlag : N
     */

    private int id;//销售出库明细ID
    private int soLineNo;//销售出库明细行号
    private String soCode;//	销售出库单号
    private String skuCode;//	存货编码
    private String skuName;//存货名称
    private String skuStd;//规格型号
    private String unitCode;//单位编码
    private String unitName;//单位名称
    private String whCode;//仓库编码
    private String whName;//仓库名称
    private String locCode;//货位编码
    private String locName;//货位名称
    private String lotNo;//批次号
    private double plnQty;//计划出库数量
    private double outQty;//累计出库数量
    private double opQty;//剩余出库数量
    private String serialFlag;//序列号管控

    private String serialNoFlag;//序列号获取方式（0：自动生成；1：手工录入；2：外部采集）

    /*****************************************/

    private double nowQty;
    private String targetRack;

    private List<ILabel> labels;
    private List<Sn> snList;
    private double totalQty;

    public String getTargetRack() {
        return targetRack;
    }

    public void setTargetRack(String targetRack) {
        this.targetRack = targetRack;
    }

    public double getNowQty() {
        return nowQty;
    }

    public void setNowQty(double nowQty) {
        this.nowQty = nowQty;
    }

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public List<Sn> getSnList() {
        return snList;
    }

    public void setSnList(List<Sn> snList) {
        this.snList = snList;
    }

    /*****************************************/
    /**
     * 是否是序列号管理
     *
     * @return
     */
    public boolean isSerial() {
        return "Y".equals(serialFlag);
    }


    public double getOutQty() {
        return outQty;
    }

    public void setOutQty(double outQty) {
        this.outQty = outQty;
    }

    public double getOpQty() {
        return opQty;
    }

    public void setOpQty(double opQty) {
        this.opQty = opQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoLineNo() {
        return soLineNo;
    }

    public void setSoLineNo(int soLineNo) {
        this.soLineNo = soLineNo;
    }

    public String getSoCode() {
        return soCode;
    }

    public void setSoCode(String soCode) {
        this.soCode = soCode;
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

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public double getPlnQty() {
        return plnQty;
    }

    public void setPlnQty(int plnQty) {
        this.plnQty = plnQty;
    }

    public String getSerialFlag() {
        return serialFlag;
    }

    public void setSerialFlag(String serialFlag) {
        this.serialFlag = serialFlag;
    }

    @Override
    public Object clone() {
        OutOrderDetails pd = null;
        try {
            pd = (OutOrderDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return pd;
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

        labels.add(new LabelBean(MyApp.getContext().getString(R.string.sku_name), skuName, 6));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.lotNo), lotNo));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.unitName), unitName));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.skuStd), skuStd));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.locName), locCode));
        if (totalQty >0) {
            labels.add(new LabelBean(MyApp.getContext().getString(R.string.total), String.format("%.4f", totalQty)));
        }else {
            labels.add(new LabelBean(MyApp.getContext().getString(R.string.plnQty), String.format("%.4f", plnQty)));

            labels.add(new LabelBean(MyApp.getContext().getString(R.string.invQty), String.format("%.4f", outQty)));
        }
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.nowQty), String.format("%.4f", nowQty)));

        return labels;
    }

    @Override
    public boolean isShowSn() {
        return isSerial();
    }

    public boolean isAutoSn() {
        return "0".equals(serialNoFlag) || "1".equals(serialNoFlag);
    }

    @Override
    public String getGoodsSkuCode() {
        return skuCode;
    }

    @Override
    public String getGoodsLotNo() {
        return lotNo;
    }

    @Override
    public String getGoodsSn() {
        return null;
    }

    @Override
    public boolean isGoodsSn() {
        return false;
    }

    @Override
    public double getGoodsQty() {
        return opQty;
    }

    @Override
    public String getSaveRack() {
        return locCode;
    }

    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }
}
