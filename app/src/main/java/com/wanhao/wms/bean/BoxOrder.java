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
 * 创建时间 2018/12/24
 *
 * @author ql
 */
public class BoxOrder implements IDoc {


    /**
     * id : 10027
     * plCode : PL201812210001
     * plDate : 1545321600000
     * depCode : 01101
     * depName : 生产管理部
     * empCode : LLF
     * empName : 李龙飞
     */

    private int id;//	装箱单ID
    private String plCode;//装箱单号
    private long plDate;//	装箱日期
    private String depCode;//	部门编码
    private String depName;//部门名称
    private String empCode;//操作员编码
    private String empName;//操作员名称

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

    public String getPlCode() {
        return plCode;
    }

    public void setPlCode(String plCode) {
        this.plCode = plCode;
    }

    public long getPlDate() {
        return plDate;
    }

    public void setPlDate(long plDate) {
        this.plDate = plDate;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.label_box);
    }

    @Override
    public CharSequence getDocNo() {
        return plCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }

        labels = new ArrayList<>();
        labels.add(new LabelBean(R.string.date, DateUtils.getStringDate(plDate)));
        labels.add(new LabelBean(R.string.section, depName));
        labels.add(new LabelBean(R.string.operate_user, empName));
        return labels;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
