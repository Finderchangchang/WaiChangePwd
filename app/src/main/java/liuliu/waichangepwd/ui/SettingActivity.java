package liuliu.waichangepwd.ui;

import android.widget.RelativeLayout;

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
    @CodeNote(id = R.id.pay_rl)
    RelativeLayout pay_rl;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_mysetting);
        mListener = new SettingListener(this, this);
        mListener.checkPay();
    }

    @Override
    public void initEvents() {
        share_rl.setOnClickListener(v -> {
            mListener.loadShare();
        });
        pay_rl.setOnClickListener(v -> {
            mListener.loadPay(0.02);
        });
    }

    @Override
    public void payResult(boolean result) {
        ToastShort("result:" + result);
    }
}
