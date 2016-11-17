package liuliu.waichangepwd.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
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
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        String openId = intent.getStringExtra("openId");
//        sendCode();
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String openId = intent.getStringExtra("openId");
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendCode() {
        GameAccount game = BaseApplication.getmOrder().get(0);//获得当前最新的一个账号名
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "findp");
//        map.put("nickName", "拜师快递");
//        map.put("openid", "ovPbFs9GEQidN3Wod-vQjNOawHxU");
//        map.put("bindPhone", "17093215800");
        map.put("nickName", game.getAccountNumber());
        map.put("openid", "");
        map.put("bindPhone", game.getPhone());
        HttpUtil.load()
                .sendMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val -> {
                    if (val.getMsg().contains("达到上限")) {
                        Toast.makeText(this, val.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, val.getMsg(), Toast.LENGTH_SHORT).show();
                        BaseApplication.getmOrder().remove(0);
                    }
                }, error -> {
                    Toast.makeText(this, "错误~~", Toast.LENGTH_SHORT).show();
                });
    }
}
