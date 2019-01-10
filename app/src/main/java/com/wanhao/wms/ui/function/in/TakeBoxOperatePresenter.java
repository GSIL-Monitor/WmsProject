package com.wanhao.wms.ui.function.in;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.BoxDetails;
import com.wanhao.wms.bean.BoxOrder;
import com.wanhao.wms.bean.ComGoods;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.WarehouseBean;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.adapter.ILabel;
import com.wanhao.wms.ui.adapter.LabelBean;
import com.wanhao.wms.ui.function.PickingBoxDetailsActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.CommGoodsComputer;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/24
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.label_box)
public class TakeBoxOperatePresenter extends DefaultGoodsListPresenter {

    private BoxOrder mBoxOrder;
    private ArrayList<BoxDetails> mBoxGoods;
    private List<IDoc> mPickingInList = new ArrayList();
    private List<IDoc> mPickingOutList = new ArrayList();
    private EditText mDialogInputCountET;
    private PickGoods mOperateInGoods;
    private QMUIDialog mInputQtyDialog;

    private CommGoodsComputer mGoodsComputer = new CommGoodsComputer();
    private BoxDetails mOperateOutGoods;

    public static void put(BoxOrder boxOrder, Bundle bundle) {
        boxOrder.setLabels(null);
        bundle.putString("a", JsonUtils.toJson(boxOrder));
    }

    DecodeCallback callback = new DecodeCallback(MarkRules.GOODS_MARK_CODE) {

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            iDialog.cancelLoadingDialog();
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            super.onGoodsCode(data);
            if (iGoodsListView.getSelectTv1().isSelected()) {
                //装入
                packingIn(data);
            } else {
                //拆出
                packingOut(data);
            }
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

    @Override
    public void setAdapter(RecyclerView mGoodsRv) {
        super.setAdapter(mGoodsRv);
        mDocAdapter.setEmptyView(R.layout.empty_please_add_goods, mGoodsRv);
    }

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iGoodsListView.setSelectGroupVisibility(View.VISIBLE);
        actionClickSelectTv1();

        mBoxOrder = JsonUtils.fromJson(bundle.getString("a"), BoxOrder.class);
        iDialog.displayLoadingDialog("加载数据中");
        iGoodsListView.setRackTextView(mBoxOrder.getPlCode());
        createInputQtyDialog();
        mGoodsComputer.setKeyBindSn(true);
        loadData();
    }

