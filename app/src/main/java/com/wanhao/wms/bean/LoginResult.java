package com.wanhao.wms.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/11/27
 *
 * @author ql
 */
public class LoginResult extends LitePalSupport {
    private Integer id;
    private String userCode;
    private String userName;
    private String language;
    private String token;
    private long ts;

    public void saveSingle() {

        LoginResult user = getUser();
        if (user == null) {
            save();
            return;
        }

        update(user.getId());

    }

    public static LoginResult getUser() {
        List<LoginResult> all = LitePal.findAll(LoginResult.class);
        if (all.size() == 0) {
            return null;
        }

        return all.get(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
