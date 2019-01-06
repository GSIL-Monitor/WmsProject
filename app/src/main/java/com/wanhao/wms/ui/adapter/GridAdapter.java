package com.wanhao.wms.ui.adapter;

import android.view.View;

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

    private boolean showRemove;
    private int iconRes = -1;

    @Override
    protected void convert(QBaseViewHolder helper, IGrid item) {
        CharSequence label = item.getLabel();
        if (label != null) {

            helper.setText(R.id.item_grid_tv, label);
        } else {
            helper.setText(R.id.item_grid_tv, item.getLabelRes());
        }

        if (iconRes != -1) {
            helper.setImageResource(R.id.item_grid_remove_iv, iconRes);
        }
        int i = showRemove ? View.VISIBLE : View.GONE;
        helper.getView(R.id.item_grid_remove_iv).setVisibility(i);
        helper.setImageResource(R.id.item_grid_iv, item.getIconRes());

        helper.addOnClickListener(R.id.item_grid_remove_iv);
    }

    public boolean isShowRemove() {
        return showRemove;
    }

    public void setShowRemove(boolean showRemove) {
        this.showRemove = showRemove;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
