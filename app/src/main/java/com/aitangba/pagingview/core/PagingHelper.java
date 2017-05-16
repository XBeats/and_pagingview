package com.aitangba.pagingview.core;

/**
 * Created by fhf11991 on 2017/5/15.
 */

public class PagingHelper {

    private boolean isLoadingMore = false;  // 是否正在加载更多
    private boolean mHasMoreData = true;  //是否有更多数据，当没有更多数据时，不能进行自动加载更多
    private boolean mIsAutoLoadEnabled = true;  //是否使用自动加载
    private OnLoadMoreListener mLoadMoreListener;

    private final PageBean mPageBean = new PageBean();

    public void setAutoLoadEnabled(boolean enable) {
        mIsAutoLoadEnabled = enable;
    }

    public boolean isAutoLoadEnabled() {
        return mIsAutoLoadEnabled;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void onScrolled(boolean isLastPosition) {
        if(!mIsAutoLoadEnabled) {
            return;
        }

        if(!isLastPosition) {
            return;
        }

        if(isLoadingMore) { //
            return;
        }

        if(!mHasMoreData) {
            return;
        }

        if (mLoadMoreListener != null) {
            isLoadingMore = true;
            mLoadMoreListener.onLoadMore(false);
        }
    }

    public void startLoad(boolean refresh) {
        if(refresh) {
            mPageBean.reset();
        } else {
            mPageBean.increase();
        }
    }

    public boolean finishLoadMore(int dataSize) {
        boolean hasMoreData = dataSize == PageBean.PAGE_SIZE;

        //修正当前页
        if(dataSize == 0) {
            mPageBean.decline();
        }

        isLoadingMore = false;
        mHasMoreData = hasMoreData;

        return mHasMoreData;
    }

    public PageBean getPageBean() {
        return mPageBean;
    }

}
