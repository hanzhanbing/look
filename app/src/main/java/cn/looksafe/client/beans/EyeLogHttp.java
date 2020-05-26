package cn.looksafe.client.beans;

import java.util.ArrayList;

public class EyeLogHttp {
    public String msg;
    public int code;
    public ArrayList<Eye> list;

    public static class Eye {
        public String loginname;
        public String righte;
        public String createday;
        public String lefte;
    }
}
