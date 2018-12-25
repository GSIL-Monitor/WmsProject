package com.wanhao.wms.ui.function.exit;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.PickingOrder;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.Page;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.adapter.DocItemAdapter;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.doc.AbsDocPresenter;
import com.wanhao.wms.ui.function.base.goods.GoodsLIstActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：材料出库任务单列表
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/18
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.material_exit)
public class MaterialExitOrderPresenter extends AbsDocPresenter implements BaseQuickAdapter.OnItemClickListener {
    private Map<String, Object> mParams = new HashMap<>();

    private Page page = new Page();
    private List datas = new ArrayList();

    private DocAdapter docItemAdapter = new DocAdapter();

    @Override
    public void init(Bundle bundle) {
        iDocView.setAdapter(docItemAdapter);
        iDialog.displayLoadingDialog(MyApp.getContext().getString(R.string.load_data));
        EventBus.getDefault().register(this);
        docItemAdapter.setNewData(datas);
        docItemAdapter.setOnItemClickListener(this);
        loadData();
    }

    @Override
    public void handlerClickSearch(CharSequence text) {
        iDialog.displayLoadingDialog(MyApp.getContext().getString(R.string.load_query_data));
        mParams.put("qsparam", text);
        loadData();
    }

    @Override
    public void onRefresh() {
        datas.clear();
        page.setPage(1);
        loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeData(Integer i) {
        onRefresh();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }


    protected void loadData() {
        page.put(mParams);
        docItemAdapter.setEnableLoadMore(false);
        OkHttpHeader.post(UrlApi.picking_list, mParams, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
                iDocView.setRefresh(false);
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                ArrayList<PickingOrder> list = resultObj.getList(PickingOrder.class);
                if (list != null && list.size() != 0) {
                    datas.addAll(list);
                }

                docItemAdapter.setNewData(datas);

                docItemAdapter.notifyDataSetChanged();

                if (datas.size() >= resultObj.getTotal()) {
                    docItemAdapter.loadMoreEnd();
                } else {
                    page.add();
                }
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PickingOrder o = (PickingOrder) datas.get(position);
        Bundle bundle = new Bundle();
        MaterialOperatePresenter.putDoc(o, bundle);
        GoodsLIstActivity.putPresenter(MaterialOperatePresenter.class, bundle);
        iToAction.startActivity(GoodsLIstActivity.class, bundle);


    }
}
