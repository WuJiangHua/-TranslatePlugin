package cn.translate;

import cn.translate.http.HttpUtils;

public class MainTest {
    public static void main(String[] args) {

        HttpUtils.initOkHttp();
        HttpUtils.doGetAsyn("smile", null);
    }
}
