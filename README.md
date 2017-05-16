# PagingView
RecyclerView,ListView分页显示解决方案



##firstly##

use `com.aitangba.pagingview.core.PagingRecyclerView` in your xml.

##secondly##

```java
mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                Log.d("TAG", "onLoadMore ---");
                loadData();
            }
        });
```

##finally##

```java
mAdapter.setData(mRecyclerView.checkPaging(getData(2)));
```

