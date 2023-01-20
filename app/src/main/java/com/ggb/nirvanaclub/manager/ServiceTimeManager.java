package com.ggb.nirvanaclub.manager;

import android.util.Log;

public class ServiceTimeManager {

    private static ServiceTimeManager INSTANCE = null;

    private boolean isActive = false;
    private long curTime = 0L;
    private int second = 0;

    private ServiceTimeManager(){

    }

    public static ServiceTimeManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ServiceTimeManager();
        }
        return INSTANCE;
    }


    public void init(){
        if(!isActive){
            this.isActive = true;
            this.second = 0;
            this.curTime = System.currentTimeMillis();
            new CycleThread().start();
        }
    }

    public void stop(){
        this.isActive = false;
        this.second = 0;
        this.curTime = 0L;
    }

    class CycleThread extends Thread{
        @Override
        public void run() {
            while (isActive){
                try {
                    if(mOnTimeSecondUpdateListener!=null){
                        mOnTimeSecondUpdateListener.onSecondUpdate(curTime);
                    }
                    second++;
                    curTime+=1000;
                    if(second>60){
                        second = 0;
                        if (mOnTimeSecondUpdateListener!=null){
                            Log.e("TAG", "run发送请求北京时间: " );
                            mOnTimeSecondUpdateListener.onUpdateTime();
                        }
                    }
                    Thread.sleep(1000);
                }catch (Exception e){
                    second = 0;
                }
            }
        }
    }

    public void setCurServiceTime(long time){
        this.curTime = time;
    }

    public long currentTimeMillis(){
        return this.curTime>0?this.curTime:System.currentTimeMillis();
    }

    public interface OnTimeSecondUpdateListener{
        void onSecondUpdate(long mills);
        void onUpdateTime();
    }

    private OnTimeSecondUpdateListener mOnTimeSecondUpdateListener;

    public void registerTimeUpdateListener(OnTimeSecondUpdateListener mOnTimeSecondUpdateListener){
        this.mOnTimeSecondUpdateListener = mOnTimeSecondUpdateListener;
    }

    public void unregisterTimeUpdateListener(){
        this.mOnTimeSecondUpdateListener = null;
    }

}
