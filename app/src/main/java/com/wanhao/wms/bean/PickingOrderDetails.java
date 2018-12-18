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
 * 创建时间 2018/12/18
 *
 * @author ql
 */
public class PickingOrderDetails implements IDoc, Cloneable {
    private Integer id;//拣配单ID
    private String pickCode;//拣配单号
    private Integer pickLineNo;//拣配单行号
    private Integer soDid;//出库单明细ID
    private Integer soLineNo;//出库单号
    private String soCode;
    private String skuCode;//存货编码
    private String skuName;//存货名称
    private String unitCode;
    private String unitName;
    private String whCode;
    private String whName;
    private String locCode;
    private String locName;
    private String lotNo;
    private Float pqty;//	计划出库数量
    private Float outQty;//累计出库数量
    private Float opQty;//	剩余出库数量
    private String serialFlag;

    /************************/
    private float nowQty;

    private List<ILabel> labels;
    private List<Sn> snList;


    public float getNowQty() {
        return nowQty;
    }

    public void setNowQty(float nowQty) {
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

    @Override
    public Object clone() {
        PickingOrderDetails pd = null;
        try {
            pd = (PickingOrderDetails) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return pd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }

    public Integer getPickLineNo() {
        return pickLineNo;
    }

    public void setPickLineNo(Integer pickLineNo) {
        this.pickLineNo = pickLineNo;
    }

    public Integer getSoDid() {
        return soDid;
    }

    public void setSoDid(Integer soDid) {
        this.soDid = soDid;
    }

    public Integer getSoLineNo() {
        return soLineNo;
    }

    public void setSoLineNo(Integer soLineNo) {
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

    public Float getPqty() {
        return pqty;
    }

    public void setPqty(Float pqty) {
        this.pqty = pqty;
    }

    public Float getOutQty() {
        return outQty;
    }

    public void setOutQty(Float outQty) {
        this.outQty = outQty;
    }

    public Float getOpQty() {
        return opQty;
    }

    public void setOpQty(Float opQty) {
        this.opQty = opQty;
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

        labels.add(new LabelBean(MyApp.getContext().getString(R.string.sku_name), skuName, 6));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.lotNo), lotNo));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.unitName), unitName));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.locName), locName));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.plnQty), pqty + ""));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.invQty), outQty + ""));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.nowQty), nowQty + ""));

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
}
