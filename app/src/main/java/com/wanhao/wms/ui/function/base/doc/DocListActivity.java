package com.wanhao.wms.ui.function.base.doc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wanhao.wms.R;
import com.wanhao.wms.base.BindLayout;
import com.wanhao.wms.base.bind.BindView;
import com.wanhao.wms.ui.adapter.DocAdapter;
import com.wanhao.wms.ui.function.base.BindPresenter;
import com.wanhao.wms.ui.widget.InputSearchView;
import com.wanhao.wms.utils.div.DividerItemDecoration;

import java.lang.reflect.Field;

@BindLayout(layoutRes = R.layout.activity_doc_list, addStatusBar = true)
public class DocListActivity extends AbsDecodeActivity implements IDocView {
    public static final String PREENTER = "presenter";

    @BindView(R.id.doc_list_inputView)
    InputSearchView mInputSearchView;
    @BindView(R.id.doc_list_rv)
    RecyclerView mDocListRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private DocAdapter mDocAdapter = new DocAdapter();

    private DocPresenter mDocPresenter;

    public static <T extends DocPresenter> void put(Class<T> clazz, Bundle bundle) {
        bundle.putString(PREENTER, clazz.getName());
    }

    private Object getPresenter() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName(getBundle().getString(PREENTER)).newInstance();
    }

    @Override
    public void initData() {
        super.initData();

    }


    @Override
    public void initWidget() {
        super.initWidget();
        try {
            Object obj = getPresenter();
            mDocPresenter = (DocPresenter) obj;
            if (obj instanceof AbsDocPresenter) {
                Field iToAction = mDocPresenter.getClass().getField("iToAction");
                Field iDocView = mDocPresenter.getClass().getField("iDocView");
                Field iDialog = mDocPresenter.getClass().getField("iDialog");
                iToAction.set(mDocPresenter, this);
                iDocView.set(mDocPresenter, this);
                iDialog.set(mDocPresenter, this);
            }
            Class aClass = obj.getClass();
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


        mDocListRv.setLayoutManager(new LinearLayoutManager(this));
        mDocListRv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL, 20, Color.GRAY));
        srl.setOnRefreshListener(mDocPresenter);
        mDocListRv.setAdapter(mDocAdapter);
        mDocPresenter.init(getBundle());
        mInputSearchView.setOnSearchListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDocPresenter.handlerClickSearch(mInputSearchView.getText());
            }
        });
    }


    @Override
    public void decode(String decode) {
        mDocPresenter.decode(decode);
    }


    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mDocListRv.setAdapter(adapter);
    }

    @Override
    public void setInputTextHint(@StringRes int hint) {
        mInputSearchView.setInputHint(hint);
    }

    @Override
    public void setInputText(CharSequence charSequence) {
        mInputSearchView.setInputText(charSequence);
    }

    @Override
    public RecyclerView getListView() {
        return mDocListRv;
    }

    @Override
    public void setRefresh(boolean refresh) {
        srl.setRefreshing(refresh);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDocPresenter.onDestroy();
    }

    @Override
    public void onToDecode(String code) {
        super.onToDecode(code);
        mDocPresenter.decode(code);
    }
}
