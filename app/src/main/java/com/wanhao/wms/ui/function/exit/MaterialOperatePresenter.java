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
import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.PickingOrderDetails;
import com.wanhao.wms.bean.PickingOrderDetails;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.OutGoodsSubParams;
import com.wanhao.wms.bean.PickingOrder;
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
import com.wanhao.wms.ui.function.PickingDocDetailsActivity;
import com.wanhao.wms.ui.function.PickingSnListActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
import com.wanhao.wms.utils.CommGoodsBindRackComputer;
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
 * 创建时间 2018/12/18
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.material_exit)
public class MaterialOperatePresenter extends DefaultGoodsListPresenter {

    public static final String DOC = "doc";
    private ArrayList<PickingOrderDetails> mGoodsAll;
    private PickingOrderDetails mToChangeGoods;

    private CommGoodsBindRackComputer mGoodsComputer = new CommGoodsBindRackComputer();


    public static void putDoc(PickingOrder iDoc, Bundle bundle) {
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
            mRackCode = data.getDOC_CODE();
            iGoodsListView.setRackTextView(mRackCode);
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            if (mGoodsAll == null) {
                iDialog.displayMessageDialog(R.string.init_error);
                return;
            }
            if (TextUtils.isEmpty(mRackCode)) {
                iDialog.displayMessageDialog(R.string.please_rack_scanning);
                return;
            }
            ComGoods goods = mGoodsComputer.getGoods(data, mRackCode);
            if (goods == null) {
                iDialog.displayMessageDialog(R.string.rack_goods_not_same);
                return;
            }
            double canAddQty = mGoodsComputer.getCanAddQty(data, mRackCode);
            if (canAddQty <= 0) {
                iDialog.displayMessageDialog("超出可添加数量 sku:" + data.getSKU_CODE() + ",数量:" + data.getPLN_QTY());
                return;
            }
            addGoods((DecodeBean) data, goods, canAddQty, mRackCode);

        }

        @Override
        public void onOtherCode(DecodeBean data) {
            iDialog.displayMessageDialog(R.string.decode_other);
        }

