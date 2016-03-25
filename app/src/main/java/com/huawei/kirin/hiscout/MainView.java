package com.huawei.kirin.hiscout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.huawei.kirin.hiscout.ui.BaseFragment;
import com.huawei.kirin.hiscout.ui.SlidingMenu;
import com.huawei.kirin.hiscout.ui.SuspensionDesktop;
import com.huawei.kirin.hiscout.utils.DataParse;
import com.huawei.kirin.hiscout.utils.ChartView;
import com.huawei.kirin.hiscout.utils.Log;


/**
 * Created by spock on 16-3-2.
 */
public class MainView extends RelativeLayout {

    private static final String TAG = MainView.class.getSimpleName();
    private static final int MSG_DATA_CHANGE =  0;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private BaseFragment mCurrentFragment;
    private PMPActivity mActivity;
    private View mParentView;
    private SlidingMenu mSlidingMenu;
    private RelativeLayout mLineChartView;
    private ChartView mCharView;
    private Button mShowMenuButton;
    private SuspensionDesktop mSuspensionDesktop;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_DATA_CHANGE:
                    mCharView.RefreshSeriesTask(msg.arg1);
            }
        }
    };

    public MainView(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context){
    	mParentView = LayoutInflater.from(context).inflate(R.layout.main_view,null);
        super.addView(mParentView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initView() {
    	mSlidingMenu = (SlidingMenu) mParentView.findViewById(R.id.slidingmenu);
    	mSlidingMenu.setController(this,mActivity);
        mLineChartView = (RelativeLayout) mParentView.findViewById(R.id.line_chart_view);
    	mShowMenuButton = (Button) mParentView.findViewById(R.id.main_show_menu);
        mCharView = new ChartView(this,mLineChartView);
        mCharView.initChart();
    	Log.d(TAG, "mShowMenu = " + mShowMenuButton);
    	mShowMenuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                Log.d(TAG, "onClick");
				mSlidingMenu.toggle();
                if (!mSuspensionDesktop.isMenuShow()){
                    mSuspensionDesktop.showDesktop();
                }
                /*Log.d(TAG, "getMaxCpuFreq  " + DataParse.getMaxCpuFreq(0)
                        + "getMinCpuFreq  " + DataParse.getMinCpuFreq(0)
                        + "getCurCpuFreq " + DataParse.getCurCpuFreq(0)
                        + "getCpuLoad " + DataParse.getCpuLoad());
                for (int i = 0 ;i < DataParse.getRunningAppList(mActivity).size(); i++ ) {
                    Log.d(TAG, "getRunningapp name = " + DataParse.getRunningAppList(mActivity).get(i).getLabel());
                    Log.d(TAG, "getRunningapp pid = " + DataParse.getRunningAppList(mActivity).get(i).getPid());
                    Log.d(TAG, "getRunningAppFreq = " + DataParse.getRunningAppFreq(DataParse.getRunningAppList(mActivity)));
                }
                Log.d(TAG, "getTotalCpuTime = " + DataParse.getTotalCpuTime());*/
			}
		});
	}
    public void onCreate(PMPActivity mainActivity) {
        mActivity = mainActivity;
        mFragmentManager = mainActivity.getFragmentManager();
        mSuspensionDesktop = new SuspensionDesktop(this);
        initView();
    }

    private void updateLineChart(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                int XM = 0;
                while (!PMPApplication.instance.isBackground()){
                    Message message = new Message();
                    message.what = MSG_DATA_CHANGE;
                    message.arg1 = (Integer.parseInt(DataParse.getCurCpuFreq(0))/10000);
                    mHandler.sendMessage(message);
                    try {
                        sleep(mActivity.getSharedPreferences(PMPActivity.TAG,0).getInt(PMPActivity.GET_TIME,500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Rect rect = new Rect();
        // /取得整个视图部分,注意，如果要设置标题样式，这个必须出现在标题样式之后，否则会出错
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int top;
        top = rect.top;//状态栏的高度，所以rect.height,rect.width分别是系统的高度的宽度

        Log.d(TAG, "top = " + top);
        mSuspensionDesktop.setTop(top);
        }


    public PMPActivity getActivity(){
    	return mActivity;
    }
    private  void removeCurrentFragment(BaseFragment mFramgent){
        if (mFramgent == null) {
            Log.d(TAG, "CurrentFragment is null");
            return;
        }
        Log.d(TAG, "removeCurrentFragment = " + mFramgent);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.remove(mFramgent);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    private void showNextFragment(BaseFragment mFragment){
        if (mFragment == null) {
            Log.d(TAG, "showNextFragment is null");
            return;
        }
        Log.d(TAG, "showNextFragment is " + mFragment);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.main_view, mFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"onSaveInstanceState");
        if (mCharView != null) {
            mCharView.onSaveInstanceState(savedInstanceState);
        }
    }
    public void onRestoreInstanceState(Bundle outState){
        Log.d(TAG,"onRestoreInstanceState");
        if (mCharView != null) {
            mCharView.onRestoreInstanceState(outState);
        }
    }
    public void onResume() {
        updateLineChart();
    }
    public void onPause() {
    }
}
