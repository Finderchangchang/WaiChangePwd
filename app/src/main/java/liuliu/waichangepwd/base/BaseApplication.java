package liuliu.waichangepwd.base;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {
    private static Context context;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, "99a9de341f584e294351fcc26e156014");
    }

    public static Context getContext() {
        return context;
    }

    //系统处于资源匮乏的状态
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}