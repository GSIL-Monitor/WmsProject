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
    private int labelRes;

    private int itemType = 3;
    public LabelBean(CharSequence label, CharSequence text) {
        this.text = text;
        this.label = label;
    }

    public LabelBean(int label, CharSequence text) {
        this.text = text;
        this.labelRes = label;
    }
    public LabelBean(int label, CharSequence text,int itemType) {
        this.text = text;
        this.labelRes = label;
        this.itemType = itemType;
    }

    public LabelBean(CharSequence label, CharSequence text, int itemType) {
        this.text = text;
        this.label = label;
        this.itemType = itemType;
    }

    public int getLabelRes() {
        return labelRes;
    }

    public void setLabelRes(int labelRes) {
        this.labelRes = labelRes;
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


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
