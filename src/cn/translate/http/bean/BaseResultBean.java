package cn.translate.http.bean;

import java.io.Serializable;
import java.util.List;

public class BaseResultBean implements Serializable{
    private String from;
    private String to;
    private String trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(String trans_result) {
        this.trans_result = trans_result;
    }
}
