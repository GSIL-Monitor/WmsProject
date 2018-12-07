package com.wanhao.wms.bean.base;

import com.wanhao.wms.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
public class BaseResult {

    private boolean rs;
    private Object data;
    private Long total;
    private String message;


    public Long getTotal() {
        if (total == null) {
            return 0L;
        }
        return total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public boolean isRs() {
        return rs;
    }

    public void setRs(boolean rs) {
        this.rs = rs;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T getData(Class<T> clazz) {
        return DataUtils.getResultObj(data, clazz);
    }

    public <T> ArrayList<T> getList(Class<T> clazz) {
        return DataUtils.getArrayResult(data, clazz);
    }
}
