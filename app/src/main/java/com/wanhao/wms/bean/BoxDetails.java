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
 * 创建时间 2018/12/24
 *
 * @author ql
 */
public class BoxDetails implements IDoc, IGoods,Cloneable {


    /**
     * id : 36
     * plCode : PL201812180001
     * skuCode : 27-70168
     * skuName : 螺钉/十字槽盘头自攻螺钉/ST4.8x10/镀白锌/GB_T 845-85/割尾
     * skuStd :
     * unitCode : J001001
     * unitName : PCS
     * locCode : 06005-001
     * locName : 现场仓-阻尼存货位
     * lotNo :
     * plnQty : 10
     */

    private int id;//装箱明细ID
    private String plCode;//装箱单号
    private String skuCode;//存货编码
    private String skuName;//存货名称
    private String skuStd;//存货规格
    private String unitCode;//单位编码
    private String unitName;//单位名称
    private String locCode;//货位编码
    private String locName;//货位名称
    private String lotNo;//批次号
    private double plnQty;//装箱数量
    private String snNo;//序列号


    private List<ILabel> labels;
    private boolean isOperate;
    private double nowQty;
    private String targetRack;

    @Override
    public Object clone() {
        BoxDetails pd = null;
        try {
            pd = (BoxDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return pd;
    }
    public void setTargetRack(String targetRack) {
        this.targetRack = targetRack;
    }

    public String getSnNo() {
        return snNo;
    }

    public void setSnNo(String snNo) {
        this.snNo = snNo;
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

    public boolean isOperate() {
        return isOperate;
    }

    public void setOperate(boolean operate) {
        isOperate = operate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlCode() {
        return plCode;
    }

    public void setPlCode(String plCode) {
        this.plCode = plCode;
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
        if (labels != null) {
            return labels;
        }

        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.sku_name, skuName, 6));
        labels.add(new LabelBean(R.string.lotNo, lotNo));
        labels.add(new LabelBean(R.string.unitName, unitName));
        labels.add(new LabelBean(R.string.plnQty, plnQty + ""));
        if (isOperate) {
            labels.add(new LabelBean(R.string.nowQty, nowQty + ""));
        }
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
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
        return snNo;
    }

    @Override
    public boolean isGoodsSn() {
        return false;
    }

    @Override
    public double getGoodsQty() {
        return plnQty;
    }

    @Override
    public String getSaveRack() {
        return null;
    }

    public String getTargetRack() {
        return targetRack;
    }

    public boolean isSerial() {
        return !TextUtils.isEmpty(snNo);
    }
}
