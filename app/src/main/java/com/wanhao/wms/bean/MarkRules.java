package com.wanhao.wms.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/5
 *
 * @author ql
 */
public class MarkRules extends LitePalSupport {
    public static final String DOC_MARK_CODE = "MR1001";//单号
    public static final String RACK_MARK_CODE = "MR2001";//货位
    public static final String GOODS_MARK_CODE = "MR3001";//存货
    public static final String BOX_MARK_CODE = "MR4001";//箱号

    public void saveSingle() {
        List<MarkRules> markRules = LitePal.where("markCode = ?", markCode).find(MarkRules.class);
        if (markRules.size() == 0) {
            save();
            return;
        }
        MarkRules mr = markRules.get(0);
        update(mr.getId());
    }

    public static String code(String mark) {
        List<MarkRules> markRules = LitePal.where("markCode = ?", mark).find(MarkRules.class);
        if (markRules.size() == 0) {
            return null;
        }
        return markRules.get(0).getMarkValue();
    }

    /**
     * {
     * "markCode":"MR1001",
     * "markName":"单号标识",
     * "markValue":"1001"
     * },
     * {
     * "markCode":"MR2001",
     * "markName":"货位标识",
     * "markValue":"2001"
     * },
     * {
     * "markCode":"MR3001",
     * "markName":"存货标识",
     * "markValue":"3001"
     * },
     * {
     * "markCode":"MR4001",
     * "markName":"箱号标识",
     * "markValue":"4001"
     * }
     */

    private int id;
    private String markCode; // 编码规则
    private String markNamee;//规则英文名称
    private String markName;//规则名称
    private String markValue;//标识值

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
    }

    public String getMarkNamee() {
        return markNamee;
    }

    public void setMarkNamee(String markNamee) {
        this.markNamee = markNamee;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getMarkValue() {
        return markValue;
    }

    public void setMarkValue(String markValue) {
        this.markValue = markValue;
    }
}
