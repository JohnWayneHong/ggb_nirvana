package com.ggb.nirvanaclub.modules.keyboard.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.ggb.nirvanaclub.modules.keyboard.data.PageEntity;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
