package com.android.hcbd.dailylog;

import android.app.Application;
import android.os.Environment;

import com.android.hcbd.dailylog.manager.GreenDaoManager;
import com.blankj.utilcode.util.Utils;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;

/**
 * Created by guocheng on 2017/12/13.
 */

public class MyApplication extends Application{

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        GreenDaoManager.getInstance();
        Utils.init(this);
        PgyCrashManager.register(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
