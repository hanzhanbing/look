package cn.looksafe.client.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.look.core.http.BaseResponse;
import com.look.core.manager.SpManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.looksafe.client.beans.ActionBean;
import cn.looksafe.client.db.Action;
import cn.looksafe.client.db.ActionDao;
import cn.looksafe.client.db.AppDatabase;
import cn.looksafe.client.repository.DataRepository;

/**
 * Created by huyg on 2019-11-12.
 */
public class IWorkManager extends Worker {
    private Context mContext;
    private DataRepository mDataRepository;

    public IWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        mDataRepository = new DataRepository();
    }


    @NonNull
    @Override
    public Result doWork() {
        long time = System.currentTimeMillis();
        ActionDao actionDao = AppDatabase.getAppDatabase(mContext).actionDao();
        List<Action> actions = actionDao.getAll(time);
        if (actions == null || actions.size() == 0) {
            Log.d("IWorkManager", "result=============failure");
            return Worker.Result.failure();
        }
        ActionBean actionBean = new ActionBean(SpManager.getInstance(mContext).getSP("phone"),
                SpManager.getInstance(mContext).getSP("token"),
                actions);
        Gson gson = new Gson();

        if (upload(gson.toJson(actionBean))) {
            for (Action action : actions) {
                action.upFlg = 1;
                actionDao.update(action);
            }
        } else {
            return Worker.Result.failure();
        }
        return Worker.Result.success();
    }


    public boolean upload(String data) {
        try {
            URL url = new URL("http://47.114.33.38/luke/upLoadPlayLogApp");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.connect();

            String body = data;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String result = is2String(inputStream);//将流转换为字符串。
                Log.d("IWorkManager", "result=============" + result);
                if (!TextUtils.isEmpty(result)){
                    BaseResponse response = new Gson().fromJson(result,BaseResponse.class);
                    if (response.getCode()==0){
                        return true;
                    }else {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String is2String(InputStream is) throws IOException {

        //连接后，创建一个输入流来读取response
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(is, "utf-8"));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        //每次读取一行，若非空则添加至 stringBuilder
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        //读取所有的数据后，赋值给 response
        String response = stringBuilder.toString().trim();
        return response;
    }

}
