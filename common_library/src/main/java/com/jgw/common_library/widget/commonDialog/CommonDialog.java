package com.jgw.common_library.widget.commonDialog;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.jgw.common_library.R;
import com.jgw.common_library.base.view.CustomDialog;
import com.jgw.common_library.bean.CommonDialogBean;

public class CommonDialog extends CustomDialog implements View.OnClickListener {

    public static final int TYPE_CONFIRM_DIALOG = 1;
    public static final int TYPE_SELECT_DIALOG = 2;
    public static final int TYPE_INPUT_DIALOG = 3;

    private OnButtonClickListener mOnButtonClickListener;
    private com.jgw.common_library.databinding.DialogFragmentCommonBinding viewDataBinding;
    private final CommonDialogBean mData = new CommonDialogBean();

    public CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public static CommonDialog newInstance(Context context) {
        return newInstance(context, R.style.CustomDialog);
    }

    public static CommonDialog newInstance(Context context, int resId) {
        return new CommonDialog(context, resId);
    }

    private void init() {
        LayoutInflater inflater = getLayoutInflater();
        viewDataBinding = DataBindingUtil.inflate(inflater
                , R.layout.dialog_fragment_common, null, false);
        viewDataBinding.setData(mData);
        viewDataBinding.tvDialogCommonLeft.setOnClickListener(this);
        viewDataBinding.tvDialogCommonRight.setOnClickListener(this);
        viewDataBinding.tvDialogCommonRight.setOnClickListener(this);
        viewDataBinding.tvDialogContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        setContentView(viewDataBinding.getRoot());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == viewDataBinding.tvDialogCommonLeft.getId()) {
            onLeftClick();
        } else if (id == viewDataBinding.tvDialogCommonRight.getId()) {
            onRightClick();
        }
    }

    private void onRightClick() {
        switch (mData.getType()) {
            case CommonDialog.TYPE_CONFIRM_DIALOG:
            case CommonDialog.TYPE_SELECT_DIALOG:
                doRightButtonClick();
                break;
            case CommonDialog.TYPE_INPUT_DIALOG:
                hideSoftKeyboard();
                doRightButtonClick();
                break;
        }
    }

    private void onLeftClick() {
        switch (mData.getType()) {
            case CommonDialog.TYPE_CONFIRM_DIALOG:
                //do nothing
                break;
            case CommonDialog.TYPE_SELECT_DIALOG:
                doLeftButtonClick();
                break;
            case CommonDialog.TYPE_INPUT_DIALOG:
                hideSoftKeyboard();
                doLeftButtonClick();
                break;
        }
    }

    public CommonDialog setDialogType(int type) {
        mData.setType(type);
        return this;
    }

    public CommonDialog setCustomTitle(CharSequence title) {
        if (title instanceof String) {
            mData.setTitle((String) title);
        } else if (title instanceof Spanned) {
            mData.setTitleSpanned((Spanned) title);
        }
        return this;
    }

    public CommonDialog setDetails(CharSequence details) {
        if (details instanceof String) {
            mData.setDetails((String) details);
        } else if (details instanceof Spanned) {
            mData.setDetailsSpanned((Spanned) details);
        }
        return this;
    }

    public CommonDialog setInputText(String input) {
        mData.setInput(input);
        return this;
    }
    public CommonDialog setInputHintText(String hintText) {
        mData.setInputHint(hintText);
        return this;
    }

    public CommonDialog setLeftButtonStr(String leftButtonStr) {
        mData.setLeft(leftButtonStr);
        return this;
    }

    public CommonDialog setRightButtonStr(String rightButtonStr) {
        mData.setRight(rightButtonStr);
        return this;
    }

    public CommonDialog setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
        return this;
    }

    public CommonDialog setOnInputType(int type) {
        viewDataBinding.etDialogInput.setInputType(type);
        return this;
    }

    private void doLeftButtonClick() {
        if (mOnButtonClickListener == null) {
            dismiss();
            return;
        }
        if (!mOnButtonClickListener.isRightConfirm()) {
            boolean inputOk = mOnButtonClickListener.onInput(mData.getInput());
            if (!inputOk) {
                return;
            }
        }
        mOnButtonClickListener.onLeftClick();
        boolean autoDismiss = mOnButtonClickListener.onAutoDismiss();
        if (autoDismiss) {
            dismiss();
        }
    }

    private void doRightButtonClick() {
        if (mOnButtonClickListener == null) {
            dismiss();
            return;
        }
        if (mOnButtonClickListener.isRightConfirm()) {
            boolean inputOk = mOnButtonClickListener.onInput(mData.getInput());
            if (!inputOk) {
                return;
            }
        }
        mOnButtonClickListener.onRightClick();
        boolean autoDismiss = mOnButtonClickListener.onAutoDismiss();
        if (autoDismiss) {
            dismiss();
        }
    }


    public interface OnButtonClickListener {
        default void onLeftClick() {
        }

        default void onRightClick() {
        }

        default boolean isRightConfirm() {
            return true;
        }

        default boolean onInput(String input) {
            return true;
        }

        default boolean onAutoDismiss() {
            return true;
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void dismiss() {
        try {
            hideSoftKeyboard();
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}