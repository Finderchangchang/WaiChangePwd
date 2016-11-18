package liuliu.waichangepwd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
                        GameAccount list = BaseApplication.getmOrder().get(0);
                        Map<String, String> map = new HashMap<>();
                        String pwd = getPwd();
                        map.put("type", "findp");
                        map.put("nickName", list.getAccountNumber());//昵称
//                        map.put("nickName", Utils.getCache("nickName"));//昵称
                        map.put("password", Utils.MD5(pwd));
                        map.put("confirmPassword", Utils.MD5(pwd));
                        map.put("openid", list.getOpenId());//ovPbFs9GEQidN3Wod-vQjNOawHxU
                        map.put("identityCard", "");//空
                        map.put("bindPhone", list.getPhone());
                        map.put("messageCode", m.group());
                        HttpUtil.load().changePwd(map)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(model -> {
                                    if (("s").equals(model.getRet()) || ("S").equals(model.getRet())) {
                                        Toast.makeText(BaseApplication.getContext(), "修改成功,当前密码为：" + pwd, Toast.LENGTH_SHORT).show();
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

    public static String getPwd() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            long result = 0;
            switch (i) {
                case 5:
                    sb.append(new Random().nextInt(10));
                    break;
                default:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char) result));
                    break;
            }

        }
        return sb.toString();
    }

}
