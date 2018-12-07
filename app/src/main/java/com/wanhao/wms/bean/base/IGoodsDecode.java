package com.wanhao.wms.bean.base;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public interface IGoodsDecode {
    /**
     * 唯一码
     *
     * @return
     */
    String getKEY_CODE();

    /**
     * 存货编码
     *
     * @return
     */
    String getSKU_CODE();

    /**
     * 批次号
     *
     * @return
     */
    String getLOT_NO();

    /**
     * 序列号
     *
     * @return
     */
    String getSN_NO();

    /**
     * 数量
     *
     * @return
     */
    Long getPLN_QTY();


    /**
     * 生产日期
     *
     * @return
     */
    Long getPRODUCE_DATE();

    /**
     * 失效日期
     */
    Long getOSS_DATE();

    /**
     * 保质期
     */
    Long getEXP_DATE();


    boolean isSerial();
}
