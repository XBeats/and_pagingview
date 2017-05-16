package com.aitangba.pagingview;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.aitangba.pagingview.core.OnLoadMoreListener;
import com.aitangba.pagingview.core.PagingRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Adapter mAdapter;
    private PagingRecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (PagingRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                Log.d("TAG", "onLoadMore ---");
                loadData();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new Adapter());

        View view = findViewById(R.id.emptyView);
        mRecyclerView.setEmptyView(view);

        TextView textView = new TextView(this);
        textView.setText("title");
        mRecyclerView.setHeaderView(textView);

        findViewById(R.id.emptyTextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.mList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

        loadData();
    }

    private List<String> getData(int size) {
        List<String> list = new ArrayList<>(size);
        for(int i= 0; i< size ; i++) {
            list.add( "名字" + (i + page * 10));
        }
        return list;
    }

    private int page = 0;
    private void loadData() {
        page = page + 1;

        if(page > 3) return;
        findViewById(Window.ID_ANDROID_CONTENT).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(page == 1 || page == 2) {
                    mAdapter.setData(mRecyclerView.checkPaging(getData(10)));
                } else if (page == 3) {
                    mAdapter.setData(mRecyclerView.checkPaging(getData(2)));
                }

            }
        }, 2000);
    }

    static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> mList = new ArrayList<>();

        public void setData(List<String> items) {
            mList.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_light_adapter, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(mList.get(position));

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.name_text);
        }
    }
}
