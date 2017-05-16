package com.aitangba.pagingview.core;

import java.util.List;

/**
 * Created by fhf11991 on 2017/3/16.
 */

public interface PagingManager {

    void setAutoLoadEnabled(boolean enable);

    void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener);

    void startLoad(boolean refresh);

    <T> List<T> checkPaging(List<T> list);

    PageBean getPageBean();

}
