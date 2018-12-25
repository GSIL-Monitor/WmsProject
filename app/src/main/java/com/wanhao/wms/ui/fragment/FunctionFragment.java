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
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.GridAdapter;
import com.wanhao.wms.ui.adapter.GridBean;
import com.wanhao.wms.ui.adapter.IGrid;
import com.wanhao.wms.ui.function.base.EnterCommBean;
import com.wanhao.wms.ui.function.base.EnterCommOrderPresenter;
import com.wanhao.wms.ui.function.base.doc.DocListActivity;
import com.wanhao.wms.ui.function.enter.EnterStoragePresenter;
import com.wanhao.wms.ui.function.enter.OtherEnterPresenter;
import com.wanhao.wms.ui.function.enter.ProductionOrderPresenter;
import com.wanhao.wms.ui.function.exit.MaterialExitOrderPresenter;
import com.wanhao.wms.ui.function.exit.OtherOrderPresenter;
import com.wanhao.wms.ui.function.exit.SalesOrderPresenter;
import com.wanhao.wms.ui.function.in.RackDownDocPresenter;
import com.wanhao.wms.ui.function.in.RackUpDocPresenter;
import com.wanhao.wms.ui.function.in.TakeBoxDocPresenter;
import com.wanhao.wms.ui.function.in.TransferInDocPresenter;
import com.wanhao.wms.ui.function.in.TransferOutDocPresenter;
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
    public static final int f_cancel_material = 30;//材料退库
    public static final int f_cancel_sales = 31;//采购退库
    public static final int f_cancel_storage = 32;//采购退库


    public static final int f_picking_box = 41;//装箱
    public static final int f_rack_down = 42;//货位下架
    public static final int f_rack_up = 43;//货位下架
    public static final int f_transfer_out = 44;//调拨出库
    public static final int f_transfer_in = 45;//调拨入库

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
    @BindView(R.id.function_cancel_rv)
    RecyclerView mCancelRv;

    private GridAdapter gridAdapter = new GridAdapter();
    private GridAdapter mEnterAdapter = new GridAdapter();
    private GridAdapter mExitAdapter = new GridAdapter();
    private GridAdapter mCancelAdapter = new GridAdapter();
    private List<IGrid> mFunctions = new ArrayList<>();
    private List<IGrid> mEnterFunctions = new ArrayList<>();
    private List<IGrid> mExitFunctions = new ArrayList<>();
    private List<IGrid> mCancelFunctions = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        mFunctions.add(new GridBean(R.string.enter_storage, F_enter_storage, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean(R.string.production, f_enter_production, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("装箱", f_picking_box, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("货位下架", f_rack_down, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("货位上架", f_rack_up, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("调拨出库", f_transfer_out, R.drawable.icon_nav_enter));
        mFunctions.add(new GridBean("调拨入库", f_transfer_in, R.drawable.icon_nav_enter));

        mEnterFunctions.add(new GridBean(R.string.enter_storage, F_enter_storage, R.drawable.icon_nav_enter));

        mEnterFunctions.add(new GridBean(R.string.production, f_enter_production, R.drawable.icon_nav_enter));
        mEnterFunctions.add(new GridBean(R.string.other_enter, f_enter_other, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean(R.string.marketing_exit, f_exit_sales, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean(R.string.material_exit, f_exit_material, R.drawable.icon_nav_enter));
        mExitFunctions.add(new GridBean(R.string.other_exit, f_exit_other, R.drawable.icon_nav_enter));
//        mFunctions.add(new GridBean("采购入库", FUNCTION_CGRK, R.drawable.icon_nav_enter));


        mCancelFunctions.add(new GridBean(R.string.material_cancel, f_cancel_material, R.drawable.icon_nav_enter));
        mCancelFunctions.add(new GridBean(R.string.sales_cancel, f_cancel_sales, R.drawable.icon_nav_enter));
        mCancelFunctions.add(new GridBean(R.string.storage_cancel, f_cancel_storage, R.drawable.icon_nav_enter));

        gridAdapter.setNewData(mFunctions);

        mExitAdapter.setNewData(mExitFunctions);
        mEnterAdapter.setNewData(mEnterFunctions);
        mCancelAdapter.setNewData(mCancelFunctions);
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
        mCancelRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mCancelRv.setAdapter(mCancelAdapter);

        mEnterAdapter.setOnItemClickListener(this);
        mExitAdapter.setOnItemClickListener(this);
        gridAdapter.setOnItemClickListener(this);
        mCancelAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        GridBean o = (GridBean) adapter.getData().get(position);
        switch (o.getTag()) {
            case F_enter_storage:
                EnterCommBean storage = new EnterCommBean(R.string.enter_storage
                        , UrlApi.purchaseOrder
                        , UrlApi.purchaseOrderDetails
                        , UrlApi.purchaseOrderDetails_submit);
                EnterCommOrderPresenter.put(storage, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_enter_production:
                EnterCommBean production = new EnterCommBean(R.string.production
                        , UrlApi.productionOrder
                        , UrlApi.productionOrderDetails
                        , UrlApi.productionOrderDetails_submit);
                EnterCommOrderPresenter.put(production, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_enter_other:
                EnterCommBean other = new EnterCommBean(R.string.other_enter
                        , UrlApi.other_enter
                        , UrlApi.other_enter_details
                        , UrlApi.other_enter_details_submit);
                EnterCommOrderPresenter.put(other, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
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
            case f_cancel_material:
                EnterCommBean materialCancel = new EnterCommBean(R.string.material_cancel
                        , UrlApi.materials_cancel_order
                        , UrlApi.materials_cancel_order_details
                        , UrlApi.materials_cancel_submit);
                EnterCommOrderPresenter.put(materialCancel, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_cancel_sales:
                EnterCommBean salesCancel = new EnterCommBean(R.string.sales_cancel
                        , UrlApi.sales_cancel_order
                        , UrlApi.sales_cancel_order_details
                        , UrlApi.sales_cancel_submit);
                EnterCommOrderPresenter.put(salesCancel, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_cancel_storage:
                EnterCommBean storageCancel = new EnterCommBean(R.string.storage_cancel
                        , UrlApi.storage_cancel_order
                        , UrlApi.storage_cancel_order_details
                        , UrlApi.storage_cancel_submit);
                EnterCommOrderPresenter.put(storageCancel, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);

                break;
            case f_picking_box:
                DocListActivity.put(TakeBoxDocPresenter.class, bundle);
                break;
            case f_rack_down:
                DocListActivity.put(RackDownDocPresenter.class, bundle);
                break;
            case f_rack_up:
                DocListActivity.put(RackUpDocPresenter.class, bundle);
                break;
            case f_transfer_out:
                DocListActivity.put(TransferOutDocPresenter.class, bundle);
                break;
            case f_transfer_in:
                DocListActivity.put(TransferInDocPresenter.class, bundle);
                break;
            default:
                return;
        }
        startActivity(DocListActivity.class, bundle);

    }
}
