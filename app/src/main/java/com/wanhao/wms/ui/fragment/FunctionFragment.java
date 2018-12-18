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
import com.wanhao.wms.ui.function.exit.MaterialExitOrderPresenter;
import com.wanhao.wms.ui.function.exit.OtherOrderPresenter;
import com.wanhao.wms.ui.function.exit.SalesOrderPresenter;
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

    public static final int F_enter_storage = 0;//采购入库
    public static final int f_enter_production = 1;//生产入库
    public static final int f_enter_other = 2;//其他入库
    public static final int f_exit_sales = 3;//销售出库
    public static final int f_exit_other = 4;//其他出库
    public static final int f_exit_material = 5;//材料出库
    public static final int FUNCTION_CGRK = 6;//采购入库

    public static FunctionFragment newInstance() {

        Bundle args = new Bundle();

        FunctionFragment fragment = new FunctionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.function_rv)
    RecyclerView rv;
    @BindView(R.id.function_enter_rv)
    RecyclerView mEnterRv;
    @BindView(R.id.function_exit_rv)
    RecyclerView mExitRv;

    private GridAdapter gridAdapter = new GridAdapter();
    private GridAdapter mEnterAdapter = new GridAdapter();
    private GridAdapter mExitAdapter = new GridAdapter();
    private List<IGrid> mFunctions = new ArrayList<>();
    private List<IGrid> mEnterFunctions = new ArrayList<>();
    private List<IGrid> mExitFunctions = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        mFunctions.add(new GridBean("采购入库", F_enter_storage, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("生产入库", f_enter_production, R.drawable.icon_nav_enter));

        mEnterFunctions.add(new GridBean("采购入库", F_enter_storage, R.drawable.icon_nav_enter));

        mEnterFunctions.add(new GridBean("生产入库", f_enter_production, R.drawable.icon_nav_enter));
        mEnterFunctions.add(new GridBean("其他入库", f_enter_other, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean("销售出库", f_exit_sales, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean("材料出库", f_exit_material, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean("其他出库", f_exit_other, R.drawable.icon_nav_enter));
//        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));
        gridAdapter.setNewData(mFunctions);

        mExitAdapter.setNewData(mExitFunctions);
        mEnterAdapter.setNewData(mEnterFunctions);
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);


        rv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv.setAdapter(gridAdapter);
        mEnterRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mEnterRv.setAdapter(mEnterAdapter);
        mExitRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mExitRv.setAdapter(mExitAdapter);
        mEnterAdapter.setOnItemClickListener(this);
        mExitAdapter.setOnItemClickListener(this);
        gridAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case F_enter_storage:
                DocListActivity.put(EnterStoragePresenter.class, bundle);
                break;
            case f_enter_production:
                DocListActivity.put(ProductionOrderPresenter.class, bundle);
                break;
            case f_enter_other:
                DocListActivity.put(OtherEnterPresenter.class, bundle);
                break;
            case f_exit_sales:
                DocListActivity.put(SalesOrderPresenter.class, bundle);
                break;
            case f_exit_other:
                DocListActivity.put(OtherOrderPresenter.class, bundle);
                break;
            case f_exit_material:
                DocListActivity.put(MaterialExitOrderPresenter.class, bundle);
                break;
        }
        startActivity(DocListActivity.class, bundle);

    }
}
