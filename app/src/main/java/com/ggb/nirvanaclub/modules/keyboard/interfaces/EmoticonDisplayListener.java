package com.ggb.nirvanaclub.modules.keyboard.interfaces;

import android.view.ViewGroup;

import com.ggb.nirvanaclub.modules.keyboard.adpater.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
