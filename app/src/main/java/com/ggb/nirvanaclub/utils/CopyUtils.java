package com.ggb.nirvanaclub.utils;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tamsiree.rxkit.RxTool;

/**
 * 复制到剪贴板以及用浏览器打开
 *
 * @author HongWeijie
 * @date 2023/4/28-下午6:58
 */
public class CopyUtils {

    public static void copyText(@NonNull String text) {
//        ClipboardManager clipboardManager = (ClipboardManager) Utils.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipboardManager clipboardManager = (ClipboardManager) RxTool.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(text);
    }

    public static void copyText(@NonNull TextView textView) {
        copyText(textView.getText().toString());
    }

    /**
     * 浏览器打开
     */
    public static void openBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
