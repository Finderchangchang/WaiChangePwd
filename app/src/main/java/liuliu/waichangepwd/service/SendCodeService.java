package liuliu.waichangepwd.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.method.Utils;
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
        intentFilter = new IntentFilter();
        intentFilter.addAction("sms_received");
        GameAccount game = BaseApplication.getmOrder().get(0);//获得当前最新的一个账号名
        HttpUtil.load().openPwd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(str -> {
                }, error -> {
                    new Handler().postDelayed(() -> {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("nickName", game.getAccountNumber());
                        map.put("type", "findp");
                        HttpUtil.load().checkName(map)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(model -> {
                                    if (("S").equals(model.getRet())) {//判断昵称
                                        new Handler().postDelayed(() -> {
                                            sendCode(game);
                                        }, 2000);
                                    }
                                });
                    }, 3000);
                });
        return super.onStartCommand(intent, flags, startId);
    }

    IntentFilter intentFilter;

    private void sendCode(GameAccount game) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "findp");//ovPbFs9GEQidN3Wod-vQjNOawHxU
        map.put("nickName", game.getAccountNumber());
        map.put("openid", game.getOpenId());
        map.put("bindPhone", game.getPhone());
        if (Utils.isNetworkConnected()) {
            HttpUtil.load()
                    .sendMsg(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(val -> {
                        if (!("S").equals(val.getRet())) {
                            Toast.makeText(this, val.getMsg(), Toast.LENGTH_SHORT).show();
                            List<GameAccount> list = BaseApplication.getmOrder();
                            list.remove(0);
                            BaseApplication.setmOrder(list);
                        }
                    }, error -> {

                    });
        } else {
            Toast.makeText(this, "请检查网络连接是否正常~~", Toast.LENGTH_SHORT).show();
        }
    }
}
