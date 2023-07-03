package com.jgw.common_library.utils.click_utils.listener;

import android.view.View;

/**
 * Created by xsw on 2016/11/1.
 */
public interface OnItemSingleClickListener extends ClickUtilsListener{
    void onItemClick(View view, int position);
    void onItemClick(View view, int groupPosition, int subPosition);
    void onItemClick(View view, int firstPosition, int secondPosition, int thirdPosition);
}
