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
public class TransferOutOrderBean  implements IDoc {


    /**
     * id : 25
     * transCode : TR201812240001
     * transDate : 1545580800000
     */

    private int id;
    private String transCode;//单据编号
    private long transDate;//单据日期

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

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public long getTransDate() {
        return transDate;
    }

    public void setTransDate(long transDate) {
        this.transDate = transDate;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_doc);
    }

    @Override
    public CharSequence getDocNo() {
        return transCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }
        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.date, DateUtils.getStringDate(transDate)));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
