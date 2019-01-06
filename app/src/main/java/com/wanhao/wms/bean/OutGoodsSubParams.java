package com.wanhao.wms.bean;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/17
 *
 * @author ql
 */
public class OutGoodsSubParams {

    private Integer id;//拣配单ID
    private String pickCode;//拣配单号
    private String pickLineNo;//拣配单行号
    private Integer soDId;//	出库单明细ID
    private Integer soLineNo;//出库单明细行号
    private String soCode;//出库单号
    private String docType;//单据类型
    private String skuCode;//存货编码
    private String lotNo;//批次号
    private double plnQty;//数量
    private String locCode;//货位编码;

    private double pQty;//材料出库使用;

    private List<Sn> snList;

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public double getpQty() {
        return pQty;
    }

    public void setpQty(double pQty) {
        this.pQty = pQty;
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

    public String getPickLineNo() {
        return pickLineNo;
    }

    public void setPickLineNo(String pickLineNo) {
        this.pickLineNo = pickLineNo;
    }

    public Integer getSoDId() {
        return soDId;
    }

    public void setSoDId(Integer soDId) {
        this.soDId = soDId;
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

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

    public double getPlnQty() {
        return plnQty;
    }

    public void setPlnQty(double plnQty) {
        this.plnQty = plnQty;
    }

    public List<Sn> getSnList() {
        return snList;
    }

    public void setSnList(List<Sn> snList) {
        this.snList = snList;
    }
}
