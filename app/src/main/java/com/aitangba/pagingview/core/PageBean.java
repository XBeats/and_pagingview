package com.aitangba.pagingview.core;

/**
 * Created by XBeats on 2017/3/26.
 */

public class PageBean {

    public final static int PAGE_SIZE = 10;
    public final static int ORIGIN_PAGE = 1;

    public volatile int currentPage = 1;

    private boolean refresh;

    public void setRefresh(boolean refresh){
        this.refresh = refresh;

        if(this.refresh) {
            reset();
        } else {
            increase();
        }
    }

    public void reset() {
        currentPage = 1;
    }

    public void increase() {
        currentPage = currentPage + 1;
    }

    public void decline() {
        currentPage = Math.max(ORIGIN_PAGE, currentPage - 1);
    }
}
