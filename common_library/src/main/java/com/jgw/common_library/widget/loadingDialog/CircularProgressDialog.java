package com.jgw.common_library.widget.loadingDialog;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.jgw.common_library.R;
import com.jgw.common_library.base.view.CustomDialog;
import com.jgw.common_library.databinding.DialogFragmentCountLoadingBinding;
import com.jgw.common_library.databinding.DialogFragmentCycleLoadingBinding;
import com.jgw.common_library.databinding.DialogFragmentPercentageLoadingBinding;

public class CircularProgressDialog extends CustomDialog {

    private ViewDataBinding view;
    private int progressType;

    public CircularProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static CircularProgressDialog newInstance(Context context, int progressType) {
        return newInstance(context, progressType, R.style.CustomDialog);
    }

    /**
     * @param progressType -1无限循环 1百分比进度 2计数进度
     * @return
     */
    public static CircularProgressDialog newInstance(Context context, int progressType, int resId) {
        CircularProgressDialog dialogFragment = new CircularProgressDialog(context, resId);
        dialogFragment.setProgressType(progressType);
        return dialogFragment;
    }

    private void setProgressType(int progressType) {
        this.progressType = progressType;
        switch (progressType) {
            case 1:
                view = buildPercentageDialogView();
                break;
            case 2:
                view = buildCountDialogView();
                break;
            default:
                view = buildCycleDialogView();
        }
        setContentView(view.getRoot());
        setCount(0);
        setTips("加载中,请等待...");
    }

    private ViewDataBinding buildCycleDialogView() {
        LayoutInflater inflater = getLayoutInflater();
        DialogFragmentCycleLoadingBinding viewDataBinding = DataBindingUtil.inflate(inflater
                , R.layout.dialog_fragment_cycle_loading, null, false);
        viewDataBinding.cpvDialogFragmentCycleLoading.setProgressMode(0);
        viewDataBinding.cpvDialogFragmentCycleLoading.startRotate();
        return viewDataBinding;
    }

    private ViewDataBinding buildPercentageDialogView() {
        LayoutInflater inflater = getLayoutInflater();
        DialogFragmentPercentageLoadingBinding viewDataBinding = DataBindingUtil.inflate(inflater
                , R.layout.dialog_fragment_percentage_loading, null, false);
        viewDataBinding.cpvDialogFragmentPercentageLoading.setProgressMode(1);
        viewDataBinding.cpvDialogFragmentPercentageLoading.setTotal(100);
        return viewDataBinding;
    }

    private ViewDataBinding buildCountDialogView() {
        LayoutInflater inflater = getLayoutInflater();
        return DataBindingUtil.inflate(inflater
                , R.layout.dialog_fragment_count_loading, null, false);
    }

    public void setCount(int count) {
        switch (progressType) {
            case 1:
                setPercentageLoadingCount(count);
                break;
            case 2:
                setCountLoadingCount(count);
                break;
            default:
                setCycleLoadingCount(count);
        }
    }

    public void addCount(int count) {
        setCount(getCount() + count);
    }

    public int getCount() {
        switch (progressType) {
            case 1:
                return ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading.getCount();
            case 2:
                return ((DialogFragmentCountLoadingBinding) view).pbDialogFragmentCountLoading.getProgress();
            default:
                return 0;
        }
    }

    private void setCycleLoadingCount(int count) {
        //something
    }

    private void setPercentageLoadingCount(int count) {
        CircularProgressView progressView = ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading;
        progressView.setCount(count);

        String source = (int) progressView.getProgress() + "%";
        SpannableString span = new SpannableString(source);
        span.setSpan(new AbsoluteSizeSpan(12, true), source.length() - 1, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((DialogFragmentPercentageLoadingBinding) view).tvDialogFragmentPercentageLoading.setText(span);
    }

    private void setCountLoadingCount(int count) {
        ((DialogFragmentCountLoadingBinding) view).pbDialogFragmentCountLoading.setProgress(count);
        ((DialogFragmentCountLoadingBinding) view).tvDialogFragmentCountLoadingCount.setText(String.valueOf(count));
    }

    public void setTotal(int total) {
        switch (progressType) {
            case 1:
                setPercentageLoadingTotal(total);
                break;
            case 2:
                setTotalLoadingTotal(total);
                break;
            default:
                setCycleLoadingTotal(total);
        }
    }

    private void setCycleLoadingTotal(int total) {
    }

    private void setPercentageLoadingTotal(int total) {
        ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading.setTotal(total);
    }

    private void setTotalLoadingTotal(int total) {
        ((DialogFragmentCountLoadingBinding) view).pbDialogFragmentCountLoading.setProgress(total);
    }


    public void setTips(String tip) {
        switch (progressType) {
            case 1:
                ((DialogFragmentPercentageLoadingBinding) view).tvDialogFragmentPercentageLoadingTips.setText(tip);
                break;
            case 2:
                ((DialogFragmentCountLoadingBinding) view).tvDialogFragmentCountLoadingTips.setText(tip);
                break;
            default:
                ((DialogFragmentCycleLoadingBinding) view).tvDialogFragmentCycleLoadingTips.setText(tip);
        }
    }

    public void setLoadProgressFinishListener(OnLoadingProgressFinishListener listener) {
        if (view instanceof DialogFragmentPercentageLoadingBinding) {
            ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading.setLoadingProgressFinishListener(listener);
        }
    }

    public void setUseProgressRange(boolean useProgressRange) {
        if (view instanceof DialogFragmentPercentageLoadingBinding) {
            ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading.setUseProgressRange(useProgressRange);
        }
    }

    public int getProgressType() {
        return progressType;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (view instanceof DialogFragmentCycleLoadingBinding) {
            ((DialogFragmentCycleLoadingBinding) view).cpvDialogFragmentCycleLoading.stopRotate();
        } else if (view instanceof DialogFragmentPercentageLoadingBinding) {
            ((DialogFragmentPercentageLoadingBinding) view).cpvDialogFragmentPercentageLoading.stopRotate();
        }
    }

    public interface OnLoadingProgressFinishListener {
        void onFinish();
    }
}