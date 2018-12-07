package com.wanhao.wms.http;

import com.wanhao.wms.info.UrlApi;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/5
 *
 * @author ql
 */
public class DecodeHelper {
    private static final Map<String, Object> params = new HashMap<>();

    public static void decode(String code, DecodeCallback callback) {
        params.put("barcode", code);
        OkHttpHeader.post(UrlApi.decode, params, callback);
    }
}
