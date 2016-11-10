package liuliu.waichangepwd.ui;

import android.content.IntentFilter;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.HashMap;
import java.util.Map;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.service.SmsReciver;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MainActivity extends BaseActivity {
    String url = "&nickName=拜师快递&openid=ovPbFs9GEQidN3Wod-vQjNOawHxU&bindPhone=17093215800";
    @CodeNote(id = R.id.start_change_btn)
    Button start_change_btn;
    private SmsReciver mSmsReceiver;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initEvents() {
        mSmsReceiver = new SmsReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sms_received");
        registerReceiver(mSmsReceiver, intentFilter);
        start_change_btn.setOnClickListener(v -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "findp");
            map.put("nickName", "拜师快递");
            map.put("openid", "ovPbFs9GEQidN3Wod-vQjNOawHxU");
            map.put("bindPhone", "17093215800");
            HttpUtil.load().sendMsg(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(val -> {
                        ToastShort(val);
                    }, error -> {
                        SmsReciver re=new SmsReciver();

                    });
        });
    }

}
