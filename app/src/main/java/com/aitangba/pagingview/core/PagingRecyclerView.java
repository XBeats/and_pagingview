package com.aitangba.pagingview.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aitangba.pagingview.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by fhf11991 on 2017/5/11.
 */

public class PagingRecyclerView extends RecyclerView implements PagingManager {

    private static final int TYPE_HEADER_VIEW = 1001; //header类型 Item
    private static final int TYPE_FOOTER_VIEW = 1002; //footer类型 Item

    private EasyAdapter mEasyAdapter;
    private View mEmptyView;
    private View mHeaderView;
    private FooterViewHolder mFooterViewHolder;

    private PagingHelper mPagingHelper = new PagingHelper();

    public PagingRecyclerView(Context context) {
        this(context, null);
    }

    public PagingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFooterView(new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_footer_view, null)));

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mEasyAdapter == null) {
                    return;
                }

                if(mFooterViewHolder == null) {
                    return;
                }

                int childCount = getChildCount();
                if(childCount == 0) {
                    return;
                }
                LayoutManager layoutManager = getLayoutManager();
                if(layoutManager.getChildAt(childCount - 1) == mFooterViewHolder.itemView) {
                    int lastPosition = findLastVisibleItemPosition(layoutManager);
                    mPagingHelper.onScrolled(lastPosition + 1 == mEasyAdapter.getItemCount());
                }
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            return;
        }
        super.setAdapter(mEasyAdapter = new EasyAdapter(adapter));
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
        updateEmptyStatus();
        updateFooterStatus(hasMoreData);
        return list;
    }

    @Override
    public PageBean getPageBean() {
        return mPagingHelper.getPageBean();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        if(mEasyAdapter != null) {
            mEasyAdapter.notifyDataSetChanged();
        }
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        updateEmptyStatus();
    }

    private void updateEmptyStatus() {
        if(mEmptyView == null) {
            return;
        }

        if(mEasyAdapter == null) {
            return;
        }

        final boolean empty = (mEasyAdapter.mAdapter.getItemCount() == 0);

        if(empty) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void setFooterView(FooterViewHolder footerViewHolder) {
        mFooterViewHolder = footerViewHolder;
        if(mEasyAdapter != null) {
            mEasyAdapter.notifyDataSetChanged();
        }
    }

    private void updateFooterStatus(boolean hasMoreData) {
        if(mFooterViewHolder == null) {
            return;
        }
        mFooterViewHolder.bindView(hasMoreData);
    }

    @Override
    public void setLayoutManager(final LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layout);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(mEasyAdapter != null && mEasyAdapter.getItemViewType(position) == TYPE_FOOTER_VIEW) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            int max = lastVisibleItemPositions[0];
            for (int value : lastVisibleItemPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
        return -1;
    }

    private class EasyAdapter extends RecyclerView.Adapter {

        private RecyclerView.Adapter mAdapter;

        public EasyAdapter(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_HEADER_VIEW) {
                return new ViewHolder(mHeaderView) {};
            } else if(viewType == TYPE_FOOTER_VIEW) {
                return new ViewHolder(mFooterViewHolder.itemView) {};
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(getItemViewType(position) == TYPE_HEADER_VIEW) {

            } else if(getItemViewType(position) == TYPE_FOOTER_VIEW) {

            } else {
                mAdapter.onBindViewHolder(holder, mHeaderView == null ? position : position - 1);
            }
        }

        @Override
        public int getItemCount() {
            final boolean hasHeader = mHeaderView != null;
            final boolean hasFooter = mPagingHelper.isAutoLoadEnabled()
                    && mFooterViewHolder != null
                    && mFooterViewHolder.itemView != null;

            final int commonItemCount = mAdapter.getItemCount();
            final int headerCount = hasHeader ? 1 : 0;
            final int footerCount;
            if(commonItemCount == 0) {
                footerCount = 0;
            } else {
                footerCount = hasFooter ? 1 : 0;
            }
            return commonItemCount + headerCount + footerCount;
        }

        @Override
        public int getItemViewType(int position) {
            final boolean hasHeader = mHeaderView != null;
            final int commonItemCount = mAdapter.getItemCount();

            if(hasHeader) {
                if(position == 0) {
                    return TYPE_HEADER_VIEW;
                } else if(position > 1 + (commonItemCount - 1)) {
                    return TYPE_FOOTER_VIEW;
                } else {
                    return mAdapter.getItemViewType(position);
                }
            } else {
                if(position > commonItemCount - 1) {
                    return TYPE_FOOTER_VIEW;
                } else {
                    return mAdapter.getItemViewType(position);
                }
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }
}
