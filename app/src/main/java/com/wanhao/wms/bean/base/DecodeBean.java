package com.wanhao.wms.bean.base;

import android.text.TextUtils;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public class DecodeBean implements IGoodsDecode {
    private String DOC_TYPE;
    private String DOC_VALUE;
    private String DOC_CODE;
    private String KEY_CODE, SKU_CODE, LOT_NO, SN_NO;
    private Double PLN_QTY;
    private Long PRODUCE_DATE, OSS_DATE, EXP_DATE;


    private String targetRack;

    public void setKEY_CODE(String KEY_CODE) {
        this.KEY_CODE = KEY_CODE;
    }

    public void setSKU_CODE(String SKU_CODE) {
        this.SKU_CODE = SKU_CODE;
    }

    public void setLOT_NO(String LOT_NO) {
        this.LOT_NO = LOT_NO;
    }

    public void setSN_NO(String SN_NO) {
        this.SN_NO = SN_NO;
    }

    public void setPLN_QTY(Double PLN_QTY) {
        this.PLN_QTY = PLN_QTY;
    }

    public void setPRODUCE_DATE(Long PRODUCE_DATE) {
        this.PRODUCE_DATE = PRODUCE_DATE;
    }

    public void setOSS_DATE(Long OSS_DATE) {
        this.OSS_DATE = OSS_DATE;
    }

    public void setEXP_DATE(Long EXP_DATE) {
        this.EXP_DATE = EXP_DATE;
    }

    public String getTargetRack() {
        return targetRack;
    }

    public void setTargetRack(String targetRack) {
        this.targetRack = targetRack;
    }

    public String getDOC_TYPE() {
        return DOC_TYPE;
    }

    public void setDOC_TYPE(String DOC_TYPE) {
        this.DOC_TYPE = DOC_TYPE;
    }

    public String getDOC_VALUE() {
        return DOC_VALUE;
    }

    public void setDOC_VALUE(String DOC_VALUE) {
        this.DOC_VALUE = DOC_VALUE;
    }

    public String getDOC_CODE() {
        return DOC_CODE;
    }

    public void setDOC_CODE(String DOC_CODE) {
        this.DOC_CODE = DOC_CODE;
    }

    @Override
    public String getKEY_CODE() {
        return KEY_CODE;
    }

    @Override
    public String getSKU_CODE() {
        return SKU_CODE;
    }

    @Override
    public String getLOT_NO() {
        return LOT_NO;
    }

    @Override
    public String getSN_NO() {
        return SN_NO;
    }

    @Override
    public Double getPLN_QTY() {
        if (PLN_QTY == null) {
            return 1d;
        }
        return PLN_QTY;
    }

    @Override
    public Long getPRODUCE_DATE() {
        return PRODUCE_DATE;
    }

    @Override
    public Long getOSS_DATE() {
        return OSS_DATE;
    }

    @Override
    public Long getEXP_DATE() {
        return EXP_DATE;
    }

    @Override
    public boolean isSerial() {
        return !TextUtils.isEmpty(SN_NO);
    }

    @Override
    public String getGoodsSkuCode() {
        return SKU_CODE;
    }

    @Override
    public String getGoodsLotNo() {
        return LOT_NO;
    }

    @Override
    public String getGoodsSn() {
        return SN_NO;
    }

    @Override
    public boolean isGoodsSn() {
        return isSerial();
    }

    @Override
    public double getGoodsQty() {
        return PLN_QTY;
    }

    @Override
    public String getSaveRack() {
        return getTargetRack();
    }
}
