package com.huawei.kirin.hiscout.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huawei.kirin.hiscout.R;


/**
 * Created by spock on 16-3-3.
 */
public class DesktopLayout extends LinearLayout {
    public DesktopLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(
                    R.layout.desklayout, null);
        this.addView(view);
    }
}
