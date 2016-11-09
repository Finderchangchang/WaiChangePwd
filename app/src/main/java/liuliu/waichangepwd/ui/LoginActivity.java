package liuliu.waichangepwd.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    @CodeNote(id = R.id.usr_name_tv)
    private AutoCompleteTextView usr_name_tv;
    @CodeNote(id = R.id.pwd_et)
    private EditText pwd_et;
    @CodeNote(id = R.id.login_progress)
    private View mProgressView;
    @CodeNote(id = R.id.login_form)
    private View mLoginFormView;
    private LoginListener mListener;
    @CodeNote(id = R.id.reg_user_btn)
    private Button reg_user_btn;
    @CodeNote(id = R.id.login_btn)
    private Button login_btn;
    @CodeNote(id = R.id.forget_pwd_tv)
    TextView forget_pwd_tv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mListener = new LoginListener(this);
    }

    @Override
    public void initEvents() {
        login_btn.setOnClickListener(view -> attemptLogin());//登录操作
        reg_user_btn.setOnClickListener(view -> Utils.IntentPost(RegUserActivity.class));//跳转到注册页面
        forget_pwd_tv.setOnClickListener(view -> ToastShort("敬请期待~~"));
    }

    /**
     * 验证执行登录
     */
    private void attemptLogin() {
        usr_name_tv.setError(null);
        pwd_et.setError(null);
        String user_name = usr_name_tv.getText().toString();
        String password = pwd_et.getText().toString();

        if (TextUtils.isEmpty(user_name)) {
            usr_name_tv.setError(getString(R.string.error_field_required));
        } else if (TextUtils.isEmpty(password)) {
            pwd_et.setError(getString(R.string.error_invalid_password));
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

