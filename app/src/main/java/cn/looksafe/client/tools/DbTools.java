package cn.looksafe.client.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cn.looksafe.client.beans.LoopImgHttp;

public class DbTools {
    private Context context;
    private static MyOpenHelper mMyOpenHelper;
    private static volatile DbTools instance = null;

    public DbTools(Context context) {
        super();
        this.context = context;
        if (mMyOpenHelper == null)
            mMyOpenHelper = new MyOpenHelper(context);
    }

    public static synchronized DbTools getInstance(Context context) {
        if (instance == null) {
            synchronized (DbTools.class) {
                if (instance == null) {
                    instance = new DbTools(context);
                }
            }
        }
        return instance;
    }


    public void setInstanceNull() {
        instance = null;
    }


    public synchronized ArrayList<String> queryAllLoopImgs() {
        ArrayList<String> list = new ArrayList<>();
        try {
            SQLiteDatabase db = mMyOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from lpimgs  order by id ASC limit 4;",
                    new String[]{});
            while (cursor.moveToNext()) {
                String url = cursor.getString(cursor.getColumnIndex("imgurl"));
                list.add(url);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized void deleteAllLoopImgs() {
        SQLiteDatabase db = mMyOpenHelper.getWritableDatabase();
        db.execSQL("delete from lpimgs;");
    }

    public synchronized void insertAllLoopImgs(LoopImgHttp loopImgHttp) {
        try {
            deleteAllLoopImgs();
            SQLiteDatabase db = mMyOpenHelper.getWritableDatabase();
            for (int i = 0; i < loopImgHttp.list.size(); i++) {
                db.execSQL("replace into lpimgs (id,imgurl) values (?,?);", new String[]{
                        loopImgHttp.list.get(i).id + "", loopImgHttp.list.get(i).imgurl
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public class MyOpenHelper extends SQLiteOpenHelper {

        public MyOpenHelper(Context context) {
            super(context, "lkdb", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //轮播图列表
            db.execSQL("create table if not exists lpimgs(id int primary key,imgurl text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }

    }
}
