package cn.looksafe.client.beans;

import java.util.ArrayList;

public class HotListHttp {
    public String msg;
    public int code;
    public ArrayList<Video> list;

    public static class Video {
        public String createtime;
        public String adminname;
        public int vtypeid;
        public int playtimes;
        public int ordernum;
        public String vurl;
        public int isvip;
        public int vid;
        public String vname;
        public int totalnum;
        public int belongid;
        public String vimg;
        public int isreal;//是否原生视频

    }
}
