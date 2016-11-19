package liuliu.waichangepwd.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.model.GameAccount;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/17.
 */

public class SendCodeService extends Service {
    public static final String ACTION = "liuliu.service.SendCodeService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "执行...", Toast.LENGTH_SHORT).show();
        sendCode();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendCode() {
        GameAccount game = BaseApplication.getmOrder().get(0);//获得当前最新的一个账号名
        Map<String, String> map = new HashMap<>();
        map.put("type", "findp");
//        map.put("nickName", "拜师快递");
//        map.put("openid", "ovPbFs9GEQidN3Wod-vQjNOawHxU");
//        map.put("bindPhone", "17093215800");
        map.put("nickName", game.getAccountNumber());
        map.put("openid", game.getOpenId());
        map.put("bindPhone", game.getPhone());
        HttpUtil.load()
                .sendMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val -> {
                    if (val.getMsg().contains("达到上限")) {
                        Toast.makeText(this, val.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {//1.ret="E",请求参数有误
                        Toast.makeText(this, val.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(this, "错误~~", Toast.LENGTH_SHORT).show();
                });
    }
}
