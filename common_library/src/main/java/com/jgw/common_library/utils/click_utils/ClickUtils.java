package com.jgw.common_library.utils.click_utils;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.jgw.common_library.base.adapter.CustomRecyclerAdapter;
import com.jgw.common_library.base.ui.BaseActivity;
import com.jgw.common_library.base.ui.BaseFragment;
import com.jgw.common_library.utils.click_utils.listener.ClickUtilsListener;
import com.jgw.common_library.utils.click_utils.listener.OnDoubleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemDoubleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemLongTimeClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemSingleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnLongTimeClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnSingleClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 熊少武 on 2018/5/11 0011.
 * 集成单击 双击 长按的工具类
 * 一个view应一次注册所需点击监听
 * 重复注册会替换之前注册
 */
@SuppressWarnings("rawtypes")
public class ClickUtils {

    private ClickUtils() {
    }

    //普通View
    public static ClickManager register(final BaseActivity activity) {

        return new ClickUtils().getClickManager(activity);
    }

    public static ClickManager register(final BaseFragment fragment) {

        return new ClickUtils().getClickManager(fragment);
    }

    private ClickManager getClickManager(final BaseFragment fragment) {
        return new ClickManager(fragment);
    }

    private ClickManager getClickManager(final BaseActivity activity) {
        return new ClickManager(activity);
    }

    //Item
    public static ItemClickManager register(final CustomRecyclerAdapter.ContentViewHolder viewHolder) {

        return new ClickUtils().getItemClickManager(viewHolder);
    }

    private ItemClickManager getItemClickManager(final CustomRecyclerAdapter.ContentViewHolder viewHolder) {
        return new ItemClickManager(viewHolder);
    }

    public static class ClickManager implements ClickUtilsListener {
        private ClickUtilsListener mListener;
        private ClickUtilsImpl mListenerImpl;
        private ArrayList<View> views = new ArrayList<>();
        private boolean isSingleClick;
        private boolean isDoubleClick;
        private boolean isLongClick;

        @SuppressWarnings("unused")
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {
            for (View v : views) {
                v.setOnClickListener(null);
            }
            views.clear();
            mListenerImpl.clear();
        }

        ClickManager(BaseFragment fragment) {
            mListener = fragment;
            fragment.getLifecycle().addObserver(this);
            if (mListenerImpl == null) {
                mListenerImpl = new ClickUtilsImpl();
            }
        }

        ClickManager(BaseActivity activity) {
            activity.getLifecycle().addObserver(this);
            mListener = activity;
            if (mListenerImpl == null) {
                mListenerImpl = new ClickUtilsImpl();
            }
        }

        public ClickManager addView(final View... view) {
            views.addAll(Arrays.asList(view));
            return this;
        }

        public ClickManager addOnClickListener() {
            isSingleClick = true;
            mListenerImpl.setSingleClick(true);
            mListenerImpl.setOnSingleClickListener((OnSingleClickListener) mListener);
            return this;

        }

        public ClickManager addOnDoubleClickListener() {
            isDoubleClick = true;
            mListenerImpl.setDoubleClick(true);
            mListenerImpl.setOnDoubleClickListener((OnDoubleClickListener) mListener);
            return this;

        }

        public ClickManager addOnLongClickListener() {
            isLongClick = true;
            mListenerImpl.setOnLongTimeClickListener((OnLongTimeClickListener) mListener);
            return this;

        }

        public ClickManager setDoubleHitTime(long time) {
            mListenerImpl.setDoubleHitTime(time);
            return this;
        }

        public void submit() {
            if (!isSingleClick && !isDoubleClick && !isLongClick) {
                throw new IllegalStateException("not set listener");
            }
            for (int i = 0; i < views.size(); i++) {
                if (isSingleClick || isDoubleClick)
                    views.get(i).setOnClickListener(mListenerImpl);
                if (isLongClick)
                    views.get(i).setOnLongClickListener(mListenerImpl);
            }
        }
    }

    public static class ItemClickManager implements LifecycleObserver {
        private ClickUtilsListener mListener;
        private ClickUtilsImpl mListenerImpl;
        private ArrayList<View> views = new ArrayList<>();
        private boolean isItemSingleClick;
        private boolean isItemDoubleClick;
        private boolean isItemLongClick;


        @SuppressWarnings("unused")
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {
            for (View v : views) {
                v.setOnClickListener(null);
            }
            views.clear();
            mListenerImpl.clear();
        }

        ItemClickManager(CustomRecyclerAdapter.ContentViewHolder viewHolder) {
            if (viewHolder.getContext() instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) viewHolder.getContext();
                activity.getLifecycle().addObserver(this);
            }
            mListener = viewHolder;
            if (mListenerImpl == null) {
                mListenerImpl = new ClickUtilsImpl();
            }

        }

        public ItemClickManager addView(final View... view) {
            views.addAll(Arrays.asList(view));
            return this;
        }

        public ItemClickManager addView(List<? extends View> view) {
            views.addAll(view);
            return this;
        }

        public ItemClickManager addOnItemLongClickListener() {
            isItemLongClick = true;
            mListenerImpl.setOnItemLongTimeClickListener((OnItemLongTimeClickListener) mListener);
            return this;
        }

        public ItemClickManager addItemDoubleClickListener() {
            isItemDoubleClick = true;
            mListenerImpl.setItemDoubleClick(true);
            mListenerImpl.setOnItemDoubleClickListener((OnItemDoubleClickListener) mListener);
            return this;
        }

        public ItemClickManager addItemClickListener() {
            isItemSingleClick = true;
            mListenerImpl.setItemSingleClick(true);
            mListenerImpl.setOnItemSingleClickListener((OnItemSingleClickListener) mListener);
            return this;
        }

        public ItemClickManager setDoubleHitTime(long time) {
            mListenerImpl.setDoubleHitTime(time);
            return this;
        }

        public void submit() {
            if (!isItemSingleClick && !isItemDoubleClick && !isItemLongClick) {
                throw new IllegalStateException("not set listener");
            }
            mListenerImpl.setViewHolder((CustomRecyclerAdapter.ContentViewHolder) mListener);
            for (int i = 0; i < views.size(); i++) {
                if (isItemSingleClick || isItemDoubleClick)
                    views.get(i).setOnClickListener(mListenerImpl);
                if (isItemLongClick)
                    views.get(i).setOnLongClickListener(mListenerImpl);
            }
        }
    }
}
