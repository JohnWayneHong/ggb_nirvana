package com.jgw.common_library.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class InputFormatUtils implements LifecycleObserver {

    private int mIntegerLength;
    private int mDoubleLength;
    private EditText mEditText;
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            formatText(s.toString(), mIntegerLength, mDoubleLength, mEditText);
        }
    };

    @SuppressWarnings("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (mEditText != null) {
            mEditText.removeTextChangedListener(watcher);
        }
        mEditText = null;
        mIntegerLength = 0;
        mDoubleLength = 0;
    }

    public void limitInput(final int integerLength, final int doubleLength, final EditText editText) {
        limitInput(null, integerLength, doubleLength, editText);
    }

    public void limitInput(Lifecycle lifecycle, final int integerLength, final int doubleLength, final EditText editText) {
        mIntegerLength = integerLength;
        mDoubleLength = doubleLength;
        mEditText = editText;

        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }

        if (editText != null) {
            editText.removeTextChangedListener(watcher);
            editText.addTextChangedListener(watcher);
        }
    }

    public static void formatText(String str, int integerLength, int doubleLength, EditText editText) {
        int pointIndex = str.indexOf(".");
        int zeroIndex = str.indexOf("0");
        if (pointIndex == -1) {
            //整数
            //多位数,删除首位0
            if (zeroIndex == 0 && str.length() > 1) {
                String substring = str.substring(1);
                editText.setText(substring);
                editText.setSelection(substring.length());
                return;
            }
            //限制整数位长度
            if (str.length() > integerLength) {
                String substring = str.substring(0, integerLength);
                editText.setText(substring);
                editText.setSelection(substring.length());
            }

        } else {
            //小数(录入了".")
            //多位数,首位0后不是"."则删除0
            if (zeroIndex == 0 && str.length() > 1 && !String.valueOf(str.charAt(1)).equals(".")) {
                String substring = str.substring(1);
                editText.setText(substring);
                editText.setSelection(substring.length());
                return;
            }
            if (pointIndex == 0) {
                //首位是"."则在前边拼0
                String text = "0" + str;
                editText.setText(text);
                editText.setSelection(text.length());
            } else {
                //noinspection StatementWithEmptyBody
                if (pointIndex != str.length() - 1) {
                    boolean isChange = false;
                    String[] split = str.split("\\.");
                    String integerStr = split[0];
                    String doubleStr = "";
                    StringBuilder sb = new StringBuilder();
                    //整数位长度限制
                    if (integerStr.length() > integerLength) {
                        integerStr = integerStr.substring(0, integerLength);
                        isChange = true;
                    }
                    if (split.length > 1) {
                        doubleStr = split[1];

                        //小数位长度限制
                        if (doubleStr.length() > doubleLength) {
                            doubleStr = doubleStr.substring(0, doubleLength);
                            isChange = true;
                        }

                    }
                    if (isChange) {
                        sb.append(integerStr);
                        sb.append(".");
                        sb.append(doubleStr);
                        String text = sb.toString();
                        editText.setText(text);
                        editText.setSelection(text.length());
                    }

                } else {
//                                "."为末尾，无需处理
                }

            }
        }
    }

    public static String formatText(String str, int integerLength, int doubleLength) {
        int pointIndex = str.indexOf(".");
        int pointLastIndex = str.lastIndexOf(".");
        if (pointIndex != pointLastIndex) {
            return str.substring(0,pointLastIndex);
        }
        int zeroIndex = str.indexOf("0");
        String formatText = str;
        if (pointIndex == -1) {
            //整数
            //多位数,删除首位0
            while (zeroIndex == 0 && formatText.length() > 1) {
                formatText = formatText.substring(1);
            }
            //限制整数位长度
            if (formatText.length() > integerLength) {
                formatText = formatText.substring(0, integerLength);
            }

        } else {
            //小数(录入了".")
            //多位数,首位0后不是"."则删除0
            while (zeroIndex == 0 && formatText.length() > 1 && !String.valueOf(formatText.charAt(1)).equals(".")) {
                formatText = formatText.substring(1);
            }
            if (pointIndex == 0) {
                //首位是"."则在前边拼0
                formatText = "0" + formatText;
            } else {
                //noinspection StatementWithEmptyBody
                if (pointIndex != formatText.length() - 1) {
                    boolean isChange = false;
                    String[] split = formatText.split("\\.");
                    String integerStr = split[0];
                    String doubleStr = "";
                    StringBuilder sb = new StringBuilder();
                    //整数位长度限制
                    if (integerStr.length() > integerLength) {
                        integerStr = integerStr.substring(0, integerLength);
                        isChange = true;
                    }
                    if (split.length > 1) {
                        doubleStr = split[1];

                        //小数位长度限制
                        if (doubleStr.length() > doubleLength) {
                            doubleStr = doubleStr.substring(0, doubleLength);
                            isChange = true;
                        }

                    }
                    if (isChange) {
                        sb.append(integerStr);
                        sb.append(".");
                        sb.append(doubleStr);
                        formatText = sb.toString();
                    }

                } else {
//                                "."为末尾，无需处理
                }

            }
        }
        return formatText;
    }
}
