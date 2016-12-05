package liuliu.waichangepwd.ui;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.RegUserListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.view.IRegUserView;

/**
 * Created by Administrator on 2016/11/9.
 */

public class RegUserActivity extends BaseActivity implements IRegUserView {
    RegUserListener mListener;
    @CodeNote(id = R.id.usr_name_et)
    EditText usr_name_et;
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;
    @CodeNote(id = R.id.pwd_et2)
    EditText pwd_et2;
    @CodeNote(id = R.id.login_reg)
    ImageButton login_reg;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_user);
        mListener = new RegUserListener(this);
    }

    private String name;
    private String pwd;

    @Override
    public void initEvents() {
        login_reg.setOnClickListener(v -> {
            name = usr_name_et.getText().toString().trim();
            pwd = pwd_et.getText().toString().trim();
            String confirm_pwd = pwd_et2.getText().toString().trim();
            if (("").equals(name)) {
                ToastShort("用户名不能为空！");
            } else if (("").equals(pwd) || ("").equals(confirm_pwd)) {
                ToastShort("密码不能为空！");
            }else if(!pwd.equals(confirm_pwd)){
                ToastShort("前后密码不一致，请重新输入！");
                pwd_et.setText("");
                pwd_et2.setText("");
            }else if(pwd.length()!=6){
                ToastShort("密码为6位字符，请重新输入！");
                pwd_et.setText("");
                pwd_et2.setText("");
            }else {
                mListener.regUser(name, pwd);
            }
        });
    }

    @Override
    public void regResult(boolean result) {
        if (result) {
            Utils.IntentPost(MainActivity.class);
            finish();//关闭当前页面
            if (LoginActivity.mIntails != null) {
                LoginActivity.mIntails.finish();
            }
        } else {
            ToastShort("用户名已存在，请重新输入~~");
        }
    }
}
