package cn.looksafe.client.beans;

import java.util.List;

/**
 * Created by huyg on 2020-06-08.
 */
public class VideoType {


    /**
     * code : 0
     * mainlist : [{"sectionlist":[{"tsid":10001,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"英语"},{"tsid":10002,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"语文"},{"tsid":10003,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"历史"},{"tsid":10004,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"科学"},{"tsid":10005,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"数学"}],"tmid":10000,"tmimg":" http://www.looksafe.cn/fs/luke.png","tmname":"快乐学习"},{"sectionlist":[{"tsid":20001,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"动漫"},{"tsid":20002,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"影视"},{"tsid":20003,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"纪录片"},{"tsid":20004,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"综艺"},{"tsid":20005,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"知识"}],"tmid":20000,"tmimg":"http://www.looksafe.cn/fs/luke.png","tmname":"轻松学习"},{"sectionlist":[{"tsid":30001,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"免费视训"}],"tmid":30000,"tmimg":"http://www.looksafe.cn/fs/luke.png","tmname":"免费视训"},{"sectionlist":[{"tsid":40001,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"教学视频"},{"tsid":40002,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"护眼知识"}],"tmid":40000,"tmimg":"http://www.looksafe.cn/fs/luke.png","tmname":"公益广场"}]
     * msg :
     */

    private int code;
    private String msg;
    private List<MainlistBean> mainlist;

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

    public List<MainlistBean> getMainlist() {
        return mainlist;
    }

    public void setMainlist(List<MainlistBean> mainlist) {
        this.mainlist = mainlist;
    }

    public static class MainlistBean {
        /**
         * sectionlist : [{"tsid":10001,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"英语"},{"tsid":10002,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"语文"},{"tsid":10003,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"历史"},{"tsid":10004,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"科学"},{"tsid":10005,"tsimg":"http://www.looksafe.cn/fs/luke.png","tsname":"数学"}]
         * tmid : 10000
         * tmimg :  http://www.looksafe.cn/fs/luke.png
         * tmname : 快乐学习
         */

        private int tmid;
        private String tmimg;
        private String tmname;
        private List<SectionlistBean> sectionlist;

        public int getTmid() {
            return tmid;
        }

        public void setTmid(int tmid) {
            this.tmid = tmid;
        }

        public String getTmimg() {
            return tmimg;
        }

        public void setTmimg(String tmimg) {
            this.tmimg = tmimg;
        }

        public String getTmname() {
            return tmname;
        }

        public void setTmname(String tmname) {
            this.tmname = tmname;
        }

        public List<SectionlistBean> getSectionlist() {
            return sectionlist;
        }

        public void setSectionlist(List<SectionlistBean> sectionlist) {
            this.sectionlist = sectionlist;
        }

        public static class SectionlistBean {
            /**
             * tsid : 10001
             * tsimg : http://www.looksafe.cn/fs/luke.png
             * tsname : 英语
             */

            private int tsid;
            private String tsimg;
            private String tsname;

            public int getTsid() {
                return tsid;
            }

            public void setTsid(int tsid) {
                this.tsid = tsid;
            }

            public String getTsimg() {
                return tsimg;
            }

            public void setTsimg(String tsimg) {
                this.tsimg = tsimg;
            }

            public String getTsname() {
                return tsname;
            }

            public void setTsname(String tsname) {
                this.tsname = tsname;
            }
        }
    }
}
