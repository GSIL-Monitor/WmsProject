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
import com.wanhao.wms.R;
import com.wanhao.wms.bean.EnterStrGoodsSubParams;
import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.EnterOrderBean;
import com.wanhao.wms.bean.EnterOrderDetails;
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
import com.wanhao.wms.ui.function.EnterDocDetailsActivity;
import com.wanhao.wms.ui.function.EnterSnListActivity;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.goods.DefaultGoodsListPresenter;
import com.wanhao.wms.utils.ActivityUtils;
import com.wanhao.wms.utils.GoodsUtils;
import com.wanhao.wms.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
@BindPresenter(titleRes = R.string.enter_storage_operate)
public class EnterStorageGoodsPresenter extends DefaultGoodsListPresenter {
    public static final String DOC = "doc";
    private ArrayList<EnterOrderDetails> mGoodsAll;
    private EnterOrderDetails mToChangeGoods;

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
                iDialog.displayMessageDialog("初始化异常，请重新操作");
                return;
            }
            boolean isHas = false;
            for (EnterOrderDetails d : mGoodsAll) {
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
    private EnterOrderBean mDocOrder;
    private List<IDoc> mGoodsList = new ArrayList<>();

    private void addGoods(EnterOrderDetails g, IGoodsDecode goods) {
        boolean add = false;
        for (IDoc iDoc : mGoodsList) {
            EnterOrderDetails d = (EnterOrderDetails) iDoc;
            if (GoodsUtils.isSame(d, goods)) {
                add = true;
                if (goods.isSerial()) {
                    for (Sn sn : d.getSnList()) {
                        if (sn.getSnNo().equals(sn.getSnNo())) {
                            iDialog.displayMessageDialog("序列号不能重复添加!" + sn.getSnNo());
                            return;
                        }
                    }
                    //如果序列号肯定存储，那snList肯定不为kong
                    Sn e = new Sn();
                    e.setProderId(d.getId());
                    d.getSnList().add(e);
                    d.setNowQty((d.getNowQty() + goods.getPLN_QTY().intValue()));
                    break;
                }
                int totalCount = d.getNowQty() + goods.getPLN_QTY().intValue();
                if (!GoodsUtils.checkTotal(goods, d)) {
                    iDialog.displayMessageDialog("超出可添加数量 sku:" + goods.getSKU_CODE() + ",数量:" + goods.getPLN_QTY());
                    return;
                }
                d.setNowQty(totalCount);
                d.setLabels(null);
            }
        }

        if (add) {
            mDocAdapter.notifyDataSetChanged();
            return;
        }


        EnterOrderDetails clone = (EnterOrderDetails) g.clone();

        int totalCount = clone.getNowQty() + goods.getPLN_QTY().intValue();
        if (!GoodsUtils.checkTotal(goods, clone)) {
            iDialog.displayMessageDialog("超出可添加数量 sku:" + goods.getSKU_CODE() + ",数量:" + goods.getPLN_QTY());
            return;
        }
        clone.setNowQty(goods.getPLN_QTY().intValue());
        mGoodsList.add(0, clone);

        List<Sn> snList = clone.getSnList();
        //没有存入序列号
        if (snList == null) {
            clone.setSnList(new ArrayList<Sn>());
            Sn e = new Sn();
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
        String string = bundle.getString(DOC);
        mDocOrder = JsonUtils.fromJson(string, EnterOrderBean.class);
        mDocAdapter.setNewData(mGoodsList);
        loadDocGoods();
    }

    private void loadDocGoods() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("asnCode", mDocOrder.getAsnCode());
        OkHttpHeader.post(UrlApi.purchaseOrderDetails, mParams, new BaseResultCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                iDialog.cancelLoadingDialog();
            }

            @Override
            protected void onResult(BaseResult resultObj, int id) {
                mGoodsAll = resultObj.getList(EnterOrderDetails.class);

            }

            @Override
            protected void onFailed(String error, int code) {
                iDialog.displayMessageDialog(error);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        EnterOrderDetails pd = (EnterOrderDetails) mGoodsList.get(position);
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
                                EnterOrderDetails iDoc = (EnterOrderDetails) mGoodsList.get(position);
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

        List<EnterStrGoodsSubParams> params = new ArrayList<>();
        for (IDoc iDoc : mGoodsList) {
            EnterOrderDetails pd = (EnterOrderDetails) iDoc;

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
        OkHttpHeader.post(UrlApi.purchaseOrderDetails_submit, params, new BaseResultCallback() {
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
        EnterDocDetailsActivity.putLoadUrl(UrlApi.purchaseOrderDetails, bundle);

        iToAction.startActivity(EnterDocDetailsActivity.class, bundle);
    }

    @Override
    public void handleClickSeeSnList(IDoc d) {
        mToChangeGoods = (EnterOrderDetails) d;
        Bundle bundle = new Bundle();
        EnterSnListActivity.put(mToChangeGoods.getSnList(), mToChangeGoods, bundle);
        iToAction.startActivity(EnterSnListActivity.class, bundle);
    }

    @Subscribe
    public void changeGoods(EnterOrderDetails pd) {
        if (pd.getSnList().size() == 0) {
            mGoodsList.remove(mToChangeGoods);
        }
        mToChangeGoods.setSnList(pd.getSnList());
        mToChangeGoods.setNowQty(pd.getSnList().size());
        mDocAdapter.notifyDataSetChanged();
    }
}
