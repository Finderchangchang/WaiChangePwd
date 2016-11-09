package liuliu.waichangepwd.ui;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

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
    @CodeNote(id = R.id.usr_name_tv)
    AutoCompleteTextView usr_name_tv;
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;
    @CodeNote(id = R.id.confirm_pwd_et)
    EditText confirm_pwd_et;
    @CodeNote(id = R.id.reg_user_btn)
    Button reg_user_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_user);
        mListener = new RegUserListener(this);
    }

    private String name;
    private String pwd;

    @Override
    public void initEvents() {
        reg_user_btn.setOnClickListener(v -> {
            name = usr_name_tv.getText().toString().trim();
            pwd = pwd_et.getText().toString().trim();
            String confirm_pwd = confirm_pwd_et.getText().toString().trim();
            if (("").equals(name)) {
                ToastShort("用户名不能为空~~");
            } else if (("").equals(pwd) || ("").equals(confirm_pwd)) {
                ToastShort("前后密码不一致，请重新输入！！");
            } else {
                mListener.regUser(name, pwd);
            }
        });
    }

    @Override
    public void regResult(boolean result) {
        if (result) {
            Utils.IntentPost(MainActivity.class);
            finish();//关闭当前页面
        } else {
            ToastShort("用户名已存在，请重新输入~~");
        }
    }
}
