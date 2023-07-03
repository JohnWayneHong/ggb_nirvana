package com.jgw.common_library.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by 熊少武 on 2018/3/28 0028.
 */

public class CustomBaseRecyclerView extends RecyclerView {
    private RecyclerView.AdapterDataObserver mObserver;

    public CustomBaseRecyclerView(Context context) {
        super(context);
    }

    private boolean intercept = false;

    public CustomBaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }

    public CustomBaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    public void setVerticalLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        setLayoutManager(linearLayoutManager);
    }

    public void setVerticalLayoutManager(final boolean canScrollVertically) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;
            }
        };
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        setLayoutManager(linearLayoutManager);
    }

    public void setHorizontalLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(linearLayoutManager);
    }

    public void setGridVerticalLayoutManager(final int spanCount) {
        setGridVerticalLayoutManager(spanCount, getContext(), true);
    }


    public void setGridVerticalLayoutManager(final int spanCount, Context context, final boolean canScroll) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount) {
            @Override
            public boolean canScrollHorizontally() {
                return canScroll;
            }

            @Override
            public boolean canScrollVertically() {
                return canScroll;
            }
        };

        layoutManager.setOrientation(RecyclerView.VERTICAL);
        setLayoutManager(layoutManager);
    }

    public void setScrollToPosition(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return intercept || super.onInterceptTouchEvent(e);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (getLayoutManager() == null) {
            setVerticalLayoutManager();
        }
        Adapter<?> oldAdapter = getAdapter();
        if (oldAdapter != null && mObserver!=null) {
            oldAdapter.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null && mObserver!=null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
    }

    public void setChangeObserver(AdapterDataObserver observer) {
        mObserver =observer;
    }
}
