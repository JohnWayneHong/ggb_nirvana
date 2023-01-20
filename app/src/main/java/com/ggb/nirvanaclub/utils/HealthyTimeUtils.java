package com.ggb.nirvanaclub.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ggb.nirvanaclub.bean.HealthyBean;
import com.ggb.nirvanaclub.bean.AppUpdateListBean;
import com.ggb.nirvanaclub.bean.HealthyBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HealthyTimeUtils {

    public static void HealthyTime(String path,Context context,OnHealthyTimeCompleteListener listener){
        new HealthyTimeTask(path,listener).execute();
    }

    public static class HealthyTimeTask extends AsyncTask<Void,Void, HealthyBean> {

        private final String fileServicePath;
        private final OnHealthyTimeCompleteListener listener;

        public HealthyTimeTask(String path,OnHealthyTimeCompleteListener listener){
            this.fileServicePath = path;
            this.listener = listener;
        }

        @Override
        protected HealthyBean doInBackground(Void... voids) {
            try {
                HealthyBean config = new HealthyBean("","",0L,"");
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(fileServicePath)//请求接口。如果需要传参拼接到接口后面。
                        .build();//创建Request 对象
                Response response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()&&response.code()==200){
                    config = new Gson().fromJson(response.body().string(),new TypeToken<HealthyBean>(){}.getType());
                }

                return config;
            }catch (Exception e){
                Log.e("exception------------>",e.getMessage() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(HealthyBean result) {
            listener.onConfigComplete(result);
        }
    }

    public interface OnHealthyTimeCompleteListener{
        void onConfigComplete(HealthyBean result);
    }

}
