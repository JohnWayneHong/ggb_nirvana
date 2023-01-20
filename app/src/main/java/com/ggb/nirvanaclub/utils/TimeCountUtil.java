package com.ggb.nirvanaclub.utils;

import android.os.CountDownTimer;

public class TimeCountUtil extends CountDownTimer {

    public TimeCountUtil(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        listener.onTickChange(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        listener.OnFinishChanger();
    }

    public interface OnCountDownListener{
        void onTickChange(long millisUntilFinished);
        void OnFinishChanger();
    }

    private OnCountDownListener listener;

    public OnCountDownListener getListener() {
        return listener;
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        this.listener = listener;
    }
}
