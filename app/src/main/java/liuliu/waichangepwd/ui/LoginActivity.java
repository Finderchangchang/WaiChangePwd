package liuliu.waichangepwd.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
    @CodeNote(id = R.id.login_reg)
    ImageView login_reg;
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;
    @CodeNote(id = R.id.title_help)
    ImageView title_help;
    @CodeNote(id = R.id.top_tb)
    RelativeLayout top_tb;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mListener = new LoginListener(this);
        mIntails = this;
        // System.out.println("key-------:loginactiv" );
    }

    @Override
    public void initEvents() {
        login_btn.setOnClickListener(view -> attemptLogin());//登录操作
        if (!("").equals(Utils.getCache("key"))) {
            Utils.IntentPost(MainActivity.class);
            finish();
        }
        title_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegUserActivity.class);
                startActivity(intent);
            }
        });
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
    public void loginResult(boolean result, String mes) {
        if (result) {
            Utils.IntentPost(MainActivity.class);
            finish();
        } else {
            if (!mes.equals(""))
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

                //Log.i("mengdd", "onTouch : ");

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

