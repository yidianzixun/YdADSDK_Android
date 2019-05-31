package com.yidian.geek.addemo.helper;

/**
 * Created by chenyichang on 2018/12/25.
 */
public class BackSupport {

    private long mLastBackTime;
    private static final long BACK_INTERNAL_TIME = 2500L;

    public boolean canBeBack() {
        boolean result;
        result = !(mLastBackTime == 0 || System.currentTimeMillis() - mLastBackTime > BACK_INTERNAL_TIME);
        mLastBackTime = System.currentTimeMillis();
        return result;
    }

}
