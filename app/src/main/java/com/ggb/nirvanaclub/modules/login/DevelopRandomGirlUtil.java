package com.ggb.nirvanaclub.modules.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ggb.nirvanaclub.bean.AppUpdateBean;
import com.ggb.nirvanaclub.bean.AppUpdateListBean;
import com.ggb.nirvanaclub.bean.DevelopRandomGirlBean;
import com.ggb.nirvanaclub.constans.C;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DevelopRandomGirlUtil {

    public static void getRandomGirl(Context context,OnGetRandomGirlCompleteListener listener){
        new DevelopRandomGirlTask(listener).execute();
    }

    public static class DevelopRandomGirlTask extends AsyncTask<Void,Void, DevelopRandomGirlBean> {

        private final OnGetRandomGirlCompleteListener listener;

        public DevelopRandomGirlTask(OnGetRandomGirlCompleteListener listener){
            this.listener = listener;
        }

        @Override
        protected DevelopRandomGirlBean doInBackground(Void... voids) {
            try {
                DevelopRandomGirlBean girlBean = new DevelopRandomGirlBean(0,"",new ArrayList<>());
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(C.THIRD_BASE_ADDRESS+"image/girl/list/random")//请求接口。如果需要传参拼接到接口后面。
                        .addHeader("APP_ID", C.MXNZP_APP_ID)
                        .addHeader("APP_SECRET", C.MXNZP_APP_SECRET)
                        .build();//创建Request 对象
                Response response = client.newCall(request).execute();//得到Response 对象
                if (response.code()==200){
                    girlBean = new Gson().fromJson(response.body().string(),new TypeToken<DevelopRandomGirlBean>(){}.getType());
                }

                return girlBean;
            }catch (Exception e){
                Log.e("exception------------>",e.getMessage() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(DevelopRandomGirlBean result) {
            listener.onComplete(result);
        }
    }

    public interface OnGetRandomGirlCompleteListener{
        void onComplete(DevelopRandomGirlBean result);
    }

}
