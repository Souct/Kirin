package com.huawei.kirin.hiscout;

import android.app.Application;

/**
 * Created by spock on 16-3-1.
 */
public class PMPApplication extends Application {

    private boolean isBackground;
    protected static PMPApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void setBackground(boolean background ){
        isBackground = background;
    }

    public boolean isBackground(){
        return isBackground;
    }

}
