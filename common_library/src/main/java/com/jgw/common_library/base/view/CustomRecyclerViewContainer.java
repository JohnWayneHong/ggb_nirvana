package com.jgw.common_library.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.recyclerview.widget.RecyclerView;

import com.jgw.common_library.base.adapter.CustomRecyclerAdapter;

public class CustomRecyclerViewContainer extends LinearLayout {

    private View emptyView;
    private CustomBaseRecyclerView mRecyclerView;

    public CustomRecyclerViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            throw new IllegalStateException("not find child view!");
        }
        mRecyclerView = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof CustomBaseRecyclerView) {
                mRecyclerView = (CustomBaseRecyclerView) child;
                mRecyclerView.setChangeObserver(observer);
                break;
            }
        }
        if (mRecyclerView == null) {
            throw new IllegalStateException("not find child view!");
        }

    }

    public RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkEmpty();
        }
    };


    private void checkEmpty() {
        if (emptyView == null || mRecyclerView.getAdapter() == null) {
            return;
        }
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        boolean emptyViewVisible = false;
        if (adapter instanceof CustomRecyclerAdapter) {
            emptyViewVisible = ((CustomRecyclerAdapter<?>) adapter).getAdapterItemCount() == 0;
        }
        emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
    }


    public void setEmptyLayout(@IdRes int resId) {
        emptyView = LayoutInflater.from(getContext()).inflate(resId, this, false);
        if (!(emptyView instanceof NestedScrollingChild2)){
            throw new IllegalStateException("empty view must be implements NestedScrollingChild!");
        }
        emptyView.setVisibility(VISIBLE);
        addView(emptyView);
    }
}
