package com.wanhao.wms.bean;

import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/18
 *
 * @author ql
 */
public class PickingOrder implements IDoc {
    /**
     * pickCode : PL201812090006
     */

    private String pickCode;

    private List<ILabel> labels;

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }


    @Override
    public CharSequence getDocNoLabel() {
        return MyApp.getContext().getString(R.string.pick_no);
    }

    @Override
    public CharSequence getDocNo() {
        return pickCode;
    }

    @Override
    public List<ILabel> getTables() {
        if (labels != null) {
            return labels;
        }
        return labels = new ArrayList<>();
    }

    @Override
    public boolean isShowSn() {
        return false;
    }
}
