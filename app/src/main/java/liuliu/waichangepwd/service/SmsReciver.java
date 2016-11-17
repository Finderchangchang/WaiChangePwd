package liuliu.waichangepwd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/10.
 */

public class SmsReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                if (msg.getMessageBody().contains("波克城市")) {
                    Pattern p = Pattern.compile("\\d{2,}");// 这个2是指连续数字的最少个数
                    Matcher m = p.matcher(msg.getMessageBody());
                    while (m.find()) {
                        List<GameAccount> list = BaseApplication.getmOrder();
                        list.remove(0);
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "findp");
                        map.put("nickName", "拜师快递");//昵称
//                        map.put("nickName", Utils.getCache("nickName"));//昵称
                        map.put("password", Utils.MD5("a11111"));
                        map.put("confirmPassword", Utils.MD5("a11111"));
                        map.put("openid", "ovPbFs9GEQidN3Wod-vQjNOawHxU");//ovPbFs9GEQidN3Wod-vQjNOawHxU
                        map.put("identityCard", "");//空
                        map.put("bindPhone", "17093215800");
                        map.put("messageCode", m.group());
                        HttpUtil.load().changePwd(map)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(model -> {
                                    if (("s").equals(model.getRet()) || ("S").equals(model.getRet())) {
                                        Toast.makeText(BaseApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(BaseApplication.getContext(), model.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }, error -> {
                                    Toast.makeText(BaseApplication.getContext(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }
        }
    }

}
