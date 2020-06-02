package cn.looksafe.client.beans;

/**
 * Created by huyg on 2020-06-02.
 */
public class SectionBean implements Comparable<SectionBean>{

    /**
     * adminname : looksafe
     * createtime : 1590043386562
     * isreal : -1
     * ordernum : 3
     * sid : 641
     * sname : 36计1-18计
     * surl : http://cdn.looksafe.cn/fs/20200521144303ipCSA.mp4
     * vtime : 1093
     */

    private String adminname;
    private String createtime;
    private int isreal;
    private Integer ordernum;
    private int sid;
    private String sname;
    private String surl;
    private int vtime;

    public SectionBean(int ordernum, int isreal, String surl) {
        this.ordernum = ordernum;
        this.isreal = isreal;
        this.surl = surl;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getIsreal() {
        return isreal;
    }

    public void setIsreal(int isreal) {
        this.isreal = isreal;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public int getVtime() {
        return vtime;
    }

    public void setVtime(int vtime) {
        this.vtime = vtime;
    }

    @Override
    public int compareTo(SectionBean o) {
        return this.getOrdernum().compareTo(o.getOrdernum());
    }
}
