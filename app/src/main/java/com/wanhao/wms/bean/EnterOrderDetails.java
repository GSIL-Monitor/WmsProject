package com.wanhao.wms.bean;

import android.text.TextUtils;

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
 * 创建时间 2018/12/5
 *
 * @author ql
 */
public class EnterOrderDetails implements IDoc, Cloneable, IGoods {


    /**
     * id : 1251
     * asnLineNo : 1
     * asnCode : PO201812010022
     * skuCode : test003
     * skuName : 序列号测试3
     * skuStd :
     * unitCode : J005001
     * unitName : 支
     * whCode : 001
     * whName : 001
     * locCode : 001
     * locName : 001
     * lotNo : LOT20181201000001
     * plnQty : 5
     * invQty : 0
     * opQty : 5
     * serialFlag : Y
     * serialNoFlag : 0
     */

    private int id;
    private int asnLineNo;//采购入库明行号
    private String asnCode;//采购入库单号
    private String skuCode;//存货编码
    private String skuName;//存货名称
    private String skuStd;//规格型号
    private String unitCode;//单位编码
    private String unitName;//单位名称
    private String whCode;//仓库编码
    private String whName;//仓库名称
    private String locCode;//计划货位编码
    private String locName;//	计划货位名称
    private String lotNo;//批次号
    private double plnQty;//	数量
    private double invQty;//累计入库数量
    private double opQty;//	剩余入库数量
    private String serialFlag;//序列号管控标识，Y表示序列号管控，N表示不是序列号管控
    private String serialNoFlag;//序列号获取方式（0：自动生成；1：手工录入；2：外部采集）


    /************************/
    private double nowQty;

    private List<ILabel> labels;
    private List<Sn> snList;
    private String targetRack;//存入的目标货位
    private double totalQty;


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

    public String getTargetRack() {
        return targetRack;
    }

    public void setTargetRack(String targetRack) {
        this.targetRack = targetRack;
    }


    /**
     * 是否是序列号管理
     *
     * @return
     */
    public boolean isSerial() {
        return "Y".equals(serialFlag);
    }

    public boolean isAutoSerial() {
        return "0".equals(serialNoFlag) || "1".equals(serialNoFlag);
    }

    public List<Sn> getSnList() {
        return snList;
    }

    public void setSnList(List<Sn> snList) {
        this.snList = snList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAsnLineNo() {
        return asnLineNo;
    }

    public void setAsnLineNo(int asnLineNo) {
        this.asnLineNo = asnLineNo;
    }

    public String getAsnCode() {
        return asnCode;
    }

    public void setAsnCode(String asnCode) {
        this.asnCode = asnCode;
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

    public void setPlnQty(double plnQty) {
        this.plnQty = plnQty;
    }

    public double getInvQty() {
        return invQty;
    }

    public void setInvQty(double invQty) {
        this.invQty = invQty;
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

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
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
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.locName), locName));
        if (totalQty > 0) {
            labels.add(new LabelBean(R.string.total,totalQty+""));
        } else {
            labels.add(new LabelBean(MyApp.getContext().getString(R.string.plnQty), plnQty + ""));
            labels.add(new LabelBean(MyApp.getContext().getString(R.string.invQty), invQty + ""));
        }

        labels.add(new LabelBean(MyApp.getContext().getString(R.string.nowQty), nowQty + ""));
        if (!TextUtils.isEmpty(targetRack)) {
            labels.add(new LabelBean(MyApp.getContext().getString(R.string.target_rack), targetRack));

        }
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return isSerial();
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
        return isSerial();
    }

    @Override
    public double getGoodsQty() {
        return opQty;
    }

    @Override
    public String getSaveRack() {
        return null;
    }

    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }
}
