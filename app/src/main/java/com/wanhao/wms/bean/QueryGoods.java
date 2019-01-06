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
 * 创建时间 2019/1/4
 *
 * @author ql
 */
public class QueryGoods implements IDoc {


    /**
     * whCode : 06005
     * whName : 现场仓-阻尼
     * locCode : 06005-001
     * skuCode : D75-W-00007
     * lotNo :
     * locName : 现场仓-阻尼存货位
     * skuName : ZDMA-S1-R30 K/GZ246810-7320广州电装
     * skuStd :
     * unitName : PCS
     * stockQty : 507
     * opQty : 507
     */

    private String whCode;//仓库编码
    private String whName;//	仓库名称
    private String locCode;//	货位编码
    private String skuCode;//存货编码
    private String lotNo;//批次号
    private String locName;//货位名称
    private String skuName;//存货名称
    private String skuStd;//规格型号
    private String unitName;//	单位名称
    private double stockQty;//	库存数量
    private double opQty;//	可用数量
    private List<Sn> snList;


    private List<ILabel> labels;

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

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getStockQty() {
        return stockQty;
    }

    public void setStockQty(double stockQty) {
        this.stockQty = stockQty;
    }

    public double getOpQty() {
        return opQty;
    }

    public void setOpQty(double opQty) {
        this.opQty = opQty;
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
        labels.add(new LabelBean(R.string.lotNo, getLotNo()));
        labels.add(new LabelBean(R.string.locName, getLocName()));
        labels.add(new LabelBean(R.string.sku_name, getSkuName(),6));
        labels.add(new LabelBean(R.string.skuStd, getSkuStd()));
        labels.add(new LabelBean(R.string.unitName, getUnitName()));
        labels.add(new LabelBean(R.string.wh_qty, String.format("%.4f", stockQty)));
        labels.add(new LabelBean(R.string.use_qty, String.format("%.4f", opQty)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return snList != null && snList.size() > 0;
    }
}
