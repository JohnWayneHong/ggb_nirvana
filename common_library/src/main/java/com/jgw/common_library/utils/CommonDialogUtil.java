package com.jgw.common_library.utils;

import android.content.Context;
import android.text.InputType;

import com.jgw.common_library.widget.commonDialog.CommonDialog;

public class CommonDialogUtil {

    public static CommonDialog showSelectDialog(Context context, CharSequence title, CharSequence detail,
                                                String leftText, String rightText, CommonDialog.OnButtonClickListener listener) {
        CommonDialog dialogFragment = CommonDialog.newInstance(context);
        dialogFragment.setCancelable(false);
        dialogFragment.setCustomTitle(title)
                .setDialogType(CommonDialog.TYPE_SELECT_DIALOG)
                .setDetails(detail)
                .setLeftButtonStr(leftText)
                .setRightButtonStr(rightText)
                .setOnButtonClickListener(listener)
                .show();
        return dialogFragment;
    }

    // single button
    public static CommonDialog showConfirmDialog(Context context, String title, CharSequence detail,
                                                 String confirmText, CommonDialog.OnButtonClickListener listener) {
        CommonDialog dialogFragment = CommonDialog.newInstance(context);
        dialogFragment.setCancelable(false);
        dialogFragment.setCustomTitle(title)
                .setDialogType(CommonDialog.TYPE_CONFIRM_DIALOG)
                .setDetails(detail)
                .setRightButtonStr(confirmText)
                .setOnButtonClickListener(listener)
                .show();
        return dialogFragment;
    }

    // input && double button
    public static CommonDialog showInputDialog(Context context, String title, String hint, String leftText,
                                               String rightText, CommonDialog.OnButtonClickListener listener) {
        return showInputDialog(context, title, hint, leftText, rightText, InputType.TYPE_CLASS_TEXT, listener);
    }

    public static CommonDialog showInputDialog(Context context, String title, String hint, String leftText,
                                               String rightText, int inputType, CommonDialog.OnButtonClickListener listener) {
        return showInputDialog(context, title, hint, "",leftText, rightText, inputType, listener);
    }

    public static CommonDialog showInputDialog(Context context, String title, String hint, String input,String leftText,
                                               String rightText, int inputType, CommonDialog.OnButtonClickListener listener) {
        CommonDialog dialogFragment = CommonDialog.newInstance(context);
        dialogFragment.setCancelable(false);
        dialogFragment.setCustomTitle(title)
                .setDialogType(CommonDialog.TYPE_INPUT_DIALOG)
                .setInputText(input)
                .setInputHintText(hint)
                .setLeftButtonStr(leftText)
                .setRightButtonStr(rightText)
                .setOnButtonClickListener(listener)
                .setOnInputType(inputType)
                .show();
        return dialogFragment;
    }
}
