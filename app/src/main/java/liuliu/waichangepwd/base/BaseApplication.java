package liuliu.waichangepwd.base;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import liuliu.waichangepwd.model.GameAccount;

public class BaseApplication extends Application {
    private static Context context;
    public static List<GameAccount> mOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mOrder = new ArrayList<>();
        Bmob.initialize(this, "99a9de341f584e294351fcc26e156014");
    }

    public static void setContext(Context context) {
        BaseApplication.context = context;
    }

    public static List<GameAccount> getmOrder() {
        return mOrder;
    }

    public static void setmOrder(List<GameAccount> mOrder) {
        BaseApplication.mOrder = mOrder;
    }

    public static Context getContext() {
        return context;
    }
}