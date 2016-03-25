package com.huawei.kirin.hiscout.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.kirin.hiscout.PMPActivity;


/**
 * Created by spock on 16-3-1.
 */
public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected PMPActivity mActivity;
    protected int mLayoutId;
    protected View mainView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity =(PMPActivity) activity;
    }

    public void setLayoutId(int layoutId){
        mLayoutId = layoutId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(mLayoutId,null);
        return mainView;
    }
}
