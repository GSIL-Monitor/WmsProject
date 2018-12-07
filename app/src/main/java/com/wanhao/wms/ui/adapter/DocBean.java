package com.wanhao.wms.ui.adapter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class DocBean implements IDoc {

    private CharSequence docNoLabel;
    private CharSequence docNo;
    private List<ILabel> tables;


    public DocBean(CharSequence docNoLabel, CharSequence docNo, List<ILabel> tables) {
        this.docNoLabel = docNoLabel;
        this.docNo = docNo;
        this.tables = tables;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return docNoLabel;
    }

    public void setDocNoLabel(CharSequence docNoLabel) {
        this.docNoLabel = docNoLabel;
    }

    @Override
    public CharSequence getDocNo() {
        return docNo;
    }

    public void setDocNo(CharSequence docNo) {
        this.docNo = docNo;
    }

    @Override
    public List<ILabel> getTables() {
        return tables;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }

    public void setTables(List<ILabel> tables) {
        this.tables = tables;
    }
}
