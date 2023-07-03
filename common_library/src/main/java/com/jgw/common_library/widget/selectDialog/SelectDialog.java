package com.jgw.common_library.widget.selectDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jgw.common_library.R;
import com.jgw.common_library.base.adapter.CustomRecyclerAdapter;
import com.jgw.common_library.base.view.CustomBaseRecyclerView;
import com.jgw.common_library.utils.click_utils.listener.OnItemSingleClickListener;

import java.util.List;

public class SelectDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private CustomBaseRecyclerView mRvList;

    private Context mContext;
    private List<String> mList;
    private OnSelectDialogItemClickListener mListener;
    private Integer mMaxHeight;

    private SelectDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public static SelectDialog newInstance(Context context) {
        return new SelectDialog(context);
    }

    public SelectDialog setContentList(List<String> list) {
        mList = list;
        return this;
    }

    public SelectDialog setOnItemClickListener(OnSelectDialogItemClickListener listener) {
        mListener = listener;
        return this;
    }

    public SelectDialog setDialogCancelable(boolean cancelable) {
        this.setCancelable(cancelable);
        return this;
    }

    public SelectDialog setDialogListMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);
        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(mContext, android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题栏
        mDialogView = getLayoutInflater().inflate(R.layout.bottom_dialog, null, false);
        setContentView(mDialogView);
        initView();
        showWithAnimation();
    }

    private void initView() {
        TextView tvCancel = mDialogView.findViewById(R.id.tv_cancel);
        mRvList = mDialogView.findViewById(R.id.rv_dialog_list);

        if (mMaxHeight != null) {
            int height = mRvList.getLayoutParams().height;
            mRvList.getLayoutParams().height = Math.min(height,mMaxHeight);
        }
        tvCancel.setOnClickListener(this);
        initAdapter();
    }

    private void initAdapter() {
        CustomRecyclerAdapter<String> mAdapter = new SelectDialogAdapter();
        mRvList.setAdapter(mAdapter);
        mAdapter.notifyRefreshList(mList);
        mAdapter.setOnItemClickListener(new OnItemSingleClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mListener != null) {
                    mListener.onSingleItemClick(view, position,mList.get(position) ,SelectDialog.this);
                }
                dismissWithAnimation();
            }

            @Override
            public void onItemClick(View view, int groupPosition, int subPosition) {
            }

            @Override
            public void onItemClick(View view, int firstPosition, int secondPosition, int thirdPosition) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onCancel();
        }
        dismissWithAnimation();
    }

    public void showWithAnimation() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(100);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mDialogView.startAnimation(slide);
    }

    public void dismissWithAnimation() {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        slide.setDuration(100);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mDialogView.startAnimation(slide);
    }

    public interface OnSelectDialogItemClickListener {
        void onSingleItemClick(View view, int position, String string,SelectDialog dialog);
        default void onCancel(){};
    }
}

