package com.jgw.common_library.utils.click_utils;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.jgw.common_library.R;
import com.jgw.common_library.base.adapter.CustomRecyclerAdapter;
import com.jgw.common_library.utils.LogUtils;
import com.jgw.common_library.utils.ToastUtils;
import com.jgw.common_library.utils.click_utils.listener.OnDoubleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemDoubleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemLongTimeClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnItemSingleClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnLongTimeClickListener;
import com.jgw.common_library.utils.click_utils.listener.OnSingleClickListener;


/**
 * Created by 熊少武 on 2018/5/11 0011.
 */

class ClickUtilsImpl implements View.OnClickListener, View.OnLongClickListener {

    private OnSingleClickListener mOnSingleClickListener;
    private OnDoubleClickListener mOnDoubleClickListener;
    private OnLongTimeClickListener mOnLongTimeClickListener;
    private OnItemSingleClickListener mOnItemSingleClickListener;
    private OnItemDoubleClickListener mOnItemDoubleClickListener;
    private OnItemLongTimeClickListener mOnItemLongTimeClickListener;
    private CustomRecyclerAdapter<?>.ContentViewHolder<? extends ViewDataBinding> mViewHolder;
    private boolean isSingleClick;
    private boolean isDoubleClick;
    private boolean isItemSingleClick;
    private boolean isItemDoubleClick;

    public void setSingleClick(boolean singleClick) {
        isSingleClick = singleClick;
    }

    public void setDoubleClick(boolean doubleClick) {
        isDoubleClick = doubleClick;
    }

    public void setItemSingleClick(boolean itemSingleClick) {
        isItemSingleClick = itemSingleClick;
    }

    public void setItemDoubleClick(boolean itemDoubleClick) {
        isItemDoubleClick = itemDoubleClick;
    }

    private View lastClickView;

    public void clear() {
        lastClickView = null;
    }

    private static final int tagKey = R.string.key_tag;

    private final long DEFAULT_DOUBLE_HIT_TIME = 500;
    private long mDoubleHitTime = DEFAULT_DOUBLE_HIT_TIME;

    @Override
    public void onClick(View v) {
        LogUtils.xswShowLog("onClick--------------------------------");
        //        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeMillis = System.nanoTime() / 1000000L;
        boolean switchClick;
        long lastTime;
        Object tag = v.getTag(tagKey);

        if (lastClickView != null && lastClickView != v) {
            lastClickView.setTag(tagKey, 0L);
            switchClick = true;
        } else {
            if (tag == null) {
                switchClick = true;
            } else {
                lastTime = (long) tag;
                long l = currentTimeMillis - lastTime;
                switchClick = l > mDoubleHitTime;
            }
        }
        lastClickView = v;

        if (mViewHolder != null) {
            if (mOnItemDoubleClickListener != null && isItemDoubleClick) {
                if (tag == null) {
                    v.setTag(tagKey, currentTimeMillis);
                } else {
                    lastTime = (long) tag;
                    if (currentTimeMillis - lastTime < mDoubleHitTime) {
                        mOnItemDoubleClickListener.onItemDoubleClick(v, mViewHolder.getAdapterPosition());
                        lastTime = 0;
                        v.setTag(tagKey, lastTime);
                        return;
                    } else {
                        v.setTag(tagKey, currentTimeMillis);
                    }
                }
            }
            if (mOnItemSingleClickListener != null && isItemSingleClick && switchClick) {
                int adapterPosition = mViewHolder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    ToastUtils.showToast("界面刷新中...请稍后再试");
                    return;
                }
                mOnItemSingleClickListener.onItemClick(v, adapterPosition);
                v.setTag(tagKey, currentTimeMillis);
            }
        }

        if (mOnDoubleClickListener != null && isDoubleClick) {
            if (tag == null) {
                v.setTag(tagKey, currentTimeMillis);
            } else {
                lastTime = (long) tag;
                if (currentTimeMillis - lastTime < mDoubleHitTime) {
                    mOnDoubleClickListener.onDoubleClick(v);
                    lastTime = 0;
                    v.setTag(tagKey, lastTime);
                    return;
                } else {
                    v.setTag(tagKey, currentTimeMillis);
                }
            }
        }
        if (mOnSingleClickListener != null && isSingleClick && switchClick) {
            mOnSingleClickListener.onClick(v);
            v.setTag(tagKey, currentTimeMillis);
        }
    }

    public void setOnSingleClickListener(OnSingleClickListener onSingleClickListener) {
        this.mOnSingleClickListener = onSingleClickListener;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.mOnDoubleClickListener = onDoubleClickListener;
    }

    public void setOnLongTimeClickListener(OnLongTimeClickListener onLongTimeClickListener) {
        this.mOnLongTimeClickListener = onLongTimeClickListener;
    }

    public void setOnItemSingleClickListener(OnItemSingleClickListener onItemSingleClickListener) {
        this.mOnItemSingleClickListener = onItemSingleClickListener;
    }

    public void setOnItemDoubleClickListener(OnItemDoubleClickListener onItemDoubleClickListener) {
        this.mOnItemDoubleClickListener = onItemDoubleClickListener;
    }

    public void setOnItemLongTimeClickListener(OnItemLongTimeClickListener onItemLongTimeClickListener) {
        this.mOnItemLongTimeClickListener = onItemLongTimeClickListener;
    }

    public void setViewHolder(CustomRecyclerAdapter<?>.ContentViewHolder<? extends ViewDataBinding> viewHolder) {
        this.mViewHolder = viewHolder;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mViewHolder != null && mOnItemLongTimeClickListener != null) {
            return mOnItemLongTimeClickListener.onItemLongClick(v, mViewHolder.getAdapterPosition());
        } else {
            return mOnLongTimeClickListener == null || mOnLongTimeClickListener.onLongClick(v);
        }
    }

    public void setDoubleHitTime(long doubleHitTime) {
        mDoubleHitTime = doubleHitTime;
    }
    public long getDoubleHitTime() {
        return mDoubleHitTime;
    }
}
