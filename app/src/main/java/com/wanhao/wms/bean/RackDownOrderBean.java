package com.wanhao.wms.bean;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;
import com.wanhao.wms.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/25
 *
 * @author ql
 */
public class RackDownOrderBean implements IDoc {


    /**
     * id : 10005
     * adjustCode : LA201812240001
     * adjustDate : 1545580800000
     */

    private int id;
    private String adjustCode;//单据编号
    private long adjustDate;//单据日期

    private List<ILabel> labels;

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdjustCode() {
        return adjustCode;
    }

    public void setAdjustCode(String adjustCode) {
        this.adjustCode = adjustCode;
    }

    public long getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(long adjustDate) {
        this.adjustDate = adjustDate;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_doc);
    }

    @Override
    public CharSequence getDocNo() {
        return adjustCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }
        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.date, DateUtils.getStringDate(adjustDate)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
