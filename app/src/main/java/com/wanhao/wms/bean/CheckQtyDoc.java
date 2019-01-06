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
 * 创建时间 2018/12/28
 *
 * @author ql
 */
public class CheckQtyDoc implements IDoc {


    /**
     * id : 10027
     * inventoryCode : IC201812230013
     * inventoryDate : 1545494400000
     */

    private int id;
    private String inventoryCode;
    private long inventoryDate;

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

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public long getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(long inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_doc);
    }

    @Override
    public CharSequence getDocNo() {
        return inventoryCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }

        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.date, DateUtils.getStringDate(inventoryDate)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
