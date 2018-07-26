package cn.translate.http;

import cn.translate.http.bean.TranslateBean;

import java.util.List;

public interface ICallBack {
    void onRequestSuccess(List<TranslateBean> result);
}