        @Override
        protected void onFailed(String error, int code) {
            iDialog.displayMessageDialog(error);
        }
    };

    private void addGoods(DecodeBean data, ComGoods goods, double canAddQty, String targetRack) {
        PickingOrderDetails saveGoods = isSaveGoods(data, targetRack);

        if (saveGoods == null) {
            PickingOrderDetails clone = (PickingOrderDetails) ((PickingOrderDetails) goods.getListKey().get(0)).clone();
            clone.setTargetRack(targetRack);
            if (clone.isSerial() && !clone.isAutoSn()) {
                List<Sn> snList = clone.getSnList();
                //没有存入序列号
                if (snList == null) {
                    clone.setSnList(new ArrayList<Sn>());
                    Sn e = new Sn();
                    e.setPorderId(clone.getId());
                    e.setSnNo(data.getSN_NO());
                    clone.getSnList().add(e);
                    clone.setNowQty((int) (clone.getNowQty() + data.getPLN_QTY()));
                }
            } else {
                Double pln_qty = data.getPLN_QTY();
                double addTotal;
                double addQty;
                if (canAddQty < pln_qty) {
                    addQty = canAddQty;
                } else {
                    addQty = pln_qty.doubleValue();
                }
                addTotal = addQty;
                data.setPLN_QTY(addQty);
                mGoodsComputer.addGoods(data, targetRack);
                clone.setNowQty(addTotal);
            }

            mGoodsList.add(0, clone);

            mDocAdapter.notifyDataSetChanged();
            return;
        }


        try {
            //序列号管理
            if (saveGoods.isSerial() && !saveGoods.isAutoSn()) {
                for (Sn sn : saveGoods.getSnList()) {
                    if (sn.getSnNo().equals(data.getSN_NO())) {
                        iDialog.displayMessageDialog("序列号不能重复添加!" + sn.getSnNo());
                        return;
                    }
                }

                //如果序列号肯定存储，那snList肯定不为kong
                Sn e = new Sn();
                e.setPorderId(saveGoods.getId());
                e.setSnNo(data.getSN_NO());
                saveGoods.getSnList().add(e);
                saveGoods.setNowQty((saveGoods.getNowQty() + data.getPLN_QTY().intValue()));
                mGoodsComputer.addGoods(data, targetRack);
                saveGoods.setLabels(null);
                return;
            }
            //
            Double pln_qty = data.getPLN_QTY();
            double addTotal;
            if (canAddQty < pln_qty) {
                addTotal = canAddQty;
            } else {
                addTotal = pln_qty.doubleValue();
            }
            data.setPLN_QTY(addTotal);
            mGoodsComputer.addGoods(data, targetRack);
            saveGoods.setNowQty(saveGoods.getNowQty() + addTotal);
            saveGoods.setLabels(null);

        } finally {
            mDocAdapter.notifyDataSetChanged();
        }
    }

    private PickingOrderDetails isSaveGoods(IGoodsDecode data, String rack) {
        //是否已经存在显示货品货品
        for (IDoc iDoc : mGoodsList) {
            PickingOrderDetails d = (PickingOrderDetails) iDoc;
            if (GoodsUtils.isSame(d, data)) {
                if (d.getTargetRack() == null) {
                    return d;
                }
                if (d.getTargetRack().equals(rack)) {
                    return d;
                }
            }
        }
        return null;
    }


    private PickingOrder mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();


    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iDialog.displayLoadingDialog("初始化数据");
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, PickingOrder.class);
        mDocAdapter.setNewData(mGoodsList);
        loadDocGoods();

        EventBus.getDefault().register(this);
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("pickCode", mDocOrder.getPickCode());
        OkHttpHeader.post(UrlApi.picking_list_details, mParams, new BaseResultCallback() {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();

            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(PickingOrderDetails.class);
                mGoodsComputer.setSrcList(mGoodsAll);
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        PickingOrderDetails o = (PickingOrderDetails) adapter.getData().get(position);
        if (o.isAutoSn()) {
            return;
        }
        super.onItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        PickingOrderDetails pd = (PickingOrderDetails) mGoodsList.get(position);
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
                            double i = Double.parseDouble(s);
                            PickingOrderDetails iDoc = (PickingOrderDetails) mGoodsList.get(position);
                            ComGoods goods = mGoodsComputer.getGoods(iDoc, iDoc.getLocCode());
                            if (i == 0) {
                                mGoodsList.remove(position);
                                goods.setNowQty(goods.getNowQty() - iDoc.getNowQty());
                            } else {

                                if (goods.getTotal() >= i) {
                                    iDoc.setLabels(null);
                                    iDoc.setNowQty(i);
                                    goods.setNowQty(i);
                                } else {
                                    iDialog.displayMessageDialog("不可大于可添加数量");
                                }
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
        if (mGoodsList.size() == 0) {            iDialog.displayMessageDialog(R.string.please_add_goods);
            return;
        }
        iDialog.displayLoadingDialog("提交中");

        List<Object> params = new ArrayList<>();
        Ok:
        for (IDoc iDoc : mGoodsList) {
            PickingOrderDetails pds = (PickingOrderDetails) iDoc;
            PickingOrderDetails pd = (PickingOrderDetails) pds.clone();
            if (pd.isSerial()) {
                if (pd.getNowQty() < pd.getOpQty()) {
                    iDialog.displayMessageDialog("序列号必须全部提交，否则不可以进行提交操作!!!");
                    iDialog.cancelLoadingDialog();
                    return;
                }
            }
            ComGoods goods = mGoodsComputer.getGoods(pd, pd.getTargetRack());
            if (goods.getTotal() != goods.getNowQty()) {
                iDialog.displayMessageDialog("未全部出库");
                iDialog.cancelTipDialogSuccess();
                return;
            }

            List goodsKey = mGoodsComputer.getGoodsKey(pd, pd.getTargetRack());
            double nowQty = pd.getNowQty();
            for (Object o : goodsKey) {
                PickingOrderDetails d = (PickingOrderDetails) o;
                double opQty = d.getOpQty();
                Map<String, Object> p = new HashMap<>();
                p.put("id", d.getId());
                p.put("pickCode", d.getPickCode());
                p.put("pickLineNo", d.getPickLineNo());
                p.put("soDId", d.getSoDid());
                p.put("soLineNo", d.getSoLineNo());
                p.put("soCode", d.getSoCode());
                p.put("docType", d.getDocType());
                p.put("skuCode", pd.getSkuCode());
                p.put("lotNo", pd.getLotNo());


                params.add(p);

                if (nowQty > opQty) {
                    List<Sn> snList = pd.getSnList();
                    if (snList != null && snList.size() == (int) opQty) {
                        List<Sn> sns = snList.subList(0, (int) opQty);
                        p.put("snList", sns);
                        pd.setSnList(snList.subList((int) opQty - 1, snList.size()));
                    }


                    p.put("pqty", opQty);
                    params.add(p);

                    nowQty = nowQty - opQty;
                    continue;
                }
                p.put("pqty", nowQty);
                p.put("snList", pd.getSnList());
                params.add(p);
                continue Ok;
            }

        }
        OkHttpHeader.post(UrlApi.materials_cancel_submit, params, new BaseResultCallback() {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
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
        PickingDocDetailsActivity.put(mDocOrder, bundle);
        PickingDocDetailsActivity.putLoadUrl(UrlApi.picking_list_details, bundle);
        iToAction.startActivity(PickingDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (PickingOrderDetails) d;
        Bundle bundle = new Bundle();
        PickingSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(PickingSnListActivity.class, bundle);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeGoodsSn(PickingOrderDetails details) {

        double v = details.getSnList().size() - mToChangeGoods.getNowQty();
        if (details.getSnList().size() == 0) {
            mGoodsList.remove(mToChangeGoods);
        }
        ComGoods goods = mGoodsComputer.getGoods(mToChangeGoods, mToChangeGoods.getTargetRack());
        goods.setNowQty(goods.getNowQty() - v);
        mToChangeGoods.setNowQty(details.getNowQty());
        mToChangeGoods.setSnList(details.getSnList());
        mToChangeGoods.setLabels(null);
        mDocAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
