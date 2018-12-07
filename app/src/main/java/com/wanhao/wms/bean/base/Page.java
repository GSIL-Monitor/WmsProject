package com.wanhao.wms.bean.base;

import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/8/19
 *
 * @author ql
 */
public class Page {
    private int page = 1;
    private int rows = 20;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void put(Map map) {
        map.put("page", page);
        map.put("rows", rows);
    }

    public void add() {
        page++;
    }
}
