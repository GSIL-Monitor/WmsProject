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
 * 创建时间 2018/12/17
 *
 * @author ql
 */
public class OutOrderBean implements IDoc {


    /**
     * id : 54
     * soCode : OSO201812130001
     * soDate : 1544630400000
     * cusCode : Z00
     * cusName : 备货
     */

    private int id;//	单据ID
    private String soCode;//单据编号
    private long soDate;//单据日期
    private String recCode;//	销售订单号
    private String cusCode;//	客户编码
    private String cusName;//客户名称
    private List<ILabel> labels;

    public List<ILabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ILabel> labels) {
        this.labels = labels;
    }

    public String getRecCode() {
        return recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoCode() {
        return soCode;
    }

    public void setSoCode(String soCode) {
        this.soCode = soCode;
    }

    public long getSoDate() {
        return soDate;
    }

    public void setSoDate(long soDate) {
        this.soDate = soDate;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_doc);
    }


    @Override
    public CharSequence getDocNo() {
        if (soCode == null) {
            return "";
        }
        return soCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }
        labels = new ArrayList<>();
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.market_doc), getRecCode(),3));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.client_name), getCusName(),3));
        labels.add(new LabelBean(MyApp.getContext().getString(R.string.date), DateUtils.getStringDate(getSoDate()),3));

        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