    @Override
    public void decode(String decode) {
        iDialog.displayLoadingDialog("解码中");
        DecodeHelper.decode(decode, callback);
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("plCode", mBoxOrder.getPlCode());
        OkHttpHeader.post(UrlApi.take_box_order_details, map, new BaseResultCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
            }

            @Override

            protected void onResult(BaseResult resultObj, int id) {
                mBoxGoods = resultObj.getList(BoxDetails.class);
                mGoodsComputer.setSrcList(mBoxGoods);
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }

    @Override
    public void handleClickSearch(CharSequence text) {
        super.handleClickSearch(text);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        decode(text.toString());
    }

    @Override
    public void actionDocDetails() {
        super.actionDocDetails();
        PickingBoxDetailsActivity.put(mBoxOrder, getBundle());
        PickingBoxDetailsActivity.putLoadUrl(UrlApi.take_box_order_details, getBundle());
        iToAction.startActivity(PickingBoxDetailsActivity.class, getBundle());
    }

    @Override
    public void actionClickSelectTv1() {
        super.actionClickSelectTv1();
        //装箱
        iGoodsListView.getSelectTv2().setSelected(false);
        iGoodsListView.getSelectTv1().setSelected(true);
        mDocAdapter.setNewData(mPickingInList);
        mDocAdapter.notifyDataSetChanged();
    }

    @Override
    public void actionClickSelectTv2() {
        super.actionClickSelectTv2();
        //拆箱
        iGoodsListView.getSelectTv2().setSelected(true);
        iGoodsListView.getSelectTv1().setSelected(false);
        mDocAdapter.setNewData(mPickingOutList);
        mDocAdapter.notifyDataSetChanged();
    }

    private void packingOut(IGoodsDecode data) {
        if (mBoxGoods == null || mBoxGoods.size() == 0) {
            iDialog.displayMessageDialog("当前箱子无货品");
            return;
        }

        if (mBoxGoods == null) {
            iDialog.displayMessageDialog(R.string.init_error);
            return;
        }
        ComGoods goods = mGoodsComputer.getGoods(data);
        if (goods == null) {
            iDialog.displayMessageDialog(R.string.rack_goods_not_same);
            return;
        }
        double canAddQty = mGoodsComputer.getCanAddQty(data);


        if (canAddQty <= 0) {
            BoxDetails o = (BoxDetails) goods.getListKey().get(0);
            if (TextUtils.isEmpty(o.getSnNo())) {

                iDialog.displayMessageDialog("超出可添加数量 sku:" + data.getSKU_CODE() + ",数量:" + data.getPLN_QTY());
                return;
            }
            iDialog.displayMessageDialog("序列号不能重复添加");
            return;
        }
        addGoods((DecodeBean) data, goods, canAddQty, "");


    }

    private void addGoods(DecodeBean data, ComGoods goods, double canAddQty, String targetRack) {
        BoxDetails saveGoods = isSaveGoods(data, targetRack);
        if (saveGoods == null) {
            BoxDetails clone = (BoxDetails) ((BoxDetails) goods.getListKey().get(0)).clone();
            clone.setTargetRack(targetRack);
            Double pln_qty = data.getPLN_QTY();
            double addTotal;
            if (canAddQty < pln_qty) {
                addTotal = canAddQty;
            } else {
                addTotal = pln_qty.doubleValue();
            }
            data.setPLN_QTY(addTotal);
            mGoodsComputer.addGoods(data);
            clone.setNowQty(addTotal);
            clone.setOperate(true);
            clone.setPlnQty(goods.getTotal());

            mPickingOutList.add(0, clone);

            mDocAdapter.notifyDataSetChanged();
            return;
        }


        try {
            Double pln_qty = data.getPLN_QTY();
            double addQty;
            if (canAddQty < pln_qty) {
                addQty = canAddQty;
            } else {
                addQty = pln_qty.doubleValue();
            }
            data.setPLN_QTY(addQty);
            mGoodsComputer.addGoods(data);
            saveGoods.setNowQty(saveGoods.getNowQty() + addQty);
            saveGoods.setLabels(null);

        } finally {
            mDocAdapter.notifyDataSetChanged();
        }
    }

    private BoxDetails isSaveGoods(IGoodsDecode data, String rack) {
        //是否已经存在显示货品货品
        for (IDoc iDoc : mPickingOutList) {
            BoxDetails d = (BoxDetails) iDoc;
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

    private void packingIn(IGoodsDecode data) {
        for (IDoc o : mPickingInList) {
            PickGoods pg = (PickGoods) o;
            IGoodsDecode decodeBean = pg.getDecodeBean();
            if (checkGoods(decodeBean, data)) {
                if (TextUtils.isEmpty(decodeBean.getSN_NO())) {
                    pg.setNowQty(pg.getNowQty() + data.getPLN_QTY());
                    pg.setLabels(null);
                    mDocAdapter.notifyDataSetChanged();
                    return;
                } else {
                    if (decodeBean.getSN_NO().equals(data.getSN_NO())) {
                        iDialog.displayMessageDialog("序列号不能重复添加");
                        return;
                    }

                }
            }
        }
        mPickingInList.add(new PickGoods(data, data.getPLN_QTY()));
        mDocAdapter.notifyDataSetChanged();
    }


    private boolean checkGoods(IGoodsDecode decodeBean, IGoodsDecode data) {
        if (TextUtils.isEmpty(decodeBean.getLOT_NO())) {
            return decodeBean.getSKU_CODE().equals(data.getSKU_CODE());
        }
        return decodeBean.getLOT_NO().equals(data.getLOT_NO()) && decodeBean.getSKU_CODE().equals(data.getSKU_CODE());
    }

    @Override
    public void actionSubmit() {
        super.actionSubmit();
        if (iGoodsListView.getSelectTv1().isSelected()) {
            //装箱提交
            actionPickingInSubmit();
        } else {
            //拆箱提交
            actionPickingOutSubmit();
        }
    }

    /**
     * 拆箱提交
     */
    private void actionPickingOutSubmit() {
        if (mPickingOutList.size() == 0) {
            iDialog.displayMessageDialog(R.string.please_add_goods);
            return;
        }
        iDialog.displayLoadingDialog("提交中..");
        List<Object> params = new ArrayList();
        Ok:
        for (IDoc iDoc : mPickingOutList) {
            BoxDetails pds = (BoxDetails) iDoc;
            BoxDetails pd = (BoxDetails) pds.clone();

            List goodsKey = mGoodsComputer.getGoodsKey(pd);
            double nowQty = pd.getNowQty();
            for (Object o : goodsKey) {
                Map map = new HashMap();
                BoxDetails d = (BoxDetails) o;
                double opQty = d.getPlnQty();
                if (nowQty > opQty) {
                    map.put("id", d.getId());
                    map.put("plnQty", opQty);
                    params.add(map);
                    nowQty = nowQty - opQty;
                    continue;
                }


                map.put("id", d.getId());
                map.put("plnQty", nowQty);
                params.add(map);
                continue Ok;
            }

        }


        OkHttpHeader.post(UrlApi.take_box_order_pack_out, params, new BaseResultCallback() {
            @Override
            protected void onResult(BaseResult resultObj, int id) {
                if (!resultObj.isRs()) {
                    iDialog.displayMessageDialog(resultObj.getMessage());
                    return;
                }
                iDialog.displayMessageDialog("提交成功");
                mPickingOutList.clear();
                mDocAdapter.notifyDataSetChanged();
                loadData();
            }


            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
            }
        });

    }

    /**
     * 装箱提交
     */
    private void actionPickingInSubmit() {
        if (mPickingInList.size() == 0) {
            iDialog.displayMessageDialog("请添加货品");
            return;
        }
        iDialog.displayLoadingDialog("提交数据中");
        List<Object> list = new ArrayList();
        for (IDoc iDoc : mPickingInList) {
            PickGoods pg = (PickGoods) iDoc;
            IGoodsDecode decodeBean = pg.getDecodeBean();
            Map map = new HashMap();
            map.put("plCode", mBoxOrder.getPlCode());
            map.put("whCode", WarehouseBean.getSelectWarehouse().getWhCode());
            map.put("skuCode", decodeBean.getSKU_CODE());
            map.put("lotNo", decodeBean.getLOT_NO());
            map.put("snNo", decodeBean.getSN_NO());
            map.put("plnQty", pg.getNowQty());
            list.add(map);
        }

        OkHttpHeader.post(UrlApi.take_box_order_pack_in, list, new BaseResultCallback() {
            @Override
            protected void onResult(BaseResult resultObj, int id) {
                if (!resultObj.isRs()) {
                    iDialog.displayMessageDialog(resultObj.getMessage());
                    return;
                }
                iDialog.displayMessageDialog("提交成功");
                mPickingInList.clear();
                mDocAdapter.notifyDataSetChanged();
                loadData();
            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (iGoodsListView.getSelectTv1().isSelected()) {
            //装入
            mOperateInGoods = (PickGoods) mPickingInList.get(position);
            mDialogInputCountET.setText(String.valueOf(mOperateInGoods.getNowQty()));
            mInputQtyDialog.show();
            mOperateInGoods.setLabels(null);
        } else {
            //拆出

            mOperateOutGoods = (BoxDetails) mPickingOutList.get(position);
            mDialogInputCountET.setText(String.valueOf(mOperateOutGoods.getNowQty()));
            mInputQtyDialog.show();
            mOperateOutGoods.setLabels(null);
        }

    }

    private void createInputQtyDialog() {
        QMUIDialog.EditTextDialogBuilder d = new QMUIDialog.EditTextDialogBuilder(iGoodsListView.getSelectTv1().getContext())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        dialog.cancel();

                        if (iGoodsListView.getSelectTv1().isSelected()) {
                            //装入
                            dialog.cancel();
                            String s = mDialogInputCountET.getText().toString();
                            if (TextUtils.isEmpty(s.trim())) {
                                mPickingInList.remove(mOperateInGoods);
                            } else {
                                double v = Double.parseDouble(s);
                                if (v == 0) {
                                    mPickingInList.remove(mOperateInGoods);
                                } else if (v < 0) {
                                    iDialog.displayMessageDialog("请输入正确数量");
                                    return;
                                } else if (mOperateInGoods.getDecodeBean().isSerial()) {
                                    iDialog.displayMessageDialog("序列号管理商品，输入0可删除商品");
                                    return;
                                } else {
                                    mOperateInGoods.setNowQty(v);
                                }
                            }
                        } else {
                            String s = mDialogInputCountET.getText().toString();
                            if (TextUtils.isEmpty(s.trim())) {
                                mPickingOutList.remove(mOperateOutGoods);
                            } else {
                                double v = Double.parseDouble(s);
                                if (v == 0) {
                                    ComGoods goods = mGoodsComputer.getGoods(mOperateOutGoods);
                                    goods.setNowQty(goods.getNowQty() - mOperateOutGoods.getNowQty());
                                    mPickingOutList.remove(mOperateOutGoods);
                                } else if (v < 0) {
                                    iDialog.displayMessageDialog("请输入正确数量");
                                    return;
                                } else if (mOperateOutGoods.isSerial()) {
                                    iDialog.displayMessageDialog("序列号管理商品，输入0可删除商品");
                                    return;
                                } else {
                                    ComGoods goods = mGoodsComputer.getGoods(mOperateOutGoods);
                                    double plnQty = goods.getTotal();
                                    if (v > plnQty) {
                                        iDialog.displayMessageDialog("超出箱内该商品数量，修改失败");
                                        return;
                                    }

                                    goods.setNowQty(goods.getNowQty() - mOperateOutGoods.getNowQty() + v);
                                    mOperateOutGoods.setLabels(null);
                                    mOperateOutGoods.setNowQty(v);
                                }
                            }
                        }
                        mDocAdapter.notifyDataSetChanged();
                    }
                }).addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.cancel();
                    }
                });

