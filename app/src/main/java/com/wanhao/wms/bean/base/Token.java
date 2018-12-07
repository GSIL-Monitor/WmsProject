package com.wanhao.wms.bean.base;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
public class Token extends LitePalSupport {
    private Integer id;
    private String appScope;
    private String appKey;
    private String appToken;
    private String appName;
    private String appVersion;
    private Long appTs;
    private String appLanguage;


    public static Token getToken() {
        List<Token> all = LitePal.findAll(Token.class);
        if (all.size() == 0) {
            return null;
        }
        return all.get(0);
    }

    public void saveSingle() {
        Token token = getToken();
        if (token == null) {
            save();
            return;
        }
        update(token.getId());
    }


    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();

        map.put("appScope", appScope);
        map.put("appKey", appKey);
        map.put("acccessToken", appToken);
        map.put("appName", appName);
        map.put("appVersion", appVersion);
        map.put("appTs", String.valueOf(appTs));
        map.put("sysVersion","1.0.0");
        map.put("appLanguage", appLanguage);
        return map;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppScope() {
        return appScope;
    }

    public void setAppScope(String appScope) {
        this.appScope = appScope;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Long getAppTs() {
        return appTs;
    }

    public void setAppTs(Long appTs) {
        this.appTs = appTs;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }


}
