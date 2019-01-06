package com.wanhao.wms.ui.function.in;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private PickGoods mOperateGoods;
    private QMUIDialog mInputQtyDialog;

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
        for (BoxDetails boxGoods : mBoxGoods) {
            if (GoodsUtils.isSame(boxGoods, data)) {
                String snNo = boxGoods.getSnNo();
                if (TextUtils.isEmpty(snNo)) {
                    addPickingOutGoods(boxGoods, data);
                    return;
                } else {
                    //是序列号管理
                    String sn_no = data.getSN_NO();
                    if (TextUtils.isEmpty(sn_no)) {
                        iDialog.displayMessageDialog("商品未包含序列号，请输入正确的货品");
                        return;
                    }
                    if (snNo.equals(sn_no)) {
                        iDialog.displayMessageDialog("序列号不能重复添加");
                        return;
                    }
                    if (snNo.equals(sn_no)) {
                        packingOutAddSn(boxGoods, data);
                        return;
                    }

                }
            }
        }
        iDialog.displayMessageDialog("箱内无该货品  \r\nsku:" + data.getSKU_CODE() + "\r\n lotNo:" + data.getLOT_NO());


    }

    /**
     * 拆箱 有序列号添加
     *
     * @param boxGoods
     * @param data
     */
    private void packingOutAddSn(BoxDetails boxGoods, IGoodsDecode data) {
        PickGoods e = new PickGoods(data, data.getPLN_QTY());
        e.setBoxDetails(boxGoods);
        e.setLabels(null);
        mPickingOutList.add(e);
        mDocAdapter.notifyDataSetChanged();
    }


    /**
     * 拆箱 无序列号添加
     *
     * @param boxGoods
     * @param data
     */
    private void addPickingOutGoods(BoxDetails boxGoods, IGoodsDecode data) {
        boolean add = false;
        for (IDoc o : mPickingOutList) {
            PickGoods pg = (PickGoods) o;
            IGoodsDecode decodeBean = pg.getDecodeBean();
            if (checkGoods(decodeBean, data)) {
                double nowQty = pg.getNowQty() + data.getPLN_QTY();
                if (nowQty > boxGoods.getPlnQty()) {
                    iDialog.displayMessageDialog("箱内库存不足");
                    return;
                }

                pg.setNowQty(nowQty);
                pg.setLabels(null);
                add = true;
                break;

            }
        }

        if (add) {
            mDocAdapter.notifyDataSetChanged();
            return;
        }
        double nowQty = data.getPLN_QTY();
        if (nowQty > boxGoods.getPlnQty()) {
            iDialog.displayMessageDialog("箱内库存不足");
            return;
        }
        PickGoods e = new PickGoods(data, data.getPLN_QTY());
        e.setBoxDetails(boxGoods);
        mPickingOutList.add(e);
        mDocAdapter.notifyDataSetChanged();
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
            iDialog.displayMessageDialog("请添加货品");
            return;
        }
        iDialog.displayLoadingDialog("提交中..");
        List<Object> list = new ArrayList();
        for (IDoc iDoc : mPickingOutList) {
            PickGoods pg = (PickGoods) iDoc;
            BoxDetails boxDetails = pg.getBoxDetails();
            Map map = new HashMap();
            map.put("id", boxDetails.getId());
            map.put("plnQty", pg.getNowQty());
            list.add(map);
        }

        OkHttpHeader.post(UrlApi.take_box_order_pack_out, list, new BaseResultCallback() {
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
            mOperateGoods = (PickGoods) mPickingInList.get(position);
            mDialogInputCountET.setText(String.valueOf(mOperateGoods.getNowQty()));
            mInputQtyDialog.show();


        } else {
            //拆出
            mOperateGoods = (PickGoods) mPickingOutList.get(position);
            mDialogInputCountET.setText(String.valueOf(mOperateGoods.getNowQty()));
            mInputQtyDialog.show();
        }
    }

    private void createInputQtyDialog() {
        QMUIDialog.EditTextDialogBuilder d = new QMUIDialog.EditTextDialogBuilder(iGoodsListView.getSelectTv1().getContext())
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        dialog.cancel();

                        if (iGoodsListView.getSelectTv1().isSelected()) {
                            dialog.cancel();
                            mOperateGoods.setLabels(new ArrayList<ILabel>());
                            String s = mDialogInputCountET.getText().toString();
                            if (TextUtils.isEmpty(s.trim())) {
                                mPickingInList.remove(mOperateGoods);
                            } else {
                                double v = Double.parseDouble(s);
                                if (v == 0) {
                                    mPickingInList.remove(mOperateGoods);
                                } else if (v < 0) {
                                    iDialog.displayMessageDialog("请输入正确数量");
                                    return;
                                } else if (mOperateGoods.getDecodeBean().isSerial()) {
                                    iDialog.displayMessageDialog("序列号管理商品，输入0可删除商品");
                                    return;
                                } else {
                                    mOperateGoods.setNowQty(v);
                                }
                            }
                        } else {
                            mOperateGoods.setLabels(new ArrayList<ILabel>());
                            String s = mDialogInputCountET.getText().toString();
                            if (TextUtils.isEmpty(s.trim())) {
                                mPickingInList.remove(mOperateGoods);
                            } else {
                                double v = Double.parseDouble(s);
                                if (v == 0) {
                                    mPickingInList.remove(mOperateGoods);
                                } else if (v < 0) {
                                    iDialog.displayMessageDialog("请输入正确数量");
                                    return;
                                } else if (mOperateGoods.getDecodeBean().isSerial()) {
                                    iDialog.displayMessageDialog("序列号管理商品，输入0可删除商品");
                                    return;
                                } else {
                                    double plnQty = mOperateGoods.getBoxDetails().getPlnQty();
                                    if (v > plnQty) {
                                        iDialog.displayMessageDialog("超出箱内该商品数量，修改失败");
                                        return;
                                    }
                                    mOperateGoods.setNowQty(v);
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
