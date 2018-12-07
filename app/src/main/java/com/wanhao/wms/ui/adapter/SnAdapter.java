package com.wanhao.wms.ui.adapter;

import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseQItemAdapter;
import com.wanhao.wms.base.QBaseViewHolder;
import com.wanhao.wms.bean.EnterStrGoodsSubParams;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
public class SnAdapter extends BaseQItemAdapter<EnterStrGoodsSubParams.Sn, QBaseViewHolder> {
    public SnAdapter() {
        super(R.layout.item_sn_operate);
    }

    @Override
    protected void convert(QBaseViewHolder helper, EnterStrGoodsSubParams.Sn item) {
        helper.setText(R.id.item_sn_operate_index_tv, helper.getAdapterPosition() + 1 + "")
                .setText(R.id.item_sn_operate_sn_tv, item.getSnNo());

        helper.addOnClickListener(R.id.item_sn_operate_remove_iv);
    }
}
