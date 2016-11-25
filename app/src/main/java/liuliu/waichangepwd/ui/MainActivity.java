package liuliu.waichangepwd.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.tsz.afinal.annotation.view.CodeNote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.listener.getOpenIDListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.OpenIdModel;
import liuliu.waichangepwd.model.PhoneNumberManager;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.service.SendCodeService;
import liuliu.waichangepwd.view.MyDialog;
import liuliu.waichangepwd.view.getOpenidView;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MainActivity extends BaseActivity implements getOpenidView {
    @CodeNote(id = R.id.bd_openid1_iv)
    private TextView bd_openid1_iv;
    @CodeNote(id = R.id.add_tel1_tv)
    private TextView add_tel1_tv;
    @CodeNote(id = R.id.add_tel1_btn)
    private TextView add_tel1_btn;
    @CodeNote(id = R.id.add_tel2_tv)
    private TextView add_tel2_tv;
    @CodeNote(id = R.id.add_tel2_btn)
    private TextView add_tel2_btn;
    @CodeNote(id = R.id.user_id_tv)
    private TextView user_id_tv;
    @CodeNote(id = R.id.yue_tv)
    private TextView yue_tv;
    @CodeNote(id = R.id.rl_bottem)
    private LinearLayout rl_bottem;
    private MyDialog myDialog;
    @CodeNote(id = R.id.add_tel1_ll)
    LinearLayout add_tel1_ll;
    @CodeNote(id = R.id.add_tel2_ll)
    LinearLayout add_tel2_ll;
    private getOpenIDListener loadlistener;
    private OpenIdModel openIdModel;
    private List<PhoneNumberManager> phoneList;
    @CodeNote(id = R.id.cl)
    ImageView cl;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        loadlistener = new getOpenIDListener(this);
        openIdModel = new OpenIdModel();
        phoneList = new ArrayList<>();
        Intent intent = new Intent(MainActivity.this, SendCodeService.class);
        intent.setAction(SendCodeService.ACTION);
        cl.setOnClickListener(v -> {
            wechatShare(0);
//            BP.pay("商品名称", "商品描述", 0.02, false, new PListener() {
//                @Override
//                public void orderId(String s) {
//                }
//
//                @Override
//                public void succeed() {
//
//                }
//
//                @Override
//                public void fail(int i, String s) {
//                    if (i == -3) {
//                        Toast.makeText(
//                                MainActivity.this,
//                                "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
//                                Toast.LENGTH_SHORT).show();
//                        installBmobPayPlugin("bp.db");
//                    } else {
//                        Toast.makeText(MainActivity.this, "支付中断!", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }
//
//                @Override
//                public void unknow() {
//
//                }
//            });

        });
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < 7) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    MainActivity.this,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        }
    }

    public IWXAPI api;

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这里填写标题";
        msg.description = "这里填写内容";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_delete);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api = WXAPIFactory.createWXAPI(this, "wx83a95cef0734f331", true);
        api.registerApp("wx83a95cef0734f331");
        api.sendReq(req);
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
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
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initEvents() {
        myDialog = new MyDialog(this);
        loadData();
        add_tel1_ll.setOnClickListener(v -> {
            if (add_tel1_tv.getText().toString().equals("添加手机号码")) {
                ToastShort("请先绑定手机号");
            } else {
                myDialog.setMiddleVal(add_tel1_tv.getText().toString());
                myDialog.setOnPositiveListener(v1 -> {
                    if (phoneList.size() > 1) {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
                    } else {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
                    }
                });
                myDialog.show();

            }
        });
        add_tel2_ll.setOnClickListener(v -> {
            if (add_tel2_tv.getText().toString().equals("添加手机号码")) {
                ToastShort("请先绑定手机号");
            } else {
                myDialog.setMiddleVal(add_tel2_tv.getText().toString());
                myDialog.setOnPositiveListener(v1 -> {
                    if (phoneList.size() > 1) {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), "", phoneList.get(1).getObjectId());
                    } else {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
                    }
                });
                myDialog.show();
            }
        });
        add_tel1_btn.setOnClickListener(v -> {//第一个手机号管理
            if (!add_tel1_tv.getText().toString().equals("添加手机号码")) {
                Intent intent = new Intent(MainActivity.this, ManageListActivity.class);
                intent.putExtra(ConfigModel.KEY_Now_Tel, add_tel1_tv.getText().toString().trim());
                intent.putExtra(ConfigModel.KEY_OpenId, openIdModel.getOpenid());
                startActivity(intent);
            }
        });
        add_tel2_btn.setOnClickListener(v -> {//第2个手机号管理
            if (!add_tel1_tv.getText().toString().equals("添加手机号码")) {
                Intent intent = new Intent(MainActivity.this, ManageListActivity.class);
                intent.putExtra(ConfigModel.KEY_Now_Tel, add_tel2_tv.getText().toString().trim());
                intent.putExtra(ConfigModel.KEY_OpenId, openIdModel.getOpenid());
                startActivity(intent);
            }
        });
        bd_openid1_iv.setOnClickListener(v -> {
            //myDialog.setMiddleMessage("请输入OPENID，保存并绑定");
            myDialog.setTitle("OPENID");
            if (("").equals(openIdModel.getOpenid()) || openIdModel.getOpenid() == null) {
                //myDialog.setMiddleVal(openIdModel.getOpenid());
                myDialog.setOnPositiveListener(v12 -> {
                    //保存到数据库
                    String openid = myDialog.getMiddleVal();//输入的openid值
                    if (openIdModel.getOpenid() == null) {
                        //添加
                        loadlistener.addOpenid(openid, "");
                    } else {
                        loadlistener.addOpenid(openid, openIdModel.getObjectId());
                    }
                });
                myDialog.show();
            } else {
                ToastShort("已经绑定OPENID");
            }
        });
        loadlistener.getOpenid(Utils.getCache("key"));
        rl_bottem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    user_id_tv.setText("账号：" + userModel.getUsername());
                    yue_tv.setText("余额：" + userModel.getYue());
                }
            }
        });
        UserModel model = BmobUser.getCurrentUser(UserModel.class);
        BmobQuery<OpenIdModel> qu = new BmobQuery<OpenIdModel>();
        qu.addWhereEqualTo("userid", model);
        qu.include("userid");
