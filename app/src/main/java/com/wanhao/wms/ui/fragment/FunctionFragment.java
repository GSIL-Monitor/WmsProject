package com.wanhao.wms.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseFragment;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.TopFunction;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.FunctionsHelper;
import com.wanhao.wms.ui.TopFunctionActivity;
import com.wanhao.wms.ui.adapter.GridAdapter;
import com.wanhao.wms.ui.adapter.GridBean;
import com.wanhao.wms.ui.adapter.IGrid;
import com.wanhao.wms.ui.function.base.EnterCommBean;
import com.wanhao.wms.ui.function.base.EnterCommOrderPresenter;
import com.wanhao.wms.ui.function.base.ExitCommOrderPresenter;
import com.wanhao.wms.ui.function.base.doc.DocListActivity;
import com.wanhao.wms.ui.function.base.goods.GoodsLIstActivity;
import com.wanhao.wms.ui.function.exit.MaterialExitOrderPresenter;
import com.wanhao.wms.ui.function.exit.OtherOrderPresenter;
import com.wanhao.wms.ui.function.exit.SalesOrderPresenter;
import com.wanhao.wms.ui.function.in.CheckQtyDocPresenter;
import com.wanhao.wms.ui.function.in.QueryGoodsPersenter;
import com.wanhao.wms.ui.function.in.RackDownDocPresenter;
import com.wanhao.wms.ui.function.in.RackUpDocPresenter;
import com.wanhao.wms.ui.function.in.TakeBoxDocPresenter;
import com.wanhao.wms.ui.function.in.TransferInDocPresenter;
import com.wanhao.wms.ui.function.in.TransferOutDocPresenter;

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
    public static final int f_cancel_sales = 31;//销售退库
    public static final int f_cancel_storage = 32;//采购退库


    public static final int f_picking_box = 41;//装箱
    public static final int f_rack_down = 42;//货位下架
    public static final int f_rack_up = 43;//货位下架
    public static final int f_transfer_out = 44;//调拨出库
    public static final int f_transfer_in = 45;//调拨入库
    public static final int f_check_qty = 46;//盘点
    public static final int f_query_goods = 47;//库存查询

    public static final int add_function = -1;
    private final int REQUEST_CHANGE_FUNCTION = 22;

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
    @BindView(R.id.function_in_rv)
    RecyclerView mInRv;

    private GridAdapter gridAdapter = new GridAdapter();
    private GridAdapter mEnterAdapter = new GridAdapter();
    private GridAdapter mExitAdapter = new GridAdapter();
    private GridAdapter mCancelAdapter = new GridAdapter();
    private GridAdapter mInAdapter = new GridAdapter();
    private List<IGrid> mFunctions = new ArrayList<>();
    private List<IGrid> mEnterFunctions = new ArrayList<>();
    private List<IGrid> mExitFunctions = new ArrayList<>();
    private List<IGrid> mCancelFunctions = new ArrayList<>();
    private List<IGrid> mInFunctions = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        addTopFunction();


        mInFunctions.add(new GridBean(R.string.pick_box, f_picking_box, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.rack_down, f_rack_down, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.rack_up, f_rack_up, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.transfer_out, f_transfer_out, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.transfer_in, f_transfer_in, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.check_qty, f_check_qty, R.drawable.icon_nav_enter));
        mInFunctions.add(new GridBean(R.string.query_goods, f_query_goods, R.drawable.icon_nav_enter));


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
        mInAdapter.setNewData(mInFunctions);
    }

    private void addTopFunction() {
        FunctionsHelper functionsHelper = new FunctionsHelper();
        List<TopFunction> selectFunctions = TopFunction.getSelectFunctions();
        List<GridBean> allFunctions = functionsHelper.getAllFunctions();
        int[] tags = new int[selectFunctions.size()];
        for (int i = 0; i < selectFunctions.size(); i++) {
            tags[i] = allFunctions.get(i).getTag();
        }
        List<GridBean> type = functionsHelper.getType(tags);
        mFunctions.addAll(type);
        mFunctions.add(new GridBean("添加功能", add_function, R.drawable.icon_nav_enter));
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);


        rv.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rv.setAdapter(gridAdapter);
        mEnterRv.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mEnterRv.setAdapter(mEnterAdapter);
        mExitRv.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mExitRv.setAdapter(mExitAdapter);
        mCancelRv.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mCancelRv.setAdapter(mCancelAdapter);
        mInRv.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mInRv.setAdapter(mInAdapter);

        mInAdapter.setOnItemClickListener(this);
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
            case add_function:
                startActivity(TopFunctionActivity.class, REQUEST_CHANGE_FUNCTION);
                return;
            case F_enter_storage://1
                EnterCommBean storage = new EnterCommBean(R.string.enter_storage
                        , UrlApi.purchaseOrder
                        , UrlApi.purchaseOrderDetails
                        , UrlApi.purchaseOrderDetails_submit, false);
                EnterCommOrderPresenter.put(storage, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_enter_production://1
                EnterCommBean production = new EnterCommBean(R.string.production
                        , UrlApi.productionOrder
                        , UrlApi.productionOrderDetails
                        , UrlApi.productionOrderDetails_submit, false);
                EnterCommOrderPresenter.put(production, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_enter_other://1
                EnterCommBean other = new EnterCommBean(R.string.other_enter
                        , UrlApi.other_enter
                        , UrlApi.other_enter_details
                        , UrlApi.other_enter_details_submit, false);
                EnterCommOrderPresenter.put(other, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_exit_sales://1
                DocListActivity.put(SalesOrderPresenter.class, bundle);
                break;
            case f_exit_other://1
                DocListActivity.put(OtherOrderPresenter.class, bundle);
                break;
            case f_exit_material://1
                DocListActivity.put(MaterialExitOrderPresenter.class, bundle);
                break;
            case f_cancel_material://1
                EnterCommBean materialCancel = new EnterCommBean(R.string.material_cancel
                        , UrlApi.materials_cancel_order
                        , UrlApi.materials_cancel_order_details
                        , UrlApi.materials_cancel_submit,false);
                EnterCommOrderPresenter.put(materialCancel, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_cancel_sales://1
                EnterCommBean salesCancel = new EnterCommBean(R.string.sales_cancel
                        , UrlApi.sales_cancel_order
                        , UrlApi.sales_cancel_order_details
                        , UrlApi.sales_cancel_submit,false);
                EnterCommOrderPresenter.put(salesCancel, bundle);
                DocListActivity.put(EnterCommOrderPresenter.class, bundle);
                break;
            case f_cancel_storage://1
                EnterCommBean storageCancel = new EnterCommBean(R.string.storage_cancel
                        , UrlApi.storage_cancel_order
                        , UrlApi.storage_cancel_order_details
                        , UrlApi.storage_cancel_submit);
                ExitCommOrderPresenter.put(storageCancel, bundle);
                DocListActivity.put(ExitCommOrderPresenter.class, bundle);

                break;
            case f_picking_box:
                DocListActivity.put(TakeBoxDocPresenter.class, bundle);
                break;
            case f_rack_down://1
                DocListActivity.put(RackDownDocPresenter.class, bundle);
                break;
            case f_rack_up://1
                DocListActivity.put(RackUpDocPresenter.class, bundle);
                break;
            case f_transfer_out://1
                DocListActivity.put(TransferOutDocPresenter.class, bundle);
                break;
            case f_transfer_in://1
                DocListActivity.put(TransferInDocPresenter.class, bundle);
                break;
            case f_check_qty:
                DocListActivity.put(CheckQtyDocPresenter.class, bundle);
                break;
            case f_query_goods:
                GoodsLIstActivity.putPresenter(QueryGoodsPersenter.class, bundle);
                startActivity(GoodsLIstActivity.class, bundle);
                return;
            default:
                return;
        }
        startActivity(DocListActivity.class, bundle);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHANGE_FUNCTION:
                mFunctions.clear();
                addTopFunction();
                gridAdapter.notifyDataSetChanged();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }
}
