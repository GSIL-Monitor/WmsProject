package com.wanhao.wms.bean;

import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
public class WarehouseBean extends LitePalSupport implements IDoc {

    private Integer id;
    private Integer whId;//仓库id
    private String whCode;//仓库编码
    private String whName;//仓库名称
    private String whDescr;//仓库描述
    private String locFlag;//是否货位管理 y/n
    private String whStatus;//仓库状态 y/n
    private String leader;//负责人
    private String linkTel;//联系电话

    private List<ILabel> tables;

    public static WarehouseBean getSelectWarehouse() {
        List<WarehouseBean> all = LitePal.findAll(WarehouseBean.class);
        if (all.size() == 0) {
            return null;
        }
        return all.get(0);
    }

    public void saveSingle() {
        WarehouseBean selectWarehouse = getSelectWarehouse();
        if (selectWarehouse == null) {
            save();
            return;
        }

        update(selectWarehouse.id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        if (whCode == null) {
            return "";
        }
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhName() {
        if (whName == null) {
            return "";
        }
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getWhDescr() {
        if (whDescr == null) {
            return "";
        }
        return whDescr;
    }

    public void setWhDescr(String whDescr) {
        this.whDescr = whDescr;
    }

    public String getLocFlag() {
        return locFlag;
    }

    public void setLocFlag(String locFlag) {
        this.locFlag = locFlag;
    }

    public String getWhStatus() {
        return whStatus;
    }

    public void setWhStatus(String whStatus) {
        this.whStatus = whStatus;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    @Override
    public CharSequence getDocNoLabel() {
        if (whCode == null) {
            return "未知编号";
        }
        return whCode;
    }

    @Override
    public CharSequence getDocNo() {
        if (whName == null) {
            return "未知名称";
        }
        return whName;
    }

    @Override
    public List<ILabel> getTables() {
        if (tables != null) {
            return tables;
        }
        List<ILabel> list = new ArrayList<>();
        list.add(new LabelBean("负责人", leader));
        list.add(new LabelBean("联系电话", linkTel));
        list.add(new LabelBean("仓库描述", whDescr));
        tables = list;
        return tables;
    }

    @Override
    public boolean isShowSn() {
        return false;
    }


}
