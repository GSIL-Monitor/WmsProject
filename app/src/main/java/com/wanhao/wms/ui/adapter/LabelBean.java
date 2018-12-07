package com.wanhao.wms.ui.adapter;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class LabelBean implements ILabel {
    private CharSequence text;
    private CharSequence label;

    public LabelBean(CharSequence label, CharSequence text) {
        this.text = text;
        this.label = label;
    }

    @Override
    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    @Override
    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
