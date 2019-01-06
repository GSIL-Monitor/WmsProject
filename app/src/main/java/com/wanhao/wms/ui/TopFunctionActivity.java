package com.wanhao.wms.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.bean.TopFunction;
import com.wanhao.wms.ui.adapter.GridAdapter;
import com.wanhao.wms.ui.adapter.GridBean;
import com.wanhao.wms.ui.adapter.IGrid;

import java.util.ArrayList;
import java.util.List;

@BindLayout(layoutRes = R.layout.activity_top_function, title = "常用功能", addStatusBar = true)
public class TopFunctionActivity extends BaseActivity {

    @BindView(R.id.top_function_all_rv)
    RecyclerView mAllRv;
    @BindView(R.id.top_function_rv)
    RecyclerView mTopRv;


    GridAdapter mTopFunctionAdapter = new GridAdapter();
    GridAdapter mAllFunctionAdapter = new GridAdapter();

    private FunctionsHelper functionsHelper;
    private List<IGrid> mAllFunctions = new ArrayList<>();
    private List<IGrid> mTopFunctions = new ArrayList<>();


    @Override
    public void initData() {
        super.initData();
        functionsHelper = new FunctionsHelper();
        mTopFunctionAdapter.setNewData(mTopFunctions);
        mAllFunctionAdapter.setNewData(mAllFunctions);

        List<TopFunction> selectFunctions = TopFunction.getSelectFunctions();
        List<GridBean> allFunctions = functionsHelper.getAllFunctions();
        int[] tags = new int[selectFunctions.size()];
        for (int i = 0; i < selectFunctions.size(); i++) {
            tags[i] = allFunctions.get(i).getTag();
        }
        List<GridBean> type = functionsHelper.getType(tags);
        mTopFunctions.addAll(type);

        OK:
        for (GridBean allFunction : allFunctions) {
            for (int tag : tags) {
                if (allFunction.getTag() == tag) {
                    continue OK;
                }
            }
            mAllFunctions.add(allFunction);
        }
    }

    @Override
    public void initWidget() {
        mTopRv.setLayoutManager(new GridLayoutManager(this, 5) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mAllRv.setLayoutManager(new GridLayoutManager(this, 5) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mTopRv.setAdapter(mTopFunctionAdapter);
        mAllRv.setAdapter(mAllFunctionAdapter);

        mTopFunctionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                IGrid iGrid = mTopFunctions.get(position);
                mAllFunctions.add(iGrid);
                mTopFunctions.remove(position);
                TopFunction functionsByTag = TopFunction.getFunctionsByTag(iGrid.getTag());
                if (functionsByTag != null) {
                    functionsByTag.delete();
                }
                mAllFunctionAdapter.notifyDataSetChanged();
                mTopFunctionAdapter.notifyDataSetChanged();
            }
        });

        mAllFunctionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IGrid iGrid = mAllFunctions.get(position);
                mTopFunctions.add(iGrid);
                mAllFunctions.remove(position);
                TopFunction topFunction = new TopFunction();
                topFunction.setSelect(1);
                topFunction.setTag(iGrid.getTag());
                topFunction.saveSingle();
                mTopFunctionAdapter.notifyDataSetChanged();
                mAllFunctionAdapter.notifyDataSetChanged();
            }
        });


        mTopFunctionAdapter.setShowRemove(true);


    }
}
