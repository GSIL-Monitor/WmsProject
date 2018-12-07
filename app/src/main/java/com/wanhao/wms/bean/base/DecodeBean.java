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
    private String KEY_CODE,SKU_CODE,LOT_NO,SN_NO;
    private Long PLN_QTY,PRODUCE_DATE,OSS_DATE,EXP_DATE;


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
    public Long getPLN_QTY() {
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
}
