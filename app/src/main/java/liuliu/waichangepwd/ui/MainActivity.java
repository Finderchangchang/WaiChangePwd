package liuliu.waichangepwd.ui;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

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
    @CodeNote(id = R.id.setting_iv)
    private ImageView setting_iv;
    private MyDialog myDialog;
    @CodeNote(id = R.id.add_tel1_ll)
    LinearLayout add_tel1_ll;
    @CodeNote(id = R.id.add_tel2_ll)
    LinearLayout add_tel2_ll;
    private getOpenIDListener loadlistener;
    private OpenIdModel openIdModel;
    private List<PhoneNumberManager> phoneList;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        loadlistener = new getOpenIDListener(this);
        openIdModel = new OpenIdModel();
        phoneList = new ArrayList<>();
        Intent intent = new Intent(MainActivity.this, SendCodeService.class);
        intent.setAction(SendCodeService.ACTION);
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
//        add_tel1_tv.setOnClickListener(v -> {
//            if (openIdModel.getOpenid() != null) {
//                myDialog.setMiddleMessage("请输入手机号，保存并绑定");
//                myDialog.setTitle("手机号");
//                if (!tvphone1.getText().equals("添加手机号码")) {
//                    myDialog.setMiddleVal(tvphone1.getText().toString());
//                } else {
//                    myDialog.setMiddleVal("");
//                }
//                myDialog.setOnPositiveListener(v1 -> {
//                    //保存到数据库
//                    if (phoneList.size() > 0) {
//                        loadlistener.addPhone(1, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
//                    } else {
//                        loadlistener.addPhone(1, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
//                    }
//                });
//                myDialog.show();
//            } else {
//                ToastShort("请先绑定OPENID");
//            }
//        });
//        add_tel2_tv.setOnClickListener(v -> {
//            if (openIdModel != null) {
//                myDialog.setMiddleMessage("请输入手机号，保存并绑定");
//                myDialog.setTitle("手机号");
//                if (!tvphone2.getText().equals("添加手机号码")) {
//                    myDialog.setMiddleVal(tvphone2.getText().toString());
//                } else {
//                    myDialog.setMiddleVal("");
//                }
//                myDialog.setOnPositiveListener(v1 -> {
//                    //保存到数据库
//                    if (phoneList.size() > 1) {
//                        loadlistener.addPhone(2, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
//                    } else {
//                        loadlistener.addPhone(2, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
//                    }
//                });
//                myDialog.show();
//            } else {
//                ToastShort("请先绑定OPENID");
//            }
//        });
        loadlistener.getOpenid(Utils.getCache("key"));
        setting_iv.setOnClickListener(v -> {
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
                    if (null != userModel.getMyMoney()) {
                        yue_tv.setText("余额：" + userModel.getMyMoney());
                    } else {
                        yue_tv.setText("余额：0");
                    }
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
