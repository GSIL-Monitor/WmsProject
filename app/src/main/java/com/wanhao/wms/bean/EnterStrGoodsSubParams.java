package com.wanhao.wms.bean;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
public class EnterStrGoodsSubParams {

    private Long id;//订单明细ID
    private Integer asnLineNo;//订单明细行号

    private String asnCode;//订单编号

    private String locCode;//货位编码
    private String skuCode;//存货编码

    private String lotNo;//批次号
    private Long plnQty;//数量
    private List<Sn> snList;//序列号集合

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAsnLineNo() {
        return asnLineNo;
    }

    public void setAsnLineNo(Integer asnLineNo) {
        this.asnLineNo = asnLineNo;
    }

    public String getAsnCode() {
        return asnCode;
    }

    public void setAsnCode(String asnCode) {
        this.asnCode = asnCode;
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

    public Long getPlnQty() {
        return plnQty;
    }

    public void setPlnQty(Long plnQty) {
        this.plnQty = plnQty;
    }

    public List<Sn> getSnList() {
        return snList;
    }

    public void setSnList(List<Sn> snList) {
        this.snList = snList;
    }


}
