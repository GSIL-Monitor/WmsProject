package com.wanhao.wms.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseQItemAdapter;
import com.wanhao.wms.base.QBaseViewHolder;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class DocAdapter extends BaseQItemAdapter<IDoc, QBaseViewHolder> {
    public static final int SINGLE_LINE = 1;

    public DocAdapter() {
        super(R.layout.item_doc);
    }

    public IDocChildClickListener clickListener;

    public IDocChildClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(IDocChildClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected void convert(QBaseViewHolder helper, IDoc item) {
        helper.setText(R.id.item_doc_no_label_tv, item.getDocNoLabel())
                .setText(R.id.item_doc_no_tv, item.getDocNo());


        int i = item.isShowSn() ? View.VISIBLE : View.GONE;
        View snGroup = helper.getView(R.id.item_doc_sn_group);
        if (snGroup.getVisibility() != i) {
            snGroup.setVisibility(i);
        }
        RecyclerView rv = helper.getView(R.id.item_doc_rv);
        Object tag = rv.getTag();
        helper.addOnClickListener(R.id.item_doc_sn_group);
        if (tag == null) {
            DocItemAdapter itemAdapter = new DocItemAdapter();
            itemAdapter.setNewData(item.getTables());
            tag = itemAdapter;
            rv.setTag(tag);
            GridLayoutManager layout = new GridLayoutManager(rv.getContext(), 6);
            SpanSizeLookupExt spanSizeLookup = new SpanSizeLookupExt();
            itemAdapter.setSpanSizeLookup(spanSizeLookup);
            rv.setLayoutManager(layout);
            rv.setTag(R.id.rv_topbar, spanSizeLookup);
            rv.setAdapter(itemAdapter);

        }
        SpanSizeLookupExt spanSizeLookup = (SpanSizeLookupExt) rv.getTag(R.id.rv_topbar);
        spanSizeLookup.setLabels(item.getTables());
        helper.addOnClickListener(R.id.item_doc_rv);
        DocItemAdapter itemAdapter = (DocItemAdapter) tag;
        if (itemAdapter.getData() != item.getTables()) {
            itemAdapter.setNewData(item.getTables());
            itemAdapter.notifyDataSetChanged();
        }
        final IDoc data = item;
        final int parentPosition = helper.getAdapterPosition();
        itemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (clickListener != null) {
                    clickListener.onDocChildClick(data, adapter, view,parentPosition, position);
                }
            }
        });
    }

    public static class SpanSizeLookupExt implements SpanSizeLookup {
        private List<ILabel> labels;


        public void setLabels(List<ILabel> labels) {
            this.labels = labels;
        }

        @Override
        public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
            return labels == null ? 3 : labels.get(position).getItemType();
        }
    }

    public interface IDocChildClickListener {

        void onDocChildClick(IDoc data, BaseQuickAdapter adapter, View view, int parentPosition, int position);
    }
}
