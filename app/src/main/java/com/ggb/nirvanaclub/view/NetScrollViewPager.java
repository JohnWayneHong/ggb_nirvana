package com.ggb.nirvanaclub.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class NetScrollViewPager extends ViewPager {

    private boolean noScroll = false;

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    private int mHeight = 0;

    /** 已经获取到的高度下标 ： 当前的高度 */
    private int mCurPosition = 0;

    /** 当前显示下标 */
    private int mPosition = 0;

    /** 按下标存储View历史高度 */
    private HashMap<Integer, Integer> mChildrenViews = new LinkedHashMap<Integer, Integer>();
    /** 记录页面是否存储了高度 */
    private HashMap<Integer, Boolean> indexList = new LinkedHashMap<Integer, Boolean>();

    /** 做自适应高度，必须先进行初始化标记 */
    public void initIndexList(int size) {
        mHeight = 0;
        mCurPosition = 0;
        mPosition = 0;
        for (int i = 0; i < size; i++) {
            /** 初始化高度存储状态 */
            indexList.put(i, false);
        }
    }

    public NetScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NetScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (indexList.size() > 0) {
            if (indexList.get(mPosition)) {
                height = mChildrenViews.get(mPosition);
            } else {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    if (h > height) {
                        height = h;
                    }
                }
                mHeight = height;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在viewpager 切换的时候进行更新高度
     */
    public void updateHeight(int current) {
        this.mPosition = current;
        if (indexList.size() > 0) {
            saveIndexData();
            if (indexList.get(current)) {
                int height = 0;
                if (mChildrenViews.get(current) != null) {
                    height = mChildrenViews.get(current);
                }
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                } else {
                    layoutParams.height = height;
                }
                setLayoutParams(layoutParams);
            }
            this.mCurPosition = current;
        }
    }

    /**
     * 保存已经测绘好的高度
     */
    private void saveIndexData() {
        if (!indexList.get(mCurPosition)) {
            /** 没保存高度时，保存 */
            indexList.put(mCurPosition, true);
            mChildrenViews.put(mCurPosition, mHeight);
        }
    }
 
}

