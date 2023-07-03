package com.jgw.common_library.utils;

import android.content.Context;

import com.jgw.common_library.base.ui.BaseActivity;
import com.jgw.common_library.widget.selectDialog.SelectDialog;

import java.util.List;

public class SelectDialogUtil {

    public static void showSelectDialog(Context context, List<String> contentList, SelectDialog.OnSelectDialogItemClickListener listener) {
        if (contentList == null || contentList.isEmpty()) {
            return;
        }
        SelectDialog.newInstance(context)
                .setContentList(contentList)
                .setOnItemClickListener(listener)
                .setDialogCancelable(false)
                .show();
    }

    public static void showSelectDialog(Context context, List<String> contentList, int maxHeight, SelectDialog.OnSelectDialogItemClickListener listener) {
        if (contentList == null || contentList.isEmpty()) {
            return;
        }
        maxHeight=(int)(maxHeight* BaseActivity.getXMultiple());
        SelectDialog.newInstance(context)
                .setContentList(contentList)
                .setOnItemClickListener(listener)
                .setDialogCancelable(false)
                .setDialogListMaxHeight(maxHeight)
                .show();
    }
}
