package liuliu.waichangepwd.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.ISettingView;

/**
 * Created by Administrator on 2016/11/26.
 */

public class SettingListener implements ISettingMView {
    private IWXAPI api;
    private Context mContext;
    private ISettingView mView;

    public SettingListener(Context mContext, ISettingView mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void checkPay() {
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < 7) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    mContext,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        }
    }

    @Override
    public void loadShare() {
        wechatShare(1);
    }

    String orderId = "";
    UserModel model;

    @Override
    public void loadPay(double money) {
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < 7) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    mContext,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        } else {
            //调起微信支付
            BP.pay("账号充值", "商品描述", money, false, new PListener() {
                @Override
                public void orderId(String s) {
                    orderId = s;
                }

                @Override
                public void succeed() {
                    BP.query(orderId, new QListener() {
                        @Override
                        public void succeed(String status) {
                            if (("SUCCESS").equals(status)) {
                                BmobQuery<UserModel> query = new BmobQuery<UserModel>();
                                query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
                                    @Override
                                    public void done(UserModel userModel, BmobException e) {
                                        model = userModel;
                                        Double db = model.getYue();
                                        model.setYue(money + db);
                                        model.update(Utils.getCache("key"), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    mView.payResult(true);
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                mView.payResult(false);
                            }
                        }

                        @Override
                        public void fail(int code, String reason) {

                        }
                    });
                }

                @Override
                public void fail(int i, String s) {
                    if (i == -3) {
                        Toast.makeText(mContext, "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗)," +
                                "安装结束后重新支付", Toast.LENGTH_SHORT).show();
                        installBmobPayPlugin("bp.db");
                    } else {
                        Toast.makeText(mContext, "支付中断!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void unknow() {

                }
            });
        }
    }

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.dakedaojia.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "提示";
        msg.description = "您的账号：" + flag + "\n您的密码：";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api = WXAPIFactory.createWXAPI(mContext, "wx83a95cef0734f331", true);
        api.registerApp("wx83a95cef0734f331");
        api.sendReq(req);
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = mContext.getAssets().open(fileName);
            String name = Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk";
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } catch (Exception e) {

        }
    }
}

interface ISettingMView {
    void checkPay();//检查支付

    void loadShare();//调起分享

    void loadPay(double money);//调起支付
}
