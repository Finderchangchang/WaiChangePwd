package liuliu.waichangepwd.ui;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.SettingListener;
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
    @CodeNote(id = R.id.refresh_iv)
    ImageView refresh_iv;
    @CodeNote(id = R.id.cz_et)
    EditText cz_et;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_mysetting);
        mListener = new SettingListener(this, this);
        mListener.checkPay();
        mListener.loadYE();
    }

    @Override
    public void initEvents() {
        refresh_iv.setOnClickListener(v -> {
            mListener.loadYE();
        });
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
