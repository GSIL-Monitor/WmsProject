package com.wanhao.wms.ui.adapter;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/24
 *
 * @author ql
 */
public class GridBean implements IGrid {
    private CharSequence label;
    private int tag;
    private int iconRes;

    public GridBean(CharSequence label, int tag, int iconRes) {
        this.label = label;
        this.tag = tag;
        this.iconRes = iconRes;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    @Override
    public CharSequence getLabel() {
        return label;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getIconRes() {
        return iconRes;
    }
}
