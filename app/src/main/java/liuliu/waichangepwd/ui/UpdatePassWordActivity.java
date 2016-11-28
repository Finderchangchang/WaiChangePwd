package liuliu.waichangepwd.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.MyDialog;

/**
 * Created by Finder丶畅畅 on 2016/11/28 20:30
 * QQ群481606175
 */

public class UpdatePassWordActivity extends BaseActivity {
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;
    @CodeNote(id = R.id.old_psw)
    EditText old_psw;
    @CodeNote(id = R.id.new_psw)
    EditText new_psw;
    @CodeNote(id = R.id.new_psw2)
    EditText new_psw2;
    @CodeNote(id = R.id.save_btn)
    Button save_btn;
    private MyDialog myDialog;
@CodeNote(id=R.id.yue_tv)TextView yue_tv;
    @CodeNote(id=R.id.user_id_tv)TextView user_id_tv;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_update_passwrod);

    }

    @Override
    public void initEvents() {
        loadBases();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改密码
                if (old_psw.getText().toString().equals("") || new_psw.getText().toString().equals("") || new_psw2.getText().toString().equals("")) {
                    ToastShort("请填写完整信息");
                } else if (!new_psw2.getText().toString().equals(new_psw.getText().toString())) {
                    new_psw.setText("");
                    new_psw2.setText("");
                    ToastShort("输入密码不一致，请重新输入");
                } else {
                    loadBase();
                }
            }
        });
        title_iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }

    private void loadBase() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    if (userModel.getPassword().equals(old_psw.getText().toString().trim())) {

                        userModel.setPassword(new_psw.getText().toString().trim());
                        userModel.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    finish();
                                    ToastShort("修改成功！");
                                } else {
                                    ToastShort("修改失败" + e.getMessage());
                                }
                            }
                        });
                    } else {
                        old_psw.setText("");
                        ToastShort("密码验证失败！");
                    }

                }
            }
        });
    }
    private void loadBases() {
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
    }
}
