package cn.looksafe.client.beans;

/**
 * Created by huyg on 2020-06-02.
 */
public class UserInfo {


    /**
     * msg :
     * code : 0
     * userinfo : {"money":0,"provincecode":0,"vipexpdate":20210521,"isvip":1}
     */

    private String msg;
    private int code;
    private UserinfoBean userinfo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {

        /**
         * money : 0
         * provincecode : 330000
         * citycode : 330781
         * addrcity : 浙江金华兰溪
         * sex : 1
         * addrdetail : 某某村
         * nickname : 测试
         * vipexpdate : 20210525
         * age : 11
         * email : 378@qq.com
         * isvip : 1
         * realname : 真名
         */

        private int money;
        private int provincecode;
        private int citycode;
        private String addrcity;
        private int sex;
        private String addrdetail;
        private String nickname;
        private int vipexpdate;
        private int age;
        private String email;
        private int isvip;
        private String realname;
        private String headimg;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getProvincecode() {
            return provincecode;
        }

        public void setProvincecode(int provincecode) {
            this.provincecode = provincecode;
        }

        public int getCitycode() {
            return citycode;
        }

        public void setCitycode(int citycode) {
            this.citycode = citycode;
        }

        public String getAddrcity() {
            return addrcity;
        }

        public void setAddrcity(String addrcity) {
            this.addrcity = addrcity;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getAddrdetail() {
            return addrdetail;
        }

        public void setAddrdetail(String addrdetail) {
            this.addrdetail = addrdetail;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getVipexpdate() {
            return vipexpdate;
        }

        public void setVipexpdate(int vipexpdate) {
            this.vipexpdate = vipexpdate;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }
    }
}
