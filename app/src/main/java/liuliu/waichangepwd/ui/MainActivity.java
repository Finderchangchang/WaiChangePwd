package liuliu.waichangepwd.ui;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @CodeNote(id = R.id.rl_bottem)
    private LinearLayout rl_bottem;
    private MyDialog myDialog;
    @CodeNote(id = R.id.add_tel1_ll)
    LinearLayout add_tel1_ll;
    @CodeNote(id = R.id.add_tel2_ll)
    LinearLayout add_tel2_ll;
    @CodeNote(id = R.id.title_help)
    ImageView title_help;
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;
    @CodeNote(id = R.id.top_tb)
    RelativeLayout top_tb;
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
                myDialog.setMiddleVal("");
            } else {
                myDialog.setMiddleVal(add_tel1_tv.getText().toString());
            }
            myDialog.setOnPositiveListener(v1 -> {
                if(!Utils.isPhoneNumberValid(myDialog.getMiddleVal())){
                    ToastShort("请核对手机号");
                }else {
                    if (phoneList.size() > 1) {
                        loadlistener.addPhone(1, myDialog.getMiddleVal(), "", phoneList.get(0).getObjectId());
                    } else {
                        loadlistener.addPhone(1, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
                    }
                }
            });
            myDialog.show();
        });
        add_tel2_ll.setOnClickListener(v -> {
            if (add_tel2_tv.getText().toString().equals("添加手机号码")) {
                myDialog.setMiddleVal("");
            } else {
                myDialog.setMiddleVal(add_tel2_tv.getText().toString());

            }
            myDialog.setOnPositiveListener(v1 -> {
                if(!Utils.isPhoneNumberValid(myDialog.getMiddleVal())){
                    ToastShort("请核对手机号");
                }else {
                    if (phoneList.size() > 1) {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), "", phoneList.get(1).getObjectId());
                    } else {
                        loadlistener.addPhone(2, myDialog.getMiddleVal(), openIdModel.getOpenid(), "");
                    }
                }
            });
            myDialog.show();
        });
        add_tel1_btn.setOnClickListener(v -> {//第一个手机号管理
            if (!add_tel1_tv.getText().toString().equals("添加手机号码")) {
                Intent intent = new Intent(MainActivity.this, ManageListActivity.class);
                intent.putExtra(ConfigModel.KEY_Now_Tel, phoneList.get(0).getObjectId());
                intent.putExtra(ConfigModel.KEY_OpenId, openIdModel.getOpenid());
                startActivity(intent);
            }
        });
        add_tel2_btn.setOnClickListener(v -> {//第2个手机号管理
            if (!add_tel2_tv.getText().toString().equals("添加手机号码")) {
                Intent intent = new Intent(MainActivity.this, ManageListActivity.class);
                intent.putExtra(ConfigModel.KEY_Now_Tel, add_tel2_tv.getText().toString().trim());
                intent.putExtra(ConfigModel.KEY_OpenId, openIdModel.getOpenid());
                startActivity(intent);
            }
        });
        bd_openid1_iv.setOnClickListener(v -> {
            myDialog.setTitle("OPENID");
            //ovPbFs9GEQidN3Wod-vQjNOawHxU
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

                myDialog.setMiddleVal(openIdModel.getOpenid());
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
                //ToastShort("已经绑定OPENID");
            }
        });
        loadlistener.getOpenid(Utils.getCache("key"));
        rl_bottem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        title_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                //startActivity(intent);
                showPopupWindow(top_tb);
            }
        });
        title_iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
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
    protected void onRestart() {
        loadData();
        super.onRestart();
    }

    @Override
    public void resultPhone(boolean isTrue, List<PhoneNumberManager> list) {
        if (isTrue) {
            phoneList = list;
            if (list.size() > 1) {
                add_tel2_tv.setText(list.get(1).getPhonenumber());
            }
            if (list.size() > 0) {
                add_tel1_tv.setText(list.get(0).getPhonenumber());
            }

        } else {
            ToastShort("加载数据失败");
        }
    }

    @Override
    public void result(boolean isTrue, OpenIdModel model) {
        if (isTrue) {
            bd_openid1_iv.setText("已绑\n微信");
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
            bd_openid1_iv.setText("已绑\n微信");
        } else {
            ToastShort(mes);
        }
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_window, null);
        // 设置按钮的点击事件

        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.mipmap.openid_layout));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }
}
