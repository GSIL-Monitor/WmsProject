package com.wanhao.wms.ui.adapter;

import android.text.TextUtils;

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
public class DocItemAdapter extends BaseQItemAdapter<ILabel, QBaseViewHolder> {
    public DocItemAdapter() {
        super(R.layout.item_text_text);

    }

    @Override
    protected void convert(QBaseViewHolder helper, ILabel item) {
        CharSequence label = item.getLabel();
        if (TextUtils.isEmpty(label)) {
            helper.setText(R.id.text, item.getLabelRes());
        } else {
            helper.setText(R.id.text, label);
        }
        helper.setText(R.id.text2, item.getText());
    }
}
