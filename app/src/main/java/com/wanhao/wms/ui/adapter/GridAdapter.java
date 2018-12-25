package com.wanhao.wms.ui.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseQItemAdapter;
import com.wanhao.wms.base.QBaseViewHolder;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class GridAdapter extends BaseQItemAdapter<IGrid, QBaseViewHolder> {
    public GridAdapter() {
        super(R.layout.item_grid);
    }

    @Override
    protected void convert(QBaseViewHolder helper, IGrid item) {
        CharSequence label = item.getLabel();
        if (label != null) {

            helper.setText(R.id.item_grid_tv, label);
        } else {
            helper.setText(R.id.item_grid_tv, item.getLabelRes());
        }
        helper
                .setImageResource(R.id.item_grid_iv, item.getIconRes());
    }
}
