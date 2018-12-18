package com.wanhao.wms.ui.function.in;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wanhao.wms.R;
import com.wanhao.wms.base.BaseActivity;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.widget.InputSearchView;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import java.lang.reflect.Field;

/**
 * 货品移动（移入，移出）
 */
@BindLayout(layoutRes = R.layout.activity_goods_move, addStatusBar = true)
public class GoodsMoveActivity extends BaseActivity implements IMoveView {
    public static final String P = "presenter";
    @BindView(R.id.goodsMove_rack_tv)
    TextView mTargetRackTv;
    @BindView(R.id.goodsMove_rv)
    RecyclerView mGoodsRv;
    @BindView(R.id.goodsMove_submit_tv)
    TextView mSubmitTv;
    @BindView(R.id.goodsMove_rack_label)
    TextView mRackLabel;
    @BindView(R.id.goodsMove_searchView)
    InputSearchView mSearchView;

    IMovePresenter movePresenter;

    public static void putPresenter(Class<IMovePresenter> presenter, Bundle bundle) {
        bundle.putString(P, presenter.getName());
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initWidget() {
        super.initWidget();
        String presenterPackager = getBundle().getString(P);
        try {
            movePresenter = (IMovePresenter) Class.forName(presenterPackager).newInstance();
            Field iDialog = movePresenter.getClass().getField("iDialog");
            iDialog.set(movePresenter, this);
            Field iMoveView = movePresenter.getClass().getField("iMoveView");
            iMoveView.set(movePresenter, this);
            BindPresenter annotation = movePresenter.getClass().getAnnotation(BindPresenter.class);
            mTopBar.setTitle(annotation.titleRes());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        mGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mGoodsRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 10, Color.GRAY));

        mSubmitTv.setOnClickListener(this);
        mSearchView.setOnScanningListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScanningActivity();
            }
        });

        mSearchView.setOnSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToDecode(mSearchView.getText().toString());
            }
        });

        movePresenter.init(getBundle());

    }

    @Override
    public void onResultCode(String resultContent) {
        super.onResultCode(resultContent);
        onToDecode(resultContent);
    }

    @Override
    public void onToDecode(String code) {
        movePresenter.actionSearchCode(code);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == mSubmitTv.getId()) {
            movePresenter.actionSubmit();
        }
    }

    @Override
    public void setRackLabel(int label) {
        mRackLabel.setText(label);
    }

    @Override
    public void setRack(CharSequence targetRack) {
        mTargetRackTv.setText(targetRack);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mGoodsRv.setAdapter(adapter);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mGoodsRv;
    }
}
