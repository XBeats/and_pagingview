package com.aitangba.pagingview.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


import com.aitangba.pagingview.R;

import java.util.Collections;
import java.util.List;


/**
 * Created by fhf11991 on 2017/5/11.
 */

public class PagingListView extends ListView implements PagingManager {

    private OnScrollListener mOnScrollListener;
    private FooterViewHolder mFooterViewHolder;

    private PagingHelper mPagingHelper = new PagingHelper();

    public PagingListView(Context context) {
        this(context, null);
    }

    public PagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View footerView = LayoutInflater.from(context).inflate(R.layout.layout_footer_view, null);
        setFooterViewHolder(new FooterViewHolder(footerView));

        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if(mFooterViewHolder == null) {
                    return;
                }

                int childCount = getChildCount();
                if(childCount == 0) {
                    return;
                }
                if(getChildAt(childCount - 1) == mFooterViewHolder.itemView) {
                    mPagingHelper.onScrolled(view.getLastVisiblePosition() + 1 ==  view.getCount());
                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    @Override
    public void setAutoLoadEnabled(boolean enable) {
        mPagingHelper.setAutoLoadEnabled(enable);
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mPagingHelper.setOnLoadMoreListener(loadMoreListener);
    }

    @Override
    public void startLoad(boolean refresh) {
        mPagingHelper.startLoad(refresh);
    }

    @Override
    public <T> List<T> checkPaging(List<T> list) {
        if(list == null) {
            list = Collections.EMPTY_LIST;
        }

        boolean hasMoreData = mPagingHelper.finishLoadMore(list.size());
        updateFooterStatus(hasMoreData);
        return list;
    }

    @Override
    public PageBean getPageBean() {
        return mPagingHelper.getPageBean();
    }

    public void setFooterViewHolder(FooterViewHolder footerViewHolder) {
        mFooterViewHolder = footerViewHolder;
        addFooterView(mFooterViewHolder.itemView);
    }

    private void updateFooterStatus(boolean hasMoreData) {
        if(mFooterViewHolder == null) {
            return;
        }
        mFooterViewHolder.bindView(hasMoreData);
    }
}
