package liuliu.waichangepwd.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.LoginListener;
import liuliu.waichangepwd.view.ILoginView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    private AutoCompleteTextView usr_name_tv;
    private EditText pwd_et;
    private View mProgressView;
    private View mLoginFormView;
    private LoginListener mListener;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mListener = new LoginListener(this);
        usr_name_tv = (AutoCompleteTextView) findViewById(R.id.usr_name_tv);
        pwd_et = (EditText) findViewById(R.id.pwd_et);
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(view -> attemptLogin());
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void initEvents() {

    }

    /**
     * 验证执行登录
     */
    private void attemptLogin() {
        usr_name_tv.setError(null);
        pwd_et.setError(null);
        String user_name = usr_name_tv.getText().toString();
        String password = pwd_et.getText().toString();

        View focusView = null;
        if (TextUtils.isEmpty(user_name)) {
            usr_name_tv.setError(getString(R.string.error_field_required));
            focusView = usr_name_tv;
        } else if (TextUtils.isEmpty(password)) {
            pwd_et.setError(getString(R.string.error_invalid_password));
            focusView = pwd_et;
        } else {
            focusView.requestFocus();
            mListener.toLogin(user_name,password);
        }
    }


    @Override
    public void loginResult(boolean result) {
        if(result){
            
        }
    }
}

