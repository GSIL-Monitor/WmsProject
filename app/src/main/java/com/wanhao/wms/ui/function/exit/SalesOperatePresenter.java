package com.wanhao.wms.ui.function.exit;

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
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.OutGoodsSubParams;
import com.wanhao.wms.bean.OutOrderBean;
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
import com.wanhao.wms.ui.function.EnterSnListActivity;
import com.wanhao.wms.ui.function.OutDocDetailsActivity;
import com.wanhao.wms.ui.function.OutSnListActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
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
 * 描述：销售退货，操作
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/12
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.marketing_exit)
public class SalesOperatePresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<OutOrderDetails> mGoodsAll;
    private OutOrderDetails mToChangeGoods;

    public static void putDoc(OutOrderBean iDoc, Bundle bundle) {
        iDoc.setLabels(null);
        bundle.putString(DOC, JsonUtils.toJson(iDoc));
    }

    private String mRackCode;

    DecodeCallback callback = new DecodeCallback(MarkRules.RACK_MARK_CODE, MarkRules.GOODS_MARK_CODE) {

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            iDialog.cancelLoadingDialog();
        }

        @Override
        public void onRackCode(DecodeBean data) {
            super.onRackCode(data);
            mRackCode = data.getDOC_VALUE();
            iGoodsListView.setRackTextView(mRackCode);
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            if (TextUtils.isEmpty(mRackCode)) {
                iDialog.displayMessageDialog("请先添加货位");
                return;
            }

            for (OutOrderDetails outOrderDetails : mGoodsAll) {
                if (checkGoods(data, outOrderDetails) && outOrderDetails.getLocCode().equals(mRackCode)) {
                    addGoods(outOrderDetails, data);
                    return;
                }
            }
            iDialog.displayMessageDialog("不包含该货品");


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

    private boolean checkGoods(IGoodsDecode data, OutOrderDetails outOrderDetails) {
        if (TextUtils.isEmpty(outOrderDetails.getLotNo())) {
            return outOrderDetails.getSkuCode().equals(data.getSKU_CODE());
        }
        return outOrderDetails.getSkuCode().equals(data.getSKU_CODE()) && outOrderDetails.getLotNo().equals(data.getLOT_NO());

    }

    private OutOrderBean mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();

    private void addGoods(OutOrderDetails g, IGoodsDecode goods) {
        boolean add = false;
        for (IDoc iDoc : mGoodsList) {
            OutOrderDetails d = (OutOrderDetails) iDoc;
            if (checkGoods(goods, d)) {
                if (d.getOpQty() < d.getNowQty() + goods.getPLN_QTY()) {
                    iDialog.displayMessageDialog("超出数量");
                    return;
                }
                add = true;
                if (goods.isSerial()) {
                    for (Sn sn : d.getSnList()) {
                        if (sn.getSnNo().equals(goods.getSN_NO())) {
                            iDialog.displayMessageDialog("序列号不能重复添加!" + sn.getSnNo());
                            return;
                        }
                    }

                    //如果序列号肯定存储，那snList肯定不为kong
                    Sn e = new Sn();
                    e.setPorderId(d.getId());
                    d.getSnList().add(e);
                    d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
                    d.setLabels(null);
                    break;
                }
                d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
                d.setLabels(null);
            }
        }

        if (add) {
            mDocAdapter.notifyDataSetChanged();
            return;
        }
        if (g.getOpQty() < g.getNowQty() + goods.getPLN_QTY()) {
            iDialog.displayMessageDialog("超出数量");
            return;
        }
        OutOrderDetails clone = (OutOrderDetails) g.clone();

        mGoodsList.add(0, clone);
        if (clone.isSerial()) {
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


        mDocAdapter.notifyDataSetChanged();
    }

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iDialog.displayLoadingDialog("初始化数据");
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, OutOrderBean.class);
        mDocAdapter.setNewData(mGoodsList);
        loadDocGoods();

        EventBus.getDefault().register(this);
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("soCode", mDocOrder.getSoCode());
        OkHttpHeader.post(UrlApi.sales_order_details, mParams, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(OutOrderDetails.class);
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        OutOrderDetails pd = (OutOrderDetails) mGoodsList.get(position);
        if (pd.isSerial()) {
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
                                OutOrderDetails iDoc = (OutOrderDetails) mGoodsList.get(position);
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
        iDialog.displayLoadingDialog("提交中");

        List<OutGoodsSubParams> params = new ArrayList<>();
        for (IDoc iDoc : mGoodsList) {
            OutOrderDetails pd = (OutOrderDetails) iDoc;
            if (pd.getNowQty() < pd.getOpQty()) {
                iDialog.displayMessageDialog("序列号必须全部提交，否则不可以进行提交操作!!!");
                iDialog.cancelLoadingDialog();
                return;
            }
            OutGoodsSubParams e = new OutGoodsSubParams();
            e.setId(pd.getId());
            e.setSoLineNo(pd.getSoLineNo());
            e.setSoCode(pd.getSoCode());
            e.setLotNo(mRackCode);
            e.setSkuCode(pd.getSkuCode());
            e.setLotNo(pd.getLotNo());
            e.setPlnQty(pd.getNowQty());
            e.setSnList(pd.getSnList());
            params.add(e);
        }
        OkHttpHeader.post(UrlApi.sales_submit, params, new BaseResultCallback() {
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
        OutDocDetailsActivity.put(mDocOrder, bundle);
        OutDocDetailsActivity.putLoadUrl(UrlApi.sales_order_details, bundle);

        iToAction.startActivity(OutDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (OutOrderDetails) d;
        Bundle bundle = new Bundle();
        OutSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(EnterSnListActivity.class, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeGoodsSn(OutOrderDetails outOrderDetails) {
        mToChangeGoods.setSnList(outOrderDetails.getSnList());
        mToChangeGoods.setNowQty(outOrderDetails.getNowQty());
        mToChangeGoods.setLabels(null);
        mDocAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
