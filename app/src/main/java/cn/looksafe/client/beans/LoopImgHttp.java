package cn.looksafe.client.beans;

import java.util.ArrayList;

public class LoopImgHttp {
    public String msg;
    public int code;
    public ArrayList<LoopImg> list;

    public static class LoopImg {

        public String imgurl;
        public String createtime;
        public String imgname;
        public String adminname;
        public int id;

    }
}
