package cn.translate.http;

import cn.translate.http.bean.BaseResultBean;
import cn.translate.http.bean.RequestBean;
import cn.translate.http.bean.TranslateBean;
import cn.translate.utils.BaiduTranslateConstant;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.apache.http.util.TextUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HttpUtils {


    private static OkHttpClient mClient;

    public static void initOkHttp() {
        if (mClient == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(createSSLSocketFactory())
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    //其他配置
                    .build();

            mClient = okHttpClient;
        }
    }


    public static void doGetAsyn(String selectText, ICallBack callBack) {
        if (TextUtils.isEmpty(selectText) || TextUtils.isEmpty(selectText.trim())) {
            return;
        }

        mClient.newCall(generateRequest(selectText))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response == null || response.code() != 200) {
                            return;
                        }
                        String body = response.body().string();
                        if (TextUtils.isEmpty(body)) {
                            return;
                        }

                        BaseResultBean bean = (BaseResultBean) JSON.parseObject(body, BaseResultBean.class);

                        List<TranslateBean> result = JSON.parseArray(bean.getTrans_result(), TranslateBean.class);
                        if (callBack != null) {
                            callBack.onRequestSuccess(result);
                        }
                    }
                });
    }

    private static Request generateRequest(String query) {

        RequestBean bean = new RequestBean(query);
        StringBuilder builder = new StringBuilder(BaiduTranslateConstant.BASEURL);
        Map<String, String> params = bean.toMap();

        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            Iterator iterator = keys.iterator();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                String value = params.get(key);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                if (!builder.toString().contains("?")) {
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key).append("=").append(value);
            }
        }

        return new Request.Builder().url(builder.toString()).build();
    }

    public static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{myX509TrustManager},
                    null);
            sSLSocketFactory = sc.getSocketFactory();

        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static X509TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };
}
