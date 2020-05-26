package cn.looksafe.client.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import androidx.core.content.FileProvider;
import cn.looksafe.client.beans.VideosBean;

public class Tools {
    /**
     * @param in
     * @return 从服务端通讯中读取字符串
     */
    public static String readResponse(InputStream in) {
        byte[] bytes = new byte[2048];
        String res = "";
        try {
            int n = -1;
            while ((n = in.read(bytes, 0, bytes.length)) != -1) {
                res += new String(bytes, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ArrayList<VideosBean.MainVideo> getTypeVideoList(ArrayList<VideosBean.MainVideo> srcList, int typeid) {
        ArrayList<VideosBean.MainVideo> list = new ArrayList<>();
        for (int i = 0; i < srcList.size(); i++) {
            VideosBean.MainVideo mainVideo = srcList.get(i);
            if (typeid == mainVideo.vtypeid || typeid == -1) {
                list.add(mainVideo);
            }
        }
        return list;
    }

    //-----------------------------
    public static String getTimeFormat(String timeStr) {
        long time = Long.parseLong(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = addZero(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = addZero(day);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//24小时制
        String hourStr = addZero(hour);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteStr = addZero(minute);
        int second = calendar.get(Calendar.SECOND);
        String secondStr = addZero(second);
        return (year + "-" + monthStr + "-" + dayStr + " "
                + hourStr + ":" + minuteStr + ":" + secondStr);
    }

    private static String addZero(int param) {
        String paramStr = param < 10 ? "0" + param : "" + param;
        return paramStr;
    }


    public static String date8toStr(String yyyyMMdd) {
        if (TextUtils.isEmpty(yyyyMMdd) || yyyyMMdd.length() < 8) return "";
        String res = yyyyMMdd.substring(0, 4) + "-" + yyyyMMdd.substring(4, 6) + "-" + yyyyMMdd.substring(6, 8);
        return res;
    }

    /**
     * 获取app的版本名称versionName  和 versionCode
     */
    public static String getAppVersionInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionName + "," + versionCode;
        } catch (Exception e) {
        }
        return "0,0";
    }

    public static int getAppVersionCode(Context context) {
        try {
            String pkName = context.getPackageName();
            int versionCode = context.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {
        }
        return 0;
    }


    public static boolean install(String apkPath, Context context) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            java.lang.Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
            }
        }
        return result;

    }

    /**
     * @param url
     * @return 文件名称
     */
    public static String getNameFromURL(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1];
    }

    private static long getFolderSize(java.io.File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * @return 获取缓存大小
     */
    public static String getCacheSize() {
        long size = 0;
        File fc = new File(AppConfig.apksDir);
        if (fc.exists()) {
            size += getFolderSize(fc);
        }
        if (size < 1000) {
            return size + "B";
        } else if (size < 1000 * 1000) {
            return size / 1000 + "KB";
        } else if (size < 1000 * 1000 * 1000) {
            return size / 1000 / 1000 + "MB";
        }
        return "";
    }

    private static void deleteFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFiles(f);
            }
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 清除缓存
     */
    public static void cleanAllCache(Context context) {
        SPutil.getInstance(context).setLoginname("");
        SPutil.getInstance(context).setLoginpwd("");
        File fc = new File(AppConfig.rootDir);
        if (fc.exists()) {
            deleteFiles(fc);
        }
    }

    /**
     * @param length
     * @return 获取随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getWxMD5(String plainText, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");//获取MD5实例
            md.update(plainText.getBytes());//此处传入要加密的byte类型值
            byte[] digest = md.digest();//此处得到的是md5加密后的byte类型值
            int i;
            StringBuilder sb = new StringBuilder();
            for (int offset = 0; offset < digest.length; offset++) {
                i = digest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append(0);
                sb.append(Integer.toHexString(i));//通过Integer.toHexString方法把值变为16进制
            }
            return sb.toString().substring(0, length).toUpperCase();//从下标0开始，length目的是截取多少长度的值
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
