package com.bameng.utils;

import com.bameng.BuildConfig;
import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * Created by Administrator on 2016/11/28.
 */

public class AppBlockCanaryContext extends BlockCanaryContext {
    public AppBlockCanaryContext() {
        super();
    }

    @Override
    public int provideMonitorDuration() {
        return super.provideMonitorDuration();
    }

    @Override
    public int provideBlockThreshold() {
        return super.provideBlockThreshold();
    }

    @Override
    public boolean displayNotification() {
        return BuildConfig.DEBUG;
    }
}