//        qu.addWhereEqualTo("userid", Utils.getCache("key"));
        qu.findObjects(new FindListener<OpenIdModel>() {
            @Override
            public void done(List<OpenIdModel> list, BmobException e) {
                String s = "";
            }
        });
    }

    @Override
    public void resultPhone(boolean isTrue, List<PhoneNumberManager> list) {
        if (isTrue) {
            phoneList = list;
            if (list.size() > 0) {
                add_tel1_tv.setText(list.get(0).getPhonenumber());
            }
            if (list.size() > 1) {
                add_tel2_tv.setText(list.get(1).getPhonenumber());
            }
        } else {
            ToastShort("加载数据失败");
        }
    }

    @Override
    public void result(boolean isTrue, OpenIdModel model) {
        if (isTrue) {
            bd_openid1_iv.setText("已绑微信");
            openIdModel = model;
            loadlistener.getPhones(model.getOpenid());
        }
    }

    @Override
    public void addPhoneResult(int type, boolean isTrue, String mes) {
        myDialog.dismiss();
        if (type == 1) {
            if (isTrue) {
                add_tel1_tv.setText(myDialog.getMiddleVal());//输入的openid值
            } else {
                ToastShort(mes);
            }
        } else {
            if (isTrue) {
                add_tel2_tv.setText(myDialog.getMiddleVal());//输入的openid值
            } else {
                ToastShort(mes);
            }
        }
    }

    @Override
    public void addOpenidResult(boolean isTrue, String mes) {
        myDialog.dismiss();
        if (isTrue) {
            String[] str = mes.split(",");
            openIdModel.setObjectId(str[0]);
            openIdModel.setOpenid(str[1]);
            bd_openid1_iv.setText("已绑微信");
        } else {
            ToastShort(mes);
        }
    }
}
