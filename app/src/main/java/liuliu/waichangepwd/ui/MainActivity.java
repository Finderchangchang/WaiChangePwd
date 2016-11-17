package liuliu.waichangepwd.ui;

import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.OpenIdModel;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.service.SmsReciver;
import liuliu.waichangepwd.view.MyDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MainActivity extends BaseActivity {
    //    @CodeNote(id = R.id.start_change_btn)
    private Button start_change_btn;
    private SmsReciver mSmsReceiver;
    @CodeNote(id = R.id.bd_openid1_iv)
    private ImageView bd_openid1_iv;
    @CodeNote(id = R.id.add_tel1_tv)
    private TextView add_tel1_tv;
    @CodeNote(id = R.id.add_tel1_btn)
    private TextView add_tel1_btn;
    @CodeNote(id = R.id.add_tel2_tv)
    private TextView add_tel2_tv;
    @CodeNote(id = R.id.add_tel2_btn)
    private TextView add_tel2_btn;
    @CodeNote(id = R.id.user_id_tv)
    private TextView user_id_tv;
    @CodeNote(id = R.id.yue_tv)
    private TextView yue_tv;
    @CodeNote(id = R.id.setting_iv)
    private ImageView setting_iv;
    private MyDialog myDialog;
    @CodeNote(id = R.id.add_tel1_ll)
    LinearLayout add_tel1_ll;
    @CodeNote(id = R.id.add_tel2_ll)
    LinearLayout add_tel2_ll;
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
        myDialog = new MyDialog(this);
        loadData();
//        add_tel1_ll.setOnClickListener(v ->
//                Utils.IntentPost(ManageListActivity.class, intent -> {
////                    intent.putExtra(ConfigModel.KEY_Now_Tel, add_tel1_tv.getText().toString());
//                    intent.putExtra(ConfigModel.KEY_Now_Tel, "17093215800");
//                }));
//        add_tel2_ll.setOnClickListener(v ->
//                Utils.IntentPost(ManageListActivity.class, intent -> {
//                            intent.putExtra(ConfigModel.KEY_Now_Tel, add_tel2_tv.getText().toString());
//                        }
//                ));
    }

    /**
     * 获得订单编号
     *
     * @return
     */
    private String getOrderID() {
        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    }

    private void loadData() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    user_id_tv.setText(userModel.getUsername());
                    yue_tv.setText("余额：" + userModel.getYue());
                }
            }
        });
        UserModel model = BmobUser.getCurrentUser(UserModel.class);
        BmobQuery<OpenIdModel> qu = new BmobQuery<OpenIdModel>();
        qu.addWhereEqualTo("userid", model);
        qu.include("userid");
//        qu.addWhereEqualTo("userid", Utils.getCache("key"));
        qu.findObjects(new FindListener<OpenIdModel>() {
            @Override
            public void done(List<OpenIdModel> list, BmobException e) {
                String s = "";
            }
        });
    }
}
