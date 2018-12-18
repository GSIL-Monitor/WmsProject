package com.wanhao.wms.ui.function.base.goods;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wanhao.wms.R;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.function.base.doc.AbsDecodeActivity;
import com.wanhao.wms.ui.widget.InputSearchView;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import java.lang.reflect.Field;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
@BindLayout(layoutRes = R.layout.activity_enter_doc_operate, addStatusBar = true)
public class GoodsLIstActivity extends AbsDecodeActivity implements IGoodsListView {
    public static final String PRESENTER = "goodsPresenter";

    @BindView(R.id.enter_storage_search_view)
    InputSearchView mSearchView;
    @BindView(R.id.enter_storage_operate_submit_tv)
    TextView mSubmitTv;
    @BindView(R.id.enter_storage_operate_rv)
    RecyclerView mGoodsRv;
    @BindView(R.id.enter_storage_rack_tv)
    TextView mRackTv;

    private IGoodsListPresenter mPresenter;

    public static void putPresenter(Class pClazz, Bundle bundle) {
        bundle.putString(PRESENTER, pClazz.getName());
    }

    public Object getPresenter() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(getBundle().getString(PRESENTER)).newInstance();
    }

    @Override
    public void initWidget() {
        super.initWidget();

        try {
            Object presenter = getPresenter();
            mPresenter = (IGoodsListPresenter) presenter;
            Field iGoodsListView = presenter.getClass().getField("iGoodsListView");
            Field iDialog = presenter.getClass().getField("iDialog");
            Field iToAction = presenter.getClass().getField("iToAction");
            if (iGoodsListView != null) {
                iGoodsListView.set(presenter, this);
            }
            if (iDialog != null) {
                iDialog.set(presenter, this);
            }
            if (iToAction != null) {
                iToAction.set(presenter, this);
            }

            Class aClass = presenter.getClass();
            BindPresenter annotation = (BindPresenter) aClass.getAnnotation(BindPresenter.class);
            if (annotation.titleRes() != -1) {
                mTopBar.setTitle(annotation.titleRes());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mGoodsRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20, Color.GRAY));

        mPresenter.setAdapter(mGoodsRv);

        mTopBar.addRightTextButton("详情", R.id.top_bar_right_btn).setOnClickListener(this);

        mSubmitTv.setOnClickListener(this);

        mPresenter.init(getBundle());
        mSearchView.setOnSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.handleClickSearch(mSearchView.getText());
            }
        });
        mSearchView.setOnScanningListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toScanningActivity();
            }
        });

    }

    @Override
    public void onResultCode(String resultContent) {
        super.onResultCode(resultContent);
        decode(resultContent);
    }

    @Override
    public void forbidClick(View v) {
        super.forbidClick(v);
        if (v.getId() == mSubmitTv.getId()) {
            mPresenter.actionSubmit();
        } else if (v.getId() == R.id.top_bar_right_btn) {
            mPresenter.actionDocDetails();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void decode(String decode) {
        mPresenter.decode(decode);
    }

    @Override
    public void setRackTextView(CharSequence charSequence) {
        mRackTv.setText(charSequence);
    }


    @Override
    public void onToDecode(String code) {
        super.onToDecode(code);
        mPresenter.decode(code);
    }
}
