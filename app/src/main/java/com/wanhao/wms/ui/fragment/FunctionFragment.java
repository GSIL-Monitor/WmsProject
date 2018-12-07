package com.wanhao.wms.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseFragment;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.ui.adapter.GridAdapter;
import com.wanhao.wms.ui.adapter.GridBean;
import com.wanhao.wms.ui.adapter.IGrid;
import com.wanhao.wms.ui.function.base.doc.DocListActivity;
import com.wanhao.wms.ui.function.enter.EnterStoragePresenter;
import com.wanhao.wms.ui.function.enter.OtherEnterPresenter;
import com.wanhao.wms.ui.function.enter.ProductionOrderPresenter;
import com.wanhao.wms.utils.div.GridDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
@BindLayout(layoutRes = R.layout.frag_funnction, title = "全部功能", backRes = 0, addStatusBar = true)
public class FunctionFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    public static final int FUNCTION_CGRK = 2;//采购入库

    public static FunctionFragment newInstance() {

        Bundle args = new Bundle();

        FunctionFragment fragment = new FunctionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.function_rv)
    RecyclerView rv;

    private GridAdapter gridAdapter = new GridAdapter();
    private List<IGrid> mFunctions = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("生产入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("其他入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        gridAdapter.setNewData(mFunctions);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);


        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv.addItemDecoration(new GridDivider(getContext()));
        rv.setAdapter(gridAdapter);

        gridAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                DocListActivity.put(EnterStoragePresenter.class, bundle);
                startActivity(DocListActivity.class, bundle);
                break;
            case 1:
                DocListActivity.put(ProductionOrderPresenter.class, bundle);
                startActivity(DocListActivity.class, bundle);
                break;
            case 2:
                DocListActivity.put(OtherEnterPresenter.class, bundle);
                startActivity(DocListActivity.class, bundle);
                break;
        }



    }
}
