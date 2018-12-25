package com.wanhao.wms.ui.function.in;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.RackDownDetailsBean;
import com.wanhao.wms.bean.RackDownOrderBean;
import com.wanhao.wms.bean.Sn;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.RackDownDocDetailsActivity;
import com.wanhao.wms.ui.function.RackSnListActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/25
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.rack_up)
public class RackUpOperatePresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<RackDownDetailsBean> mGoodsAll;
    private RackDownDetailsBean mToChangeGoods;
    private String mTargetRack;

    public static void putDoc(RackDownOrderBean iDoc, Bundle bundle) {
        iDoc.setLabels(null);
        bundle.putString(DOC, JsonUtils.toJson(iDoc));
    }


    DecodeCallback callback = new DecodeCallback(MarkRules.RACK_MARK_CODE, MarkRules.GOODS_MARK_CODE) {

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            iDialog.cancelLoadingDialog();
        }

        @Override
        public void onRackCode(DecodeBean data) {
            super.onRackCode(data);
            mTargetRack = data.getDOC_CODE();
            iGoodsListView.setRackTextView(mTargetRack);
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            if (mGoodsAll == null) {
                iDialog.displayMessageDialog("初始化异常，请重新操作");
                return;
            }
            boolean isHas = false;
            for (RackDownDetailsBean d : mGoodsAll) {
                if (GoodsUtils.isSame(d, data)) {
                    isHas = true;
                    addGoods(d, data);
                }
            }
            if (!isHas) {
                iDialog.displayMessageDialog("任务单不存在该货品->sku=" + data.getSKU_CODE());
                return;
            }

        }

        @Override
        public void onOtherCode(DecodeBean data) {
            iDialog.displayMessageDialog("解码类型不匹配");
        }

        @Override
        protected void onFailed(String error, int code) {
            iDialog.displayMessageDialog(error);
        }
    };
    private RackDownOrderBean mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();

    private void addGoods(RackDownDetailsBean g, IGoodsDecode goods) {
        boolean add = false;
        for (IDoc iDoc : mGoodsList) {
            RackDownDetailsBean d = (RackDownDetailsBean) iDoc;

            if (GoodsUtils.isSame(d, goods)) {
                double totalCount = d.getNowQty() + goods.getPLN_QTY().intValue();
                if (!GoodsUtils.checkTotal(goods, d)) {
                    iDialog.displayMessageDialog("超出可添加数量 sku:" + goods.getSKU_CODE() + ",数量:" + goods.getPLN_QTY());
                    return;
                }
                add = true;
                if (goods.isSerial() && !d.isAutoSn()) {
                    for (Sn sn : d.getSnList()) {
                        if (sn.getSnNo().equals(goods.getSN_NO())) {
                            iDialog.displayMessageDialog("序列号不能重复添加!" + sn.getSnNo());
                            return;
                        }
                    }
                    //如果序列号肯定存储，那snList肯定不为kong
                    Sn e = new Sn();
                    e.setPorderId(d.getId());
                    e.setSnNo(goods.getSN_NO());
                    d.getSnList().add(e);
                    d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
                    d.setLabels(null);
                    break;
                }

                d.setNowQty(totalCount);
                d.setLabels(null);
            }
        }

        if (add) {
            mDocAdapter.notifyDataSetChanged();
            return;
        }


        RackDownDetailsBean clone = (RackDownDetailsBean) g.clone();

        double totalCount = clone.getNowQty() + goods.getPLN_QTY().intValue();
        if (!GoodsUtils.checkTotal(goods, clone)) {
            iDialog.displayMessageDialog("超出可添加数量 sku:" + goods.getSKU_CODE() + ",数量:" + goods.getPLN_QTY());
            return;
        }
        if (clone.isSerial() && !clone.isAutoSn()) {
            List<Sn> snList = clone.getSnList();
            //没有存入序列号
            if (snList == null) {
                clone.setSnList(new ArrayList<Sn>());
                Sn e = new Sn();
                e.setPorderId(mDocOrder.getId());
                e.setSnNo(goods.getSN_NO());
                clone.getSnList().add(e);
                clone.setNowQty((int) (clone.getNowQty() + goods.getPLN_QTY()));
            }
        } else {
            clone.setNowQty(goods.getPLN_QTY().intValue());
        }

        mGoodsList.add(0, clone);

        mDocAdapter.notifyDataSetChanged();
    }

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iDialog.displayLoadingDialog("初始化数据");
        EventBus.getDefault().register(this);
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, RackDownOrderBean.class);
        mDocAdapter.setNewData(mGoodsList);
        iGoodsListView.setRackTextView(mDocOrder.getAdjustCode());
        loadDocGoods();
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("adjustCode", mDocOrder.getAdjustCode());
        OkHttpHeader.post(UrlApi.rack_up_order_details, mParams, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(RackDownDetailsBean.class);

            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        RackDownDetailsBean iDoc = (RackDownDetailsBean) mGoodsList.get(position);
        if (iDoc.isAutoSn()) {
            iDialog.displayMessageDialog("序列号自动生成");
            return;
        }
        super.onItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        RackDownDetailsBean pd = (RackDownDetailsBean) mGoodsList.get(position);
        if (pd.isSerial() && !pd.isAutoSn()) {
            return;
        }
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(ActivityUtils.getTop());
        final EditText editText = editTextDialogBuilder
                .getEditText();
        editText.setText(String.valueOf(pd.getNowQty()));
        editTextDialogBuilder.setPlaceholder("请输入数量");
        editTextDialogBuilder.setTitle("请输入数量")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String s = editText.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            mGoodsList.remove(position);
                        } else {
                            int i = Integer.parseInt(s);
                            if (i == 0) {
                                mGoodsList.remove(position);
                            } else {
                                RackDownDetailsBean iDoc = (RackDownDetailsBean) mGoodsList.get(position);
                                iDoc.setLabels(null);
                                iDoc.setNowQty(i);
                            }
                        }
                        mDocAdapter.notifyDataSetChanged();

                        QMUIKeyboardHelper.hideKeyboard(editText);
                        dialog.cancel();
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                QMUIKeyboardHelper.hideKeyboard(editText);
                dialog.cancel();
            }
        }).show();
    }

    @Override
    public void handleClickSearch(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        decode(text.toString());
    }

    @Override
    public void actionSubmit() {
        if (mGoodsList.size() == 0) {
            return;
        }
        if (TextUtils.isEmpty(mTargetRack)) {
            iDialog.displayMessageDialog("请添加目标货位");
            return;
        }
        iDialog.displayLoadingDialog("提交中");
        List params = new ArrayList<>();
        for (IDoc iDoc : mGoodsList) {
            RackDownDetailsBean pd = (RackDownDetailsBean) iDoc;
            if (pd.isSerial()) {
                if (pd.getNowQty() < pd.getOpQty()) {
                    iDialog.displayMessageDialog("序列号必须全部提交，否则不可以进行提交操作!!!");
                    iDialog.cancelLoadingDialog();
                    return;
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", pd.getId());
            map.put("lineNo", pd.getLineNo());
            map.put("adjustCode", pd.getAdjustCode());
            map.put("skuCode", pd.getSkuCode());
            map.put("lotNo", pd.getLotNo());
            map.put("plnQty", pd.getNowQty());
            map.put("snList", pd.getSnList());
            map.put("locCodeTo", mTargetRack);
            params.add(map);
        }
        OkHttpHeader.post(UrlApi.rack_up_order_submit, params, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                if (!resultObj.isRs()) {
                    onFailed(resultObj.getMessage(), id);
                    return;
                }
                iDialog.toast("提交成功");
                EventBus.getDefault().post(0);
                ActivityUtils.getTop().finish();
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }


    @Override
    public void decode(String decode) {
        iDialog.displayLoadingDialog("解码中");
        DecodeHelper.decode(decode, callback);
    }

    @Override
    public void actionDocDetails() {
        Bundle bundle = new Bundle();
        RackDownDocDetailsActivity.put(mDocOrder, bundle);
        RackDownDocDetailsActivity.putLoadUrl(UrlApi.rack_down_order_details, bundle);

        iToAction.startActivity(RackDownDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (RackDownDetailsBean) d;
        Bundle bundle = new Bundle();
        RackSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(RackSnListActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeGoods(RackDownDetailsBean pd) {
        if (pd.getSnList().size() == 0) {
            mGoodsList.remove(mToChangeGoods);
        }
        mToChangeGoods.setSnList(pd.getSnList());
        mToChangeGoods.setNowQty(pd.getSnList().size());
        mToChangeGoods.setLabels(null);
        mDocAdapter.notifyDataSetChanged();
    }

}
