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
import com.wanhao.wms.C;
import com.wanhao.wms.MyApp;
import com.wanhao.wms.R;
import com.wanhao.wms.bean.CheckQtyDoc;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.Sn;
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
import com.wanhao.wms.ui.function.CheckQtyDocDetailsActivity;
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
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/28
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.check_qty)
public class CheckQtyOperatePresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<CheckQtyGoodsBean> mGoodsAll;
    private List<CheckQtyGoodsBean> mTakeGoodsList = new ArrayList<>();

    public static void putDoc(CheckQtyDoc iDoc, Bundle bundle) {
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
            if (TextUtils.isEmpty(mTargetRack)) {
                iDialog.displayMessageDialog(R.string.decode_error);
                return;
            }
            iGoodsListView.setRackTextView(mTargetRack);
        }

        @Override
        public void onGoodsCode(IGoodsDecode data) {
            if (TextUtils.isEmpty(mTargetRack)) {
                iDialog.displayMessageDialog(R.string.please_rack_scanning);
                return;
            }
            if (data == null || data.getSKU_CODE() == null) {
                iDialog.displayMessageDialog(R.string.decode_error);
                return;
            }
            addGoods(data, mTargetRack);
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

    private void addGoods(IGoodsDecode data, String targetRack) {
        if (!TextUtils.isEmpty(data.getSN_NO())) {
            //序列号管理
            CheckQtyGoodsBean e = new CheckQtyGoodsBean();
            e.setiGoodsDecode(data);
            e.setTargetRack(targetRack);
            e.setNowQty(data.getPLN_QTY());
            e.setLabels(null);
            mTakeGoodsList.add(e);
            mDocAdapter.notifyDataSetChanged();
            return;
        }
        for (CheckQtyGoodsBean checkQtyGoodsBean : mTakeGoodsList) {
            if (isSame(checkQtyGoodsBean, data) && checkQtyGoodsBean.getTargetRack().equals(targetRack)) {
                checkQtyGoodsBean.setNowQty(checkQtyGoodsBean.getNowQty() + data.getPLN_QTY());
                checkQtyGoodsBean.setLabels(null);
                mDocAdapter.notifyDataSetChanged();
                return;
            }
        }

        //添加货品
        CheckQtyGoodsBean e = new CheckQtyGoodsBean();
        e.setiGoodsDecode(data);
        e.setNowQty(data.getPLN_QTY());
        e.setTargetRack(targetRack);
        mTakeGoodsList.add(e);
        mDocAdapter.notifyDataSetChanged();
    }

    private boolean isSame(CheckQtyGoodsBean checkQtyGoodsBean, IGoodsDecode data) {
        IGoodsDecode iGoodsDecode = checkQtyGoodsBean.getiGoodsDecode();
        String lot_no = iGoodsDecode.getLOT_NO();
        if (TextUtils.isEmpty(lot_no)) {
            return iGoodsDecode.getSKU_CODE().equals(data.getSKU_CODE());
        }
        return lot_no.equals(data.getLOT_NO()) && iGoodsDecode.getSKU_CODE().equals(data.getSKU_CODE());
    }

    private CheckQtyDoc mDocOrder;
    private String mTargetRack;

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);
        iDialog.displayLoadingDialog("初始化数据");
        EventBus.getDefault().register(this);
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, CheckQtyDoc.class);
        List list = mTakeGoodsList;
        mDocAdapter.setNewData(list);
        loadDocGoods();
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("inventoryCode", mDocOrder.getInventoryCode());
        OkHttpHeader.post(UrlApi.check_qty_order_details, mParams, new BaseResultCallback() {

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                iDialog.cancelLoadingDialog();

            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(CheckQtyGoodsBean.class);

            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//        super.onItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        CheckQtyGoodsBean pd = (CheckQtyGoodsBean) mTakeGoodsList.get(position);
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
                            mTakeGoodsList.remove(position);
                        } else {
                            int i = Integer.parseInt(s);
                            if (i == 0) {
                                mTakeGoodsList.remove(position);
                            } else {

                                CheckQtyGoodsBean iDoc = (CheckQtyGoodsBean) mTakeGoodsList.get(position);
                                if (iDoc.isSn()) {
                                    iDialog.displayMessageDialog("输入0即可删除序列号,不可编辑其他数量");
                                } else {
                                    iDoc.setLabels(null);
                                    iDoc.setNowQty(i);
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
        if (mTakeGoodsList.size() == 0) {
            iDialog.displayMessageDialog("请添加货品");
            return;
        }
        String whCode = WarehouseBean.getSelectWarehouse().getWhCode();
        iDialog.displayLoadingDialog("提交中");
        List params = new ArrayList<>();
        for (CheckQtyGoodsBean checkQtyGoodsBean : mTakeGoodsList) {
            Map<String, Object> map = new HashMap<>();
            IGoodsDecode iGoodsDecode = checkQtyGoodsBean.getiGoodsDecode();
            map.put("id", mDocOrder.getId());
            map.put("inventoryCode", mDocOrder.getInventoryCode());
            map.put("skuCode", iGoodsDecode.getSKU_CODE());
            map.put("whCode", whCode);
            map.put("locCode", checkQtyGoodsBean.getTargetRack());
            map.put("actQty", checkQtyGoodsBean.getNowQty());
            map.put("lotNo", iGoodsDecode.getLOT_NO());
//            map.put("diffQty", pd.getNowQty());
            if (iGoodsDecode.isSerial()) {
                List<Sn> snList = new ArrayList<>();
                String sn_no = iGoodsDecode.getSN_NO();
                Sn e = new Sn();
                e.setSnNo(sn_no);
                e.setPorderId(mDocOrder.getId());
                snList.add(e);
                map.put("snList", snList);
            }

            params.add(map);
        }
        OkHttpHeader.post(UrlApi.check_qty_order_submit, params, new BaseResultCallback() {

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                iDialog.cancelLoadingDialog();
                if (!resultObj.isRs()) {
                    onFailed(resultObj.getMessage(), id);
                    return;
                }
                EventBus.getDefault().post(0);
                iDialog.displayTipDialogSuccess("提交成功");
                C.sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iDialog.cancelTipDialogSuccess();
                        iToAction.activityFinish();
                    }
                }, 3000);

            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.cancelLoadingDialog();

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
        CheckQtyDocDetailsActivity.put(mDocOrder, bundle);
        // TODO: 2018/12/29 未制作完成
        CheckQtyDocDetailsActivity.putLoadUrl(UrlApi.check_qty_order_details, bundle);
        iToAction.startActivity(CheckQtyDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
//        mToChangeGoods = (CheckQtyGoodsBean) d;
//        Bundle bundle = new Bundle();
//        RackSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
//        iToAction.startActivity(RackSnListActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeGoods(CheckQtyGoodsBean pd) {
//        if (pd.getSnList().size() == 0) {
//            mGoodsList.remove(mToChangeGoods);
//        }
//        mToChangeGoods.setSnList(pd.getSnList());
//        mToChangeGoods.setNowQty(pd.getSnList().size());
//        mToChangeGoods.setLabels(null);
//        mDocAdapter.notifyDataSetChanged();
    }

    public static class CheckQtyGoodsBean implements IDoc {
        private double nowQty;
        private IGoodsDecode iGoodsDecode;

        private CheckQtyGoodsBean operateGoods;

        private List<ILabel> labels;

        private List<Sn> snList;
        private String targetRack;

        public boolean isSn() {
            return !TextUtils.isEmpty(iGoodsDecode.getSN_NO());
        }

        public List<Sn> getSnList() {
            return snList;
        }

        public void setSnList(List<Sn> snList) {
            this.snList = snList;
        }

        public List<ILabel> getLabels() {
            return labels;
        }

        public void setLabels(List<ILabel> labels) {
            this.labels = labels;
        }

        public double getNowQty() {
            return nowQty;
        }

        public void setNowQty(double nowQty) {
            this.nowQty = nowQty;
        }

        public IGoodsDecode getiGoodsDecode() {
            return iGoodsDecode;
        }

        public void setiGoodsDecode(IGoodsDecode iGoodsDecode) {
            this.iGoodsDecode = iGoodsDecode;
        }

        public CheckQtyGoodsBean getOperateGoods() {
            return operateGoods;
        }

        public void setOperateGoods(CheckQtyGoodsBean operateGoods) {
            this.operateGoods = operateGoods;
        }

        @Override
        public CharSequence getDocNoLabel() {
            return MyApp.getContext().getString(R.string.label_sku_code);
        }

        @Override
        public CharSequence getDocNo() {
            return iGoodsDecode.getSKU_CODE();
        }

        @Override
        public List<ILabel> getTables() {
            if (labels != null) {
                return labels;
            }
            labels = new ArrayList<>();
            if (!TextUtils.isEmpty(iGoodsDecode.getSN_NO())) {
                labels.add(new LabelBean(R.string.label_sn, iGoodsDecode.getSN_NO(), 6));
            }

//            if (!TextUtils.isEmpty(iGoodsDecode.getLOT_NO())) {
            String s = iGoodsDecode.getLOT_NO() == null ? "" : iGoodsDecode.getLOT_NO();
            labels.add(new LabelBean(R.string.lotNo, s, 6));
//            }
            labels.add(new LabelBean(R.string.nowQty, String.valueOf(nowQty)));
            return labels;
        }

        @Override
        public boolean isShowSn() {
            return false;
        }

        public String getTargetRack() {
            return targetRack;
        }

        public void setTargetRack(String targetRack) {
            this.targetRack = targetRack;
        }
    }
}


