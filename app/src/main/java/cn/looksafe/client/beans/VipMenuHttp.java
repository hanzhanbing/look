package cn.looksafe.client.beans;

import java.util.ArrayList;

public class VipMenuHttp {
    public int code;
    public String msg;
    public ArrayList<Menu> list;

    public static class Menu {
        public int id;
        public String pname;
        public int realamount;
        public int oriamount;
        public String expdays;

    }
}
