package com.aitangba.pagingview.core;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aitangba.pagingview.R;


/**
 * Created by fhf11991 on 2017/5/15.
 */

public class FooterViewHolder {

    public View itemView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public FooterViewHolder(View itemView) {
        this.itemView = itemView;

        mProgressBar = (ProgressBar) this.itemView.findViewById(R.id.footer_view_progressbar);
        mTextView = (TextView) this.itemView.findViewById(R.id.footer_view_tv);
    }

    public void bindView(boolean hasMoreData) {
        if(hasMoreData) {
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setText("加载更多数据中");
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextView.setText("没有更多数据了");
        }
    }
}
