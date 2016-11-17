package liuliu.waichangepwd.ui;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.method.CommonAdapter;
import liuliu.waichangepwd.method.CommonViewHolder;
import liuliu.waichangepwd.method.HttpUtil;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.model.PhoneNumberManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ManageListActivity extends BaseActivity {
    @CodeNote(id = R.id.list_lv)
    ListView list_lv;
    @CodeNote(id = R.id.bottom_ll)
    LinearLayout bottom_ll;
    @CodeNote(id = R.id.no_data_tv)
    TextView no_data_tv;
    @CodeNote(id = R.id.add_tel_btn)
    Button add_tel_btn;
    @CodeNote(id = R.id.no_data_rl)
    RelativeLayout no_data_rl;
    @CodeNote(id = R.id.add_nick_name_tv)
    TextView add_nick_name_tv;
    @CodeNote(id = R.id.start_change_tv)
    TextView start_change_tv;
    @CodeNote(id = R.id.share_tv)
    TextView share_tv;
    String tel;
    CommonAdapter<GameAccount> mAdapter;
    List<GameAccount> mList;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_manage_list);
        mList = new ArrayList<>();
        tel = getIntent().getStringExtra(ConfigModel.KEY_Now_Tel);
        if (("添加手机号码").compareTo(tel) == 0) {
            tel = null;
            add_tel_btn.setVisibility(View.VISIBLE);
        } else {
            no_data_tv.setText("当前无数据~~");
            add_tel_btn.setVisibility(View.GONE);
        }
        mAdapter = new CommonAdapter<GameAccount>(this, mList, R.layout.item_game_name) {
            @Override
            public void convert(CommonViewHolder holder, GameAccount gameAccount, int position) {
                holder.setText(R.id.item_position_tv, position + 1);
                holder.setText(R.id.nick_name_tv, gameAccount.getAccountNumber());
                if (!("").equals(gameAccount.getPassword()) && gameAccount.getPassword() != null) {
                    holder.setText(R.id.pwd_tv, gameAccount.getPassword());
                    holder.setText(R.id.pwd_change_tv, "上次修改时间：" + gameAccount.getUpdatedAt());
                } else {
                    holder.setText(R.id.pwd_tv, "无");
                    holder.setText(R.id.pwd_change_tv, "密码未修改过");
                }
            }
        };
        list_lv.setAdapter(mAdapter);
    }

    @Override
    public void initEvents() {
        if (tel != null) {
            load();
        }
        add_tel_btn.setOnClickListener(v -> {
            PhoneNumberManager model = new PhoneNumberManager();
            model.setPhonenumber("17093215800");
            model.setOpenid(Utils.getCache("key"));
            model.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e.getErrorCode() == 401) {
                        ToastShort("当前手机号已存在，如有疑问请联系客服人员");
                    } else {
                        no_data_tv.setText("当前无数据~~");
                        add_tel_btn.setVisibility(View.GONE);
                    }
                }
            });
        });
        add_nick_name_tv.setOnClickListener(v -> {//添加昵称
            if (tel != null) {
                GameAccount game = new GameAccount();
                game.setPhone(tel);
                game.setAccountNumber("哇哈哈");
                game.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        ToastShort(s);
                        load();
                    }
                });
            } else {
                ToastShort("请先填写绑定的手机号码~~");
            }
        });
        start_change_tv.setOnClickListener(v -> {//批量操作修改密码

        });
        share_tv.setOnClickListener(v -> {//分享

        });
    }

    private void load() {
        BmobQuery<GameAccount> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", tel);
        query.findObjects(new FindListener<GameAccount>() {
            @Override
            public void done(List<GameAccount> list, BmobException e) {
                boolean is_null;
                if (list != null) {
                    if (list.size() > 0) {
                        is_null = false;
                        mAdapter.refresh(list);
                    } else {
                        is_null = true;
                    }
                } else {
                    is_null = true;
                }
                no_data_rl.setVisibility(is_null ? View.VISIBLE : View.GONE);
                list_lv.setVisibility(is_null ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void sendCode() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "findp");
        map.put("nickName", "拜师快递");
        map.put("openid", "ovPbFs9GEQidN3Wod-vQjNOawHxU");
        map.put("bindPhone", "17093215800");
        HttpUtil.load()
                .sendMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val -> {
                    if (val.getMsg().contains("达到上限")) {
                        ToastShort(val.getMsg());
                    } else {
                        ToastShort(val.getMsg());
                    }
                }, error -> {
                    ToastShort(error.toString());
                });
    }
}
