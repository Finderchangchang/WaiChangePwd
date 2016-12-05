package liuliu.waichangepwd.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.SettingListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.ISettingView;

/**
 * Created by Administrator on 2016/11/16.
 */

public class SettingActivity extends BaseActivity implements ISettingView {
    SettingListener mListener;
    @CodeNote(id = R.id.share_rl)
    RelativeLayout share_rl;
    @CodeNote(id = R.id.cz_btn)
    Button cz_btn;
    @CodeNote(id = R.id.yue_tv)
    TextView yue_tv;
    @CodeNote(id = R.id.cz_et)
    EditText cz_et;
    @CodeNote(id = R.id.setting_help)
    RelativeLayout setting_help;
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;
    @CodeNote(id = R.id.exit_rl)
    RelativeLayout exit_rl;
    @CodeNote(id = R.id.update_pwd_rl)
    RelativeLayout update_pwd_rl;
    @CodeNote(id = R.id.bottom_yue_tv)
    TextView bottom_yue_tv;
    @CodeNote(id = R.id.user_id_tv)
    TextView user_id_tv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_mysetting);
        mListener = new SettingListener(this, this);
        mListener.checkPay();
        if (Utils.getCache("key") != null & (!Utils.getCache("key").equals(""))) {
            loadBase();
            mListener.loadYE();

        }
    }

    @Override
    public void initEvents() {

        share_rl.setOnClickListener(v -> {
            mListener.loadShare();
        });
        cz_btn.setOnClickListener(v -> {
            if (!("").equals(cz_et.getText().toString().trim())) {
                mListener.loadPay(Double.parseDouble(cz_et.getText().toString().trim()));
            } else {
                ToastShort("请填写充值金额");
            }
        });
        setting_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        title_iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update_pwd_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, UpdatePassWordActivity.class);
                startActivity(intent);
            }
        });
        exit_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("确认退出吗？");
                builder.setTitle("提示");

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Utils.putCache("key", "");
                        myfinish();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    private void myfinish() {
        setResult(131);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("----------onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("----------onRestart");
        loadBase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("----------onPause");
    }

    private void loadBase() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    user_id_tv.setText("账号：" + userModel.getUsername());
                    bottom_yue_tv.setText("余额：" + userModel.getYue());
                }
            }
        });
    }

    @Override
    public void payResult(boolean result) {
        ToastShort("result:" + result);
    }

    @Override
    public void yueResult(String yue) {
        yue_tv.setText(yue);
    }
}
