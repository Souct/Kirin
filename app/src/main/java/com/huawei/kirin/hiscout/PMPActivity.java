package com.huawei.kirin.hiscout;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.huawei.kirin.hiscout.utils.Log;


public class PMPActivity extends Activity {

    private MainView mMainView;
    public static final String TAG = PMPActivity.class.getSimpleName();
    public static final String GET_TIME = "get_time";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMainView = new MainView(this);
        setContentView(mMainView);
        mMainView.onCreate(this);
        getSharedPreferences(TAG,0).edit().putInt(GET_TIME, 500);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMainView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMainView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
    	Log.d(TAG, "onResume");
        super.onResume();
        mMainView.onResume();
        PMPApplication.instance.setBackground(false);
    }

    @Override
    protected void onPause() {
    	Log.d(TAG, "OnPause");
        mMainView.onPause();
        super.onPause();
        PMPApplication.instance.setBackground(true);
    }
    @Override
    protected void onDestroy() {
    	Log.d(TAG, "OnDestroy");
    	super.onDestroy();
    }
}
