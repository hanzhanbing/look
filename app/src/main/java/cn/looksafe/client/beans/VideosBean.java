package cn.looksafe.client.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class VideosBean implements Serializable {
    public int code;
    public String msg;
    public int total;
    public ArrayList<MainVideo> mainVideosList=new ArrayList<>();

    public static class MainVideo implements Serializable{
        public int vid;
        public String vname;
        public String vurl;
        public String vimg;
        public int vtypeid;
        public String vtypename;
        public int isvip;
        public int totalnum;
        public int ordernum;
        public String createtime;
        public String adminname;
        public int playtimes;
        public int isreal;
        public ArrayList<SubVideo> sectionlist=new ArrayList<>();

        public static class SubVideo implements Serializable {
            public int sid;
            public String sname;
            public String surl;
            public int ordernum;
            public String createtime;
            public String adminname;
            public int isreal;
        }

    }


}
