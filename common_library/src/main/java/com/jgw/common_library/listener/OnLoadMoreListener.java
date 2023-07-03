package com.jgw.common_library.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by XiongShaoWu  滑动到底时触发
 * on 2020/7/29
 */
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private boolean isDownScroll;
    /*存储瀑布流每一列最下面的那个item的位置*/
    private int[] mLastPositions;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //noinspection RedundantIfStatement
        if (dy > 0) {
            isDownScroll = true;
        } else {
            isDownScroll = false;
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();


        int lastVisibleItem;

        // 当不滚动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (manager instanceof GridLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
            } else if (manager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                if (mLastPositions == null) {
                    mLastPositions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                }
                ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(mLastPositions);
                lastVisibleItem = findMax(mLastPositions);
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
            int totalItemCount = manager.getItemCount();

            // 判断是否滚动到底部，并且是向下滚动
            if (lastVisibleItem == (totalItemCount - 1) && isDownScroll) {
                //加载更多功能的代码
                onScrollToLastItem();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public abstract void onScrollToLastItem();
}
