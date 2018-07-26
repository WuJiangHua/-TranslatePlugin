package cn.translate.http.bean;

import cn.translate.utils.BaiduTranslateConstant;
import cn.translate.utils.LanguageUtils;
import cn.translate.utils.Md5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestBean extends LanguageBean {
    private String q; //请求翻译query	UTF-8编码

    private String query;

    private String sign;//签名	appid+q+salt+密钥的MD5值

    public RequestBean (String query) {
        this.query = query;
        initLanguage();
        initQuery();
        initSign();
    }

    public String getQ() {
        return q;
    }

    public String getSign() {
        return sign;
    }

    private void initLanguage() {
        setSourceChinese(LanguageUtils.isChinese(query));
    }

    private void initQuery() {
        this.q = query;
    }

    private void initSign() {
        StringBuilder builder = new StringBuilder();
        builder.append(getAppid())
                .append(query)
                .append(getSalt())
                .append(BaiduTranslateConstant.PASSWORD);

        sign = Md5Utils.md5(builder.toString());
    }

    public Map<String, String> toMap() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("q", encode(q));
        requestMap.put("from", encode(getFrom()));
        requestMap.put("to", encode(getTo()));
        requestMap.put("appid", encode(getAppid()));
        requestMap.put("salt", encode(getSalt()));
        requestMap.put("sign", encode(sign));
        return requestMap;
    }

    private String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}
