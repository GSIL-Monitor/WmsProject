package com.wanhao.wms.bean;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/17
 *
 * @author ql
 */
public class Sn {
    private Integer porderId;//入库单id
    private String snNo;//序列号

    public Integer getPorderId() {
        return porderId;
    }

    public void setPorderId(Integer porderId) {
        this.porderId = porderId;
    }

    public String getSnNo() {
        return snNo;
    }

    public void setSnNo(String snNo) {
        this.snNo = snNo;
    }

}
