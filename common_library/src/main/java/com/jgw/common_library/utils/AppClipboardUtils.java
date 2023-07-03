package com.jgw.common_library.utils;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.jgw.common_library.base.CustomApplication;

/**
 * 剪切板信息copy
 */
public class AppClipboardUtils {

    //复制文案
    public static void copyText(String textCase) {
        if (TextUtils.isEmpty(textCase)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) CustomApplication.getCustomApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            cmb.setPrimaryClip(ClipData.newPlainText("text", textCase));
        }

        if (getClipboardFirstData().equals(textCase)) {
            ToastUtils.showToast("复制成功");
        } else {
            ToastUtils.showToast("复制失败");
        }
    }

    public static String getClipboardFirstData() {
        ClipboardManager manager = (ClipboardManager) CustomApplication.getCustomApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager == null) {
            return "";
        }
        ClipData primaryClip = manager.getPrimaryClip();
        if (primaryClip == null) {
            return "";
        }
        int clipDataSize = primaryClip.getItemCount();
        if (clipDataSize == 0) {
            return "";
        }
        ClipData.Item firstClipItem = primaryClip.getItemAt(0);
        if (firstClipItem == null) {
            return "";
        }
        CharSequence clipCharSequence = firstClipItem.getText();
        if (clipCharSequence == null) {
            return "";
        }
        return clipCharSequence.toString();
    }

    public static void clearClipboardFirstData() {
        ClipboardManager manager = (ClipboardManager) CustomApplication.getCustomApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            manager.setPrimaryClip(ClipData.newPlainText("", ""));
        }
    }
}
