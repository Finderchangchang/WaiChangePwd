package liuliu.waichangepwd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.model.OrderModel;
import liuliu.waichangepwd.model.UserModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/10.
 */

public class SmsReciver extends BroadcastReceiver {
    public static SmsReciver mIntail;
    @Override
    public void onReceive(Context context, Intent intent) {
        mIntail=this;
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
                        map.put("password", Utils.MD5(pwd));
                        map.put("confirmPassword", Utils.MD5(pwd));
                        map.put("openid", list.getPhoneId().getOpenid());
                        map.put("identityCard", "");//空
                        map.put("bindPhone", list.getPhoneId().getPhonenumber());
                        map.put("messageCode", m.group());
                        new Handler().postDelayed(() -> {
                            HttpUtil.load().changePwd(map)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(model -> {
                                        boolean result;
                                        if (("s").equals(model.getRet()) || ("S").equals(model.getRet())) {
                                            result = true;
                                            Intent intents = new Intent();
                                            intents.setAction("action.refreshFriend");//通知更新ui
                                            BaseApplication.getContext().sendBroadcast(intents);
                                            load();
                                            GameAccount game = new GameAccount();
                                            game.setPassword(pwd);//修改数据库密码
                                            game.setState("未租");
                                            game.update(list.getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    //修改成功。
                                                }
                                            });
                                        } else {
                                            result = false;
                                            Toast.makeText(BaseApplication.getContext(), model.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                        //在数据库存订单
                                        OrderModel order = new OrderModel();
                                        order.setState(result);
                                        order.setGameid(list);
                                        order.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {

                                            }
                                        });
                                        List<GameAccount> lists = BaseApplication.getmOrder();
                                        lists.remove(0);//移除第一个
                                        BaseApplication.setmOrder(lists);
                                        if (BaseApplication.getmOrder().size() > 0) {
                                            new Handler().postDelayed(() -> {
                                                Intent intents = new Intent(BaseApplication.getContext(), SendCodeService.class);
                                                intents.setAction(SendCodeService.ACTION);
                                                BaseApplication.getContext().startService(intents);
                                            }, 3000);
                                        } else {
                                            BaseApplication.getContext().unregisterReceiver(this);//关闭当前接受短信服务
                                        }
                                    }, error -> {

                                    });
                        }, 3000);
                    }
                }
            }
        }
    }

    private void load() {
        BmobQuery<UserModel> query = new BmobQuery<>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (e == null) {
                    userModel.setYue(userModel.getYue() - 0.5);
                    userModel.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Toast.makeText(BaseApplication.getContext(), "扣费失败~~", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public static String getPwd() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            long result;
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
