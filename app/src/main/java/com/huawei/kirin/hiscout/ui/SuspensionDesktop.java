package com.huawei.kirin.hiscout.ui;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.huawei.kirin.hiscout.MainView;
import com.huawei.kirin.hiscout.PMPActivity;
import com.huawei.kirin.hiscout.utils.Log;


public class SuspensionDesktop {
	private static final String TAG = SuspensionDesktop.class.getSimpleName();
	private MainView mMainView;
	private PMPActivity mActivity;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutpLayoutParams;
    private DesktopLayout mDesktopLayout;
    private boolean isMenuShow;
    private long startTime;
    // 声明屏幕的宽高
    private float x, y;
    private int top;

	
	public SuspensionDesktop(MainView mainView) {
		mMainView = mainView;
		mActivity = mainView.getActivity();
		createWindowManager();
		createDesktopLayout();
	}
	 private void createWindowManager() {
	        // 取得系统窗体
	        mWindowManager = (WindowManager) mActivity.getApplicationContext()
	                .getSystemService("window");
	        mLayoutpLayoutParams = new WindowManager.LayoutParams();// 窗体的布局样式
	        mLayoutpLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
	        mLayoutpLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置窗体焦点及触摸：FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)

	        mLayoutpLayoutParams.format = PixelFormat.RGBA_8888; // 设置显示的模式

	        mLayoutpLayoutParams.gravity = Gravity.TOP | Gravity.LEFT; // 设置对齐的方法
	        // 设置窗体宽度和高度
	        mLayoutpLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
	        mLayoutpLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

	    }
	private void createDesktopLayout() {
        mDesktopLayout = new DesktopLayout(mActivity);
        mDesktopLayout.setOnTouchListener(new OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                x = event.getRawX();
                y = event.getRawY() - top; // 25是系统状态栏的高度
                Log.d(TAG, "startX" + mTouchStartX + "====startY"
                        + mTouchStartY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取相对View的坐标，即以此View左上角为原点
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        Log.d(TAG, "startX" + mTouchStartX + "====startY"
                                + mTouchStartY);
                        long end = System.currentTimeMillis() - startTime;
                        // 双击的间隔在 300ms以下
                        if (end < 300) {
                            closeDesk();
                        }
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 更新浮动窗口位置参数
                        mLayoutpLayoutParams.x = (int) (x - mTouchStartX);
                        mLayoutpLayoutParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayoutpLayoutParams);
                        break;
                    case MotionEvent.ACTION_UP:

                        // 更新浮动窗口位置参数
                        mLayoutpLayoutParams.x = (int) (x - mTouchStartX);
                        mLayoutpLayoutParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayoutpLayoutParams);

                        // 可以在此记录最后一次的位置
                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });
    }

	public void showDesktop(){
		Log.d(TAG, "showDeskTop");
        isMenuShow = true;
        mWindowManager.addView(mDesktopLayout, mLayoutpLayoutParams);
       // mActivity.finish();
    }
    public void closeDesk(){
    	Log.d(TAG, "CloseDesktop");
        isMenuShow = false;
        mWindowManager.removeView(mDesktopLayout);
        //mActivity.finish();
    }

    public boolean isMenuShow(){
        return  isMenuShow;
    }
	public void setTop(int top) {
		this.top = top;
	}
}