        d.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mDialogInputCountET = d.getEditText();
        mInputQtyDialog = d.create();
    }

    public static class PickGoods implements IDoc {
        private IGoodsDecode decodeBean;
        private List<ILabel> labels;
        private double nowQty;
        private BoxDetails boxDetails;


        public PickGoods(IGoodsDecode decodeBean, double nowQty) {
            this.decodeBean = decodeBean;
            this.nowQty = nowQty;
        }


        public BoxDetails getBoxDetails() {
            return boxDetails;
        }

        public void setBoxDetails(BoxDetails boxDetails) {
            this.boxDetails = boxDetails;
        }

        public double getNowQty() {
            return nowQty;
        }

        public void setNowQty(double nowQty) {
            this.nowQty = nowQty;
        }

        public IGoodsDecode getDecodeBean() {
            return decodeBean;
        }

        public void setDecodeBean(IGoodsDecode decodeBean) {
            this.decodeBean = decodeBean;
        }

        public List<ILabel> getLabels() {
            return labels;
        }

        public void setLabels(List<ILabel> labels) {
            this.labels = labels;
        }

        @Override
        public CharSequence getDocNoLabel() {
            return MyApp.getContext().getString(R.string.label_sku_code);
        }

        @Override
        public CharSequence getDocNo() {
            return decodeBean.getSKU_CODE();
        }

        @Override
        public List<ILabel> getTables() {
            if (labels != null) {
                return labels;
            }
            labels = new ArrayList<>();
            labels.add(new LabelBean(R.string.lotNo, decodeBean.getLOT_NO(), 6));
            if (!TextUtils.isEmpty(decodeBean.getSN_NO())) {
                labels.add(new LabelBean(R.string.label_sn, decodeBean.getSN_NO(), 6));
            }
            labels.add(new LabelBean(R.string.nowQty, nowQty + ""));
            return labels;
        }

        @Override
        public boolean isShowSn() {
            return false;
        }
    }
}
