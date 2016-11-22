package liuliu.waichangepwd.ui;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.LoginListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.view.ILoginView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView {
    public static LoginActivity mIntails;
    @CodeNote(id = R.id.usr_name_et)
    private EditText usr_name_et;
    @CodeNote(id = R.id.pwd_et)
    private EditText pwd_et;
    private LoginListener mListener;
    @CodeNote(id = R.id.login_btn)
    ImageView login_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mListener = new LoginListener(this);
        mIntails = this;
    }

    @Override
    public void initEvents() {
        login_btn.setOnClickListener(view -> attemptLogin());//登录操作
        if (!("").equals(Utils.getCache("key"))) {
            Utils.IntentPost(MainActivity.class);
            finish();
        }
    }

    /**
     * 验证执行登录
     */
    private void attemptLogin() {
        String user_name = usr_name_et.getText().toString();
        String password = pwd_et.getText().toString();
        if (TextUtils.isEmpty(user_name)) {
            ToastShort("用户名不能为空~~");
        } else if (TextUtils.isEmpty(password)) {
            ToastShort("密码不能为空~~");
        } else {
            mListener.toLogin(user_name, password);
        }
    }


    @Override
    public void loginResult(boolean result) {
        if (result) {
            Utils.IntentPost(MainActivity.class);
            finish();
        } else {
            ToastShort("账号或密码错误，请重新输入~~");
        }
    }
}

