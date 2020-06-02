package cn.looksafe.client.beans;

public class HttpBean {
    private int code;
    private String msg;
    private String token;
    private boolean isactive;
    private boolean isvip;
    private String vipexp;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public boolean isIsvip() {
        return isvip;
    }

    public void setIsvip(boolean isvip) {
        this.isvip = isvip;
    }

    public String getVipexp() {
        return vipexp;
    }

    public void setVipexp(String vipexp) {
        this.vipexp = vipexp;
    }
}
