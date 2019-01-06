package com.wanhao.wms.bean;

import android.nfc.Tag;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2019/1/4
 *
 * @author ql
 */
public class TopFunction extends LitePalSupport {

    private int id;
    private int tag;
    private int select;//0-未选中 1-选中;

    public static List<TopFunction> getSelectFunctions() {
        List<TopFunction> topFunctions = LitePal.where("select = ?", "1").find(TopFunction.class);
        return topFunctions;
    }

    public static TopFunction getFunctionsByTag(int tag) {
        List<TopFunction> topFunctions = LitePal.where("tag = ?", String.valueOf(tag)).find(TopFunction.class);
        if (topFunctions.size() == 0) {
            return null;
        }
        return topFunctions.get(0);
    }

    public void saveSingle() {
        List<TopFunction> topFunctions = LitePal.where("tag = ?", String.valueOf(tag)).find(TopFunction.class);
        if (topFunctions.size() == 0) {
            save();
            return;
        }
        update(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
