package com.wanhao.wms.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/10
 *
 * @author ql
 */
public class Ip extends LitePalSupport {
    private int id;
    private String ip;
    private Integer port;

    public static Ip getLastIp() {
        Ip last = LitePal.findLast(Ip.class);
        return last;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
