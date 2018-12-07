package com.wanhao.wms.ui.adapter;

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
public class DocItemAdapter extends BaseQItemAdapter<ILabel,QBaseViewHolder> {
    public DocItemAdapter() {
        super(R.layout.item_text_text);

    }

    @Override
    protected void convert(QBaseViewHolder helper, ILabel item) {
        helper.setText(R.id.text,item.getLabel())
                .setText(R.id.text2,item.getText());
    }
}
