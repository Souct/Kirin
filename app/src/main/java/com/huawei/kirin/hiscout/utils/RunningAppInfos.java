package com.huawei.kirin.hiscout.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by spock on 2016/3/23.
 */
public class RunningAppInfos {
    private String label;
    private String packageName;
    private String processName;
    private int pid;
    private Drawable drawable;

    public void setAppLabel(String s) {
        label = s;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppIcon(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setProcessName(String processName) {
        this.processName = processName ;
    }

    public void setPid(int pid) {
        this.pid = pid ;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getLabel() {
        return label;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getPid() {
        return pid;
    }

    public String getProcessName() {
        return processName;
    }
}
