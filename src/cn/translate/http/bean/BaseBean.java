package cn.translate.http.bean;

import cn.translate.utils.BaiduTranslateConstant;

import java.io.Serializable;

public class BaseBean implements Serializable{

    private String appid;
    private String salt; //随机数

    public BaseBean() {
        appid = BaiduTranslateConstant.APP_ID;
        salt = String.valueOf(System.currentTimeMillis());
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
