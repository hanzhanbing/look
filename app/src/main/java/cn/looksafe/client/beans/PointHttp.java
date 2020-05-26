package cn.looksafe.client.beans;

import java.util.ArrayList;

public class PointHttp {
    public ArrayList<Point> list;

    public class Point {
        public int pid;
        public int type;
        public String pname;
        public String addr;
        public String jdu;
        public String wdu;
    }

}
