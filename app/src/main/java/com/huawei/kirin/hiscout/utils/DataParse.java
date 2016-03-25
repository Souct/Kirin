package com.huawei.kirin.hiscout.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataParse {
        private static final String TAG = DataParse.class.getSimpleName();

        private  static String result;
        // 获取CPU最大频率（单位KHZ）
        // "/system/bin/cat" 命令行
        // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
        public static String getMaxCpuFreq(int which) {
                String path = "/sys/devices/system/cpu/cpu" + which + "/cpufreq/scaling_max_freq";
                try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        result = text.trim();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return result;
        }


         // 获取CPU最小频率（单位KHZ）
        public static String getMinCpuFreq(int which) {
                String path = "/sys/devices/system/cpu/cpu" + which + "/cpufreq/scaling_min_freq";
                try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        result = text.trim();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return result;
        }
        // 实时获取CPU当前频率（单位KHZ）
        public static String getCurCpuFreq(int which) {
                String path = "/sys/devices/system/cpu/cpu" + which + "/cpufreq/scaling_cur_freq";
                try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        result = text.trim();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return result;
        }
        public static String maxCpuFreq(int which) {
                String path = "/sys/devices/system/cpu/cpu" + which + "/cpufreq/scaling_max_freq";
                try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        result = text.trim();
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return result;
        }

        public static long getCpuCount(){
                class CpuFilter implements FileFilter{
                        @Override
                        public boolean accept(File file) {
                                if (Pattern.matches("cpu[0-9]",file.getName())){
                                        return true;
                                }
                                return false;
                        }
                }
                try {
                        File dir = new File("sys/devices/system/cpu");
                        File[] files = dir.listFiles(new CpuFilter());
                        Log.d(TAG,"cpu count = " + files.length);
                        return  files.length;
                }catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, "get CPU count if fail");
                        return 1;
                }

        }

        /*每一个long类型的值都是cpu信息里面的值，当前只是用来计算，需要知道各个意义的情自行查阅，这里不做赘述*/
        public static float getCpuLoad(){
                String path = "proc/stat";
                float cpuLoad;
                try{
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        Log.d(TAG,"cpu data = "+ text);
                        String[] array = text.split(" ");
                        long user = Long.parseLong(array[2]);
                        long nice = Long.parseLong(array[3]);
                        long system = Long.parseLong(array[4]);
                        long idle = Long.parseLong(array[5]);
                        long iowait = Long.parseLong(array[6]);
                        long hardirp = Long.parseLong(array[7]);
                        long softirp = Long.parseLong(array[8]);
                        long steal = Long.parseLong(array[9]);
                        long geuest = Long.parseLong(array[10]);
                        cpuLoad = 100 * (float)(user + nice + system + hardirp + softirp + steal)
                                / (float)(user + nice + system + idle + iowait + hardirp + softirp + steal + geuest);
                }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                        Log.d(TAG,"ArrayIndexOutOfBoundsException");
                        return 0;
                }catch (IOException e ){
                        Log.d(TAG, "IOException");
                        e.printStackTrace();
                        return 0;
                }
                return cpuLoad;
        }

        public static List<RunningAppInfos> getRunningAppList(Context context){
                PackageManager pm = context.getPackageManager();
                ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ApplicationInfo> allApplicationInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);//查询已安装应用程序
                List<ApplicationInfo> notSystemApp = new ArrayList<>();
                for (int i = 0; i < allApplicationInfoList.size(); i++) {
                        if ((allApplicationInfoList.get(i).flags & ApplicationInfo.FLAG_SYSTEM) <= 0){//过滤系统应用,只获取第三方应用
                                notSystemApp.add(allApplicationInfoList.get(i));
                        }
                }
                List<RunningAppProcessInfo> runningAppProcessInfoList = manager.getRunningAppProcesses();
                Map<String,RunningAppProcessInfo> runningAppProcessInfoMap = new HashMap<>();
                for (RunningAppProcessInfo appProcessInfo : runningAppProcessInfoList){
                        String [] pkgNameList = appProcessInfo.pkgList;
                        for (int i = 0; i < pkgNameList.length; i++) {
                                runningAppProcessInfoMap.put(pkgNameList[i],appProcessInfo);
                        }
                }
                List<RunningAppInfos> runningAppInfosList = new ArrayList<>();
                for (ApplicationInfo app : notSystemApp){
                        if (runningAppProcessInfoMap.containsKey(app.packageName)){
                                int pid = runningAppProcessInfoMap.get(app.packageName).pid;
                                String processName = runningAppProcessInfoMap.get(app.packageName).processName;
                             runningAppInfosList.add(getAppInfo(pm,app,pid,processName));
                        }
                }

                return runningAppInfosList;
        }
        private static RunningAppInfos getAppInfo(PackageManager pm ,ApplicationInfo app, int pid, String processName){
                RunningAppInfos appInfos = new RunningAppInfos();
                appInfos.setAppLabel((String) app.loadLabel(pm));
                appInfos.setAppIcon(app.loadIcon(pm));
                appInfos.setPackageName(app.packageName);
                appInfos.setPid(pid);
                appInfos.setProcessName(processName);
                return  appInfos;
        }
        public static List<Long> getRunningAppFreq(List<RunningAppInfos> list){
                Log.d(TAG,"list.size = " +list.size());
                List<Long> processCpuTimeList = new ArrayList<>();
                for (int i=0 ; i < list.size() ; i++){
                        String path = "proc/" + list.get(i).getPid() + "/stat";
                        FileReader fr = null;
                        try {
                                fr = new FileReader(path);
                                BufferedReader br = new BufferedReader(fr);
                                String text = br.readLine();
                                String[] array = text.split(" ");
                                long Utime = Long.parseLong(array[13]);
                                long Stime = Long.parseLong(array[14]);
                                long cutime = Long.parseLong(array[15]);
                                long cstime = Long.parseLong(array[16]);
                                long processCpuTime = Utime + Stime + cutime + cstime;
                                processCpuTimeList.add(processCpuTime);
                        } catch (FileNotFoundException e) {
                                e.printStackTrace();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return processCpuTimeList;
        }
        public static long getTotalCpuTime(){
                String path = "proc/stat";
                long totalCpuTime;
                try{
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        String text = br.readLine();
                        Log.d(TAG,"cpu data = "+ text);
                        String[] array = text.split(" ");
                        long user = Long.parseLong(array[2]);
                        long nice = Long.parseLong(array[3]);
                        long system = Long.parseLong(array[4]);
                        long idle = Long.parseLong(array[5]);
                        long iowait = Long.parseLong(array[6]);
                        long hardirp = Long.parseLong(array[7]);
                        long softirp = Long.parseLong(array[8]);
                        long steal = Long.parseLong(array[9]);
                        long geuest = Long.parseLong(array[10]);
                        totalCpuTime = user + nice + system + idle + iowait + hardirp + softirp + steal + geuest;
                }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                        Log.d(TAG,"ArrayIndexOutOfBoundsException");
                        return 0;
                }catch (IOException e ){
                        Log.d(TAG, "IOException");
                        e.printStackTrace();
                        return 0;
                }
                return totalCpuTime;
        }
}
