package cn.looksafe.client.beans;

import java.util.List;

import cn.looksafe.client.db.Action;

/**
 * Created by huyg on 2020-06-17.
 */
public class ActionBean {

    private String phone;
    private String token;
    private List<Action> record;


    public ActionBean(String phone, String token, List<Action> record) {
        this.phone = phone;
        this.token = token;
        this.record = record;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Action> getRecord() {
        return record;
    }

    public void setRecord(List<Action> record) {
        this.record = record;
    }
}
