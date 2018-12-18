package com.wanhao.wms.http;

import com.wanhao.wms.bean.MarkRules;
import com.wanhao.wms.bean.base.BaseResult;
import com.wanhao.wms.bean.base.DecodeBean;
import com.wanhao.wms.bean.base.IGoodsDecode;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/5
 *
 * @author ql
 */
public abstract class DecodeCallback extends BaseResultCallback {
    String[] decodeTag;

    public DecodeCallback(String... decodeTag) {
        this.decodeTag = decodeTag;
    }

    @Override
    protected void onResult(BaseResult resultObj, int id) {

        if (!resultObj.isRs()) {
            onFailed(resultObj.getMessage(), id);
            return;
        }
        DecodeBean data = resultObj.getData(DecodeBean.class);
        String doc_code = data.getDOC_TYPE();
        String boxCode = MarkRules.code(MarkRules.BOX_MARK_CODE);
        String docCode = MarkRules.code(MarkRules.DOC_MARK_CODE);
        String goodsCode = MarkRules.code(MarkRules.GOODS_MARK_CODE);
        String rackCode = MarkRules.code(MarkRules.RACK_MARK_CODE);
        boolean isHas = false;
        OK:
        for (String s : decodeTag) {
            switch (s) {
                case MarkRules.BOX_MARK_CODE:
                    if (doc_code.equals(boxCode)) {
                        isHas = true;
                        onBoxCode(data);
                        break OK;
                    }
                    break;
                case MarkRules.DOC_MARK_CODE:
                    if (doc_code.equals(docCode)) {
                        isHas = true;
                        onDocCode(data);
                        break OK;
                    }
                    break;
                case MarkRules.GOODS_MARK_CODE:
                    if (doc_code.equals(goodsCode)) {
                        isHas = true;
                        onGoodsCode(data);
                        break OK;
                    }
                    break;
                case MarkRules.RACK_MARK_CODE:
                    if (doc_code.equals(rackCode)) {
                        isHas = true;
                        onRackCode(data);
                        break OK;
                    }
                    break;
            }
        }
        if (!isHas) {
            onOtherCode(data);
        }
    }

    public void onOtherCode(DecodeBean data) {

    }

    public void onRackCode(DecodeBean data) {

    }

    public void onGoodsCode(IGoodsDecode data) {

    }

    public void onDocCode(DecodeBean data) {

    }

    public void onBoxCode(DecodeBean data) {


    }


}
