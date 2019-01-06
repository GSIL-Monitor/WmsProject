package com.wanhao.wms.ui.adapter;

import android.opengl.Visibility;
import android.view.View;

import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseQItemAdapter;
import com.wanhao.wms.base.QBaseViewHolder;
import com.wanhao.wms.bean.EnterStrGoodsSubParams;
import com.wanhao.wms.bean.Sn;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public class SnAdapter extends BaseQItemAdapter<Sn, QBaseViewHolder> {
    private boolean canRemove = true;

    public SnAdapter() {
        super(R.layout.item_sn_operate);
    }

    @Override
    protected void convert(QBaseViewHolder helper, Sn item) {
        helper.setText(R.id.item_sn_operate_index_tv, helper.getAdapterPosition() + 1 + "")
                .setText(R.id.item_sn_operate_sn_tv, item.getSnNo());
        int i = canRemove ? View.VISIBLE : View.GONE;
        helper.getView(R.id.item_sn_operate_remove_iv).setVisibility(i);
        helper.addOnClickListener(R.id.item_sn_operate_remove_iv);
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }
}
