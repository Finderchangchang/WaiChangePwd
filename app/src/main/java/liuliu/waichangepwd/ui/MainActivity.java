package liuliu.waichangepwd.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.listener.getOpenIDListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.OpenIdModel;
import liuliu.waichangepwd.model.PhoneNumberManager;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.service.SmsReciver;
import liuliu.waichangepwd.view.MyDialog;
import liuliu.waichangepwd.view.getOpenidView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MainActivity extends BaseActivity implements getOpenidView {
    //    @CodeNote(id = R.id.start_change_btn)
    private Button start_change_btn;
    private SmsReciver mSmsReceiver;
    @CodeNote(id = R.id.bd_openid1_iv)
    private ImageView bd_openid1_iv;
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
    @CodeNote(id = R.id.add_tel1_tv)
    TextView tvphone1;
    @CodeNote(id = R.id.add_tel2_tv)
    TextView tvphone2;
    private getOpenIDListener loadlistener;
    private OpenIdModel openIdModel;
    private List<PhoneNumberManager> phoneList;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        loadlistener = new getOpenIDListener(this);
        openIdModel = new OpenIdModel();
        phoneList = new ArrayList<>();
    }

    @Override
    public void initEvents() {
        mSmsReceiver = new SmsReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sms_received");
        registerReceiver(mSmsReceiver, intentFilter);
        myDialog = new MyDialog(this);
        loadData();

        add_tel1_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvphone1.getText().toString().equals("添加手机号码")) {
                    ToastShort("请先绑定手机号");
                }else{
                    Intent intent=new Intent(MainActivity.this,ManageListActivity.class);
                    intent.putExtra(ConfigModel.KEY_Now_Tel, tvphone1.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
        add_tel2_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvphone2.getText().toString().equals("添加手机号码")) {
                    ToastShort("请先绑定手机号");
                }else{
                    Intent intent=new Intent(MainActivity.this,ManageListActivity.class);
                    intent.putExtra(ConfigModel.KEY_Now_Tel, tvphone2.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
        bd_openid1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setMiddleMessage("请输入OPENID，保存并绑定");
                myDialog.setTitle("OPENID");
                if(openIdModel.getOpenid().equals("")) {
                    //myDialog.setMiddleVal(openIdModel.getOpenid());
                    myDialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("-----------------openid");
                            //保存到数据库
                            String openid = myDialog.getMiddleVal();//输入的openid值
                            if (openIdModel.getOpenid().equals("")) {
                                //添加
                                loadlistener.addOpenid(openid, "");
                            } else {
                                if (!openIdModel.getOpenid().equals(openid)) {
                                    loadlistener.addOpenid(openid, openIdModel.getObjectId());
                                }
                            }
                        }
                    });

                    myDialog.show();
                }else{
                    ToastShort("已经绑定OPENID");
                }
            }
        });
        tvphone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setMiddleMessage("请输入手机号，保存并绑定");
                myDialog.setTitle("手机号");
                if(!tvphone1.getText().equals("添加手机号码")){
                    myDialog.setMiddleVal(tvphone1.getText().toString());
                }
                myDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("-----------------openid");
                        //保存到数据库

                        if (phoneList.size() > 0) {
                            loadlistener.addPhone(1, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
                        }else{
                            loadlistener.addPhone(1, myDialog.getMiddleVal(), openIdModel.getOpenid(),"");
                        }
                    }
                });
                myDialog.show();
            }
        });
        tvphone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setMiddleMessage("请输入手机号，保存并绑定");
                myDialog.setTitle("手机号");
                if(!tvphone2.getText().equals("添加手机号码")){
                    myDialog.setMiddleVal(tvphone2.getText().toString());
                }
                myDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("-----------------openid");
                        //保存到数据库
                        if (phoneList.size() > 1) {
                            loadlistener.addPhone(2, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
                        }else{
                            loadlistener.addPhone(2, myDialog.getMiddleVal(), openIdModel.getOpenid(),"");
                        }
                    }
                });
                myDialog.show();
            }
        });
        loadlistener.getOpenid(Utils.getCache("key"));
    }

    /**
     * 获得订单编号
     *
     * @return
     */
    private String getOrderID() {

        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    }

    private void loadData() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    user_id_tv.setText(userModel.getUsername());
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
                tvphone1.setText(list.get(0).getPhonenumber());
            }
            if (list.size() > 1) {
                tvphone2.setText(list.get(1).getPhonenumber());
            }

        } else {
            ToastShort("加载数据失败");
        }
    }

    @Override
    public void result(boolean isTrue, OpenIdModel model) {
        if (isTrue) {
            //切换图片为已绑定状态
            bd_openid1_iv.setImageResource(R.mipmap.yibangding);
            //?
            openIdModel = model;
            //获取Openid下的两个手机号
            loadlistener.getPhones(model.getOpenid());
        } else {
            ToastShort("加载数据失败");
        }
    }

    @Override
    public void addPhoneResult(int type, boolean isTrue, String mes) {
        myDialog.dismiss();
        if (type == 1) {
            if (isTrue) {
                tvphone1.setText(myDialog.getMiddleVal());//输入的openid值
            } else {
                ToastShort(mes);
            }
        } else {
            if (isTrue) {
                tvphone2.setText(myDialog.getMiddleVal());//输入的openid值
            } else {
                ToastShort(mes);
            }
        }
    }

    @Override
    public void addOpenidResult(boolean isTrue, String mes) {
        myDialog.dismiss();
        ToastShort(mes);
    }
}
