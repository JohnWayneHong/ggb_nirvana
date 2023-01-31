package com.ggb.nirvanaclub.modules.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ggb.nirvanaclub.bean.DevelopJokesBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DevelopJokesUtil {

    public static void getRandomJokes(Context context,OnGetRandomJokesCompleteListener listener){
        new DevelopRandomJokesTask(listener).execute();
    }

    public static class DevelopRandomJokesTask extends AsyncTask<Void,Void, DevelopJokesBean> {

        private final OnGetRandomJokesCompleteListener listener;

        public DevelopRandomJokesTask(OnGetRandomJokesCompleteListener listener){
            this.listener = listener;
        }

        @Override
        protected DevelopJokesBean doInBackground(Void... voids) {
            try {
                DevelopJokesBean girlBean = new DevelopJokesBean(0,"",new ArrayList<>());
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .post(RequestBody.create(MediaType.parse("application/json"),""))
//                        .url("http://tools.cretinzp.com/jokes"+"/home/text")//请求接口。如果需要传参拼接到接口后面。
                        .url("http://tools.cretinzp.com/jokes"+"/home/recommend")//请求接口。如果需要传参拼接到接口后面。
                        .addHeader("project_token", "A9E915F9F9CF4D06B0C661D1B2E25997")
                        .addHeader("channel", "cretin_open_api")
                        .build();//创建Request 对象
                Response response = client.newCall(request).execute();//得到Response 对象
                if (response.code()==200){
                    girlBean = new Gson().fromJson(response.body().string(),new TypeToken<DevelopJokesBean>(){}.getType());
                }

                return girlBean;
            }catch (Exception e){
                Log.e("exception------------>",e.getMessage() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(DevelopJokesBean result) {
            listener.onJokesComplete(result);
        }
    }

    public interface OnGetRandomJokesCompleteListener{
        void onJokesComplete(DevelopJokesBean result);
    }

}
