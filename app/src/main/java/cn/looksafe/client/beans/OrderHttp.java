package cn.looksafe.client.beans;

import java.util.ArrayList;

public class OrderHttp {
    public int code;
    public String msg;
    public ArrayList<Order> list;

    public static class Order {
        public int amount;
        public String threetradeno;
        public long servertime;
        public long paytime;
        public int payway;
        public String outtradeno;

    }
}
