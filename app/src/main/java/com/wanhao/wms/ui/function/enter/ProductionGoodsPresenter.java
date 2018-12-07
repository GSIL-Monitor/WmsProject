package com.wanhao.wms.ui.function.enter;

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
import com.wanhao.wms.R;
import com.wanhao.wms.bean.EnterStrGoodsSubParams;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.PurchaseOrder;
import com.wanhao.wms.bean.PurchaseOrderDetails;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;
import com.wanhao.wms.http.BaseResultCallback;
import com.wanhao.wms.http.DecodeCallback;
import com.wanhao.wms.http.DecodeHelper;
import com.wanhao.wms.http.OkHttpHeader;
import com.wanhao.wms.info.UrlApi;
import com.wanhao.wms.ui.adapter.IDoc;
import com.wanhao.wms.ui.function.EnterDocDetailsActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/7
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.production_operate)
public class ProductionGoodsPresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<PurchaseOrderDetails> mGoodsAll;

    public static void putDoc(PurchaseOrder iDoc, Bundle bundle) {
        iDoc.setLabels(null);
        bundle.putString(DOC, C.sGson.toJson(iDoc));
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
            if (mGoodsAll == null) {
                iDialog.displayMessageDialog("初始化异常，请重新操作");
                return;
            }
            boolean isHas = false;
            for (PurchaseOrderDetails d : mGoodsAll) {
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
    private PurchaseOrder mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();

    private void addGoods(PurchaseOrderDetails g, IGoodsDecode goods) {
        boolean add = false;
        for (IDoc iDoc : mGoodsList) {
            PurchaseOrderDetails d = (PurchaseOrderDetails) iDoc;
            if (GoodsUtils.isSame(d, goods)) {
                add = true;
                if (goods.isSerial()) {
                    for (EnterStrGoodsSubParams.Sn sn : d.getSnList()) {
                        if (sn.getSnNo().equals(sn.getSnNo())) {
                            iDialog.displayMessageDialog("序列号不能重复添加!" + sn.getSnNo());
                            return;
                        }
                    }
                    //如果序列号肯定存储，那snList肯定不为kong
                    EnterStrGoodsSubParams.Sn e = new EnterStrGoodsSubParams.Sn();
                    e.setProderId(mDocOrder.getId());
                    d.getSnList().add(e);
                    d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
                    break;
                }
                d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
            }
        }

        if (add) {
            return;
        }
        PurchaseOrderDetails clone = (PurchaseOrderDetails) g.clone();
        clone.setNowQty(goods.getPLN_QTY().intValue());
        mGoodsList.add(0, clone);

        List<EnterStrGoodsSubParams.Sn> snList = clone.getSnList();
        //没有存入序列号
        if (snList == null) {
            clone.setSnList(new ArrayList<EnterStrGoodsSubParams.Sn>());
            EnterStrGoodsSubParams.Sn e = new EnterStrGoodsSubParams.Sn();
            e.setProderId(mDocOrder.getId());
            e.setSnNo(goods.getSN_NO());
            clone.getSnList().add(e);
            clone.setNowQty((int) (clone.getNowQty() + goods.getPLN_QTY()));
        }

        mDocAdapter.notifyDataSetChanged();
    }

    @Override
    public void init(Bundle bundle) {
        super.init(bundle);

        iDialog.displayLoadingDialog("初始化数据");
        mDocOrder = JsonUtils.fromJson(bundle.getString(DOC), PurchaseOrder.class);

        loadDocGoods();
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("asnCode", mDocOrder.getAsnCode());
        OkHttpHeader.post(UrlApi.productionOrderDetails, mParams, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(PurchaseOrderDetails.class);

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
    public void handleClickSearch(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        decode(text.toString());
    }

    @Override
    public void actionDocDetails() {
        Bundle bundle = new Bundle();
        EnterDocDetailsActivity.put(mDocOrder, bundle);
        EnterDocDetailsActivity.putLoadUrl(UrlApi.productionOrderDetails, bundle);
        iToAction.startActivity(EnterDocDetailsActivity.class, bundle);
    }

    @Override
    public void actionSubmit() {
        if (mGoodsList.size() == 0) {
            return;
        }
        iDialog.displayLoadingDialog("提交中");

        List<EnterStrGoodsSubParams> params = new ArrayList<>();
        for (IDoc iDoc : mGoodsList) {
            PurchaseOrderDetails pd = (PurchaseOrderDetails) iDoc;

            EnterStrGoodsSubParams e = new EnterStrGoodsSubParams();
            e.setId((long) pd.getId());
            e.setAsnLineNo(pd.getAsnLineNo());
            e.setAsnCode(pd.getAsnCode());
            e.setLocCode(mRackCode);
            e.setSkuCode(pd.getSkuCode());
            e.setLotNo(pd.getLotNo());
            e.setPlnQty((long) pd.getNowQty());
            e.setSnList(pd.getSnList());
            params.add(e);
        }
        OkHttpHeader.post(UrlApi.productionOrderDetails_submit, params, new BaseResultCallback() {
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
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        PurchaseOrderDetails pd = (PurchaseOrderDetails) mGoodsList.get(position);
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
                                PurchaseOrderDetails iDoc = (PurchaseOrderDetails) mGoodsList.get(position);
                                iDoc.setNowQty(i);
                                iDoc.setLabels(null);
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
}
