package com.ggb.nirvanaclub.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ggb.nirvanaclub.R;


public class LoadingDialog extends Dialog {

    private TextView tv_loading;
    private ImageView iv_loading;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_view);
        setCanceledOnTouchOutside(false);

        tv_loading = findViewById(R.id.tv_loading_text);
        iv_loading = findViewById(R.id.iv_loading);
    }


    public void showLoading(String text){
        show();
        tv_loading.setText(text);

        Animation loadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation);
        iv_loading.setAnimation(loadingAnimation);
        loadingAnimation.start();
    }

}
