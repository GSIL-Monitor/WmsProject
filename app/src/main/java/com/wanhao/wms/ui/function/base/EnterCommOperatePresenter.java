package com.wanhao.wms.ui.function.base;

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
import com.wanhao.wms.bean.EnterOrderBean;
import com.wanhao.wms.bean.EnterOrderDetails;
import com.wanhao.wms.bean.EnterStrGoodsSubParams;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.OutOrderDetails;
import com.wanhao.wms.bean.Sn;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.EnterDocDetailsActivity;
import com.wanhao.wms.ui.function.EnterSnListActivity;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
import com.wanhao.wms.utils.CommGoodsBindRackComputer;
import com.wanhao.wms.utils.CommGoodsComputer;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 描述： 入库操作 不进行验证货位。
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/29
 *
 * @author ql
 */
public class EnterCommOperatePresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<EnterOrderDetails> mGoodsAll;
    private EnterOrderDetails mToChangeGoods;

    private EnterCommBean enterCommBean;
    private CommGoodsComputer mGoodsComputer = new CommGoodsComputer();

    public static void putDoc(EnterOrderBean iDoc, Bundle bundle) {
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
            boolean isHas = false;
            ComGoods goods = mGoodsComputer.getGoods(data);
            if (goods == null) {
                iDialog.displayMessageDialog(R.string.rack_goods_not_same);
                return;
            }
            double canAddQty = mGoodsComputer.getCanAddQty(data);
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
        EnterOrderDetails saveGoods = isSaveGoods(data, targetRack);

        if (saveGoods == null) {
            EnterOrderDetails clone = (EnterOrderDetails) ((EnterOrderDetails) goods.getListKey().get(0)).clone();
            clone.setTotalQty(goods.getTotal());
            clone.setTargetRack(targetRack);
            if (clone.isSerial() && !clone.isAutoSerial()) {
                List<Sn> snList = clone.getSnList();
                //没有存入序列号
                if (snList == null) {
                    clone.setSnList(new ArrayList<Sn>());
                    Sn e = new Sn();
                    e.setPorderId(mDocOrder.getId());
                    e.setSnNo(data.getSN_NO());
                    clone.getSnList().add(e);
                    clone.setLotNo(data.getLOT_NO());
                    clone.setSkuCode(data.getSKU_CODE());
                    clone.setTargetRack(targetRack);
                    clone.setNowQty((int) (clone.getNowQty() + data.getPLN_QTY()));
                }
            } else {
                Double pln_qty = data.getPLN_QTY();
                double addTotal;
                if (canAddQty < pln_qty) {
                    addTotal = canAddQty;
                } else {
                    addTotal = pln_qty.doubleValue();
                }
                data.setPLN_QTY(addTotal);
                mGoodsComputer.addGoods(data);
                clone.setLotNo(data.getLOT_NO());
                clone.setSkuCode(data.getSKU_CODE());
                clone.setTargetRack(targetRack);
                clone.setNowQty(addTotal);
            }

            mGoodsList.add(0, clone);

            mDocAdapter.notifyDataSetChanged();
            return;
        }


        try {
            //序列号管理
            if (saveGoods.isSerial() && !saveGoods.isAutoSerial()) {
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
                mGoodsComputer.addGoods(data);
                saveGoods.setLabels(null);
                return;
            }
            //
            Double pln_qty = data.getPLN_QTY();
            double addTotal;
            double addQty;
            if (canAddQty < pln_qty) {
                addQty = canAddQty;
                addTotal = saveGoods.getNowQty() + canAddQty;
            } else {
                addQty = pln_qty.doubleValue();
                addTotal = saveGoods.getNowQty() + pln_qty.doubleValue();
            }
            data.setPLN_QTY(addQty);
            mGoodsComputer.addGoods(data);
            saveGoods.setNowQty(saveGoods.getNowQty() + addTotal);
            saveGoods.setLabels(null);

        } finally {
            mDocAdapter.notifyDataSetChanged();
        }
    }

    private EnterOrderDetails isSaveGoods(IGoodsDecode data, String rack) {
        //是否已经存在显示货品货品
        for (IDoc iDoc : mGoodsList) {
            EnterOrderDetails d = (EnterOrderDetails) iDoc;
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

    private EnterOrderBean mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();


    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        enterCommBean = JsonUtils.fromJson(bundle.getString("enter_comm_bean"), EnterCommBean.class);
        mGoodsComputer.setBindLotNo(enterCommBean.isBindLotNo());
        iGoodsListView.setTopbarTitle(enterCommBean.getTitleRes());
        iDialog.displayLoadingDialog("初始化数据");
        EventBus.getDefault().register(this);
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, EnterOrderBean.class);
        mDocAdapter.setNewData(mGoodsList);
        loadDocGoods();
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("asnCode", mDocOrder.getAsnCode());
        OkHttpHeader.post(enterCommBean.getOrderDetailUrl(), mParams, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(EnterOrderDetails.class);
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
        EnterOrderDetails iDoc = (EnterOrderDetails) mGoodsList.get(position);
        if (iDoc.isAutoSerial()) {
            iDialog.displayMessageDialog("序列号自动生成");
            return;
        }
        super.onItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        EnterOrderDetails pd = (EnterOrderDetails) mGoodsList.get(position);
        if (pd.isSerial() && !pd.isAutoSerial()) {
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
                            EnterOrderDetails iDoc = (EnterOrderDetails) mGoodsList.get(position);
                            ComGoods goods = mGoodsComputer.getGoods(iDoc);

                            if (i == 0) {
                                mGoodsList.remove(position);
                                goods.setNowQty(goods.getNowQty() - iDoc.getNowQty());
                            } else {
                                double total = goods.getTotal();
                                double v = goods.getNowQty() - iDoc.getNowQty();
                                double canSetMaxQty = total - v;//可以填入最大值
                                if (canSetMaxQty >= i) {
                                    iDoc.setLabels(null);
                                    iDoc.setNowQty(i);
                                    goods.setNowQty(i + v);


                                } else {
                                    iDialog.displayMessageDialog("不可大于可添加数量,可填入数量:" + canSetMaxQty);
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
        if (mGoodsList.size() == 0) {
            return;
        }
        iDialog.displayLoadingDialog("提交中");

        List<EnterStrGoodsSubParams> params = new ArrayList<>();
        Ok:
        for (IDoc iDoc : mGoodsList) {
            EnterOrderDetails pds = (EnterOrderDetails) iDoc;
            EnterOrderDetails pd = (EnterOrderDetails) pds.clone();
            if (pd.isSerial()) {
                if (pd.getNowQty() < pd.getOpQty()) {
                    iDialog.displayMessageDialog("序列号必须全部提交，否则不可以进行提交操作!!!");
                    iDialog.cancelLoadingDialog();
                    return;
                }
            }

            List goodsKey = mGoodsComputer.getGoodsKey(pd);
            double nowQty = pd.getNowQty();
            for (Object o : goodsKey) {
                EnterOrderDetails d = (EnterOrderDetails) o;
                double opQty = d.getOpQty();
                EnterStrGoodsSubParams e = new EnterStrGoodsSubParams();
                e.setId((long) d.getId());
                e.setAsnLineNo(d.getAsnLineNo());
                e.setAsnCode(d.getAsnCode());
                e.setLocCode(pd.getTargetRack());
                e.setSkuCode(pd.getSkuCode());
                e.setLotNo(pd.getLotNo());

                if (nowQty > opQty) {
                    List<Sn> snList = pd.getSnList();
                    if (snList != null && snList.size() == (int) opQty) {
                        List<Sn> sns = snList.subList(0, (int) opQty);
                        e.setSnList(sns);
                        pd.setSnList(snList.subList((int) opQty - 1, snList.size()));
                    }


                    e.setPlnQty(opQty);

                    params.add(e);

                    nowQty = nowQty - opQty;
                    continue;
                }
                e.setPlnQty(nowQty);
                e.setSnList(pd.getSnList());
                params.add(e);
                continue Ok;
            }

        }
        OkHttpHeader.post(enterCommBean.getSubmitUrl(), params, new BaseResultCallback() {
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
        EnterDocDetailsActivity.put(mDocOrder, bundle);
        EnterDocDetailsActivity.putLoadUrl(enterCommBean.getOrderDetailUrl(), bundle);

        iToAction.startActivity(EnterDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (EnterOrderDetails) d;
        Bundle bundle = new Bundle();
        EnterSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(EnterSnListActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeGoods(EnterOrderDetails pd) {
        double v = pd.getSnList().size() - mToChangeGoods.getNowQty();
        if (pd.getSnList().size() == 0) {
            mGoodsList.remove(mToChangeGoods);
        }
        ComGoods goods = mGoodsComputer.getGoods(pd);
        goods.setNowQty(goods.getNowQty() - v);
        mToChangeGoods.setSnList(pd.getSnList());
        mToChangeGoods.setNowQty(pd.getSnList().size());

        mToChangeGoods.setLabels(null);
        mDocAdapter.notifyDataSetChanged();
    }
}
