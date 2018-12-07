package com.wanhao.wms.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wanhao.wms.R;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class InputSearchView extends FrameLayout implements View.OnClickListener {
    private EditText mInputEt;
    private ImageView mSearchIv;
    private ImageView mDelIv;//删除文字

    public InputSearchView(@NonNull Context context) {
        this(context, null);
    }

    public InputSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = layoutInflater.inflate(R.layout.view_search, null);
        addView(inflate);
        initView();
    }

    private void initView() {
        mInputEt = findViewById(R.id.view_search_et);
        mSearchIv = findViewById(R.id.view_search_iv);
        mDelIv = findViewById(R.id.view_search_del_iv);

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    int visibility = mDelIv.getVisibility();
                    if (visibility != View.INVISIBLE) {
                        mDelIv.setVisibility(INVISIBLE);
                    }
                    return;
                }
                int visibility = mDelIv.getVisibility();
                if (visibility != View.VISIBLE) {
                    mDelIv.setVisibility(VISIBLE);
                }

            }
        });
        mDelIv.setOnClickListener(this);
    }

    public void setInputHint(int hint) {
        mInputEt.setHint(hint);
    }

    public void setInputText(CharSequence text) {
        mInputEt.setText(text);
    }

    public CharSequence getText() {
        return mInputEt.getText();
    }

    public void setOnSearchListener(View.OnClickListener l) {
        mSearchIv.setOnClickListener(l);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mDelIv.getId()) {
            mInputEt.setText("");
        }
    }
}
