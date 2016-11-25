package liuliu.waichangepwd.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.listener.ManagerListListener;
import liuliu.waichangepwd.method.CommonAdapter;
import liuliu.waichangepwd.method.CommonViewHolder;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.service.SendCodeService;
import liuliu.waichangepwd.service.SmsReciver;
import liuliu.waichangepwd.view.ManagerListView;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ManageListActivity extends BaseActivity implements ManagerListView {
    @CodeNote(id = R.id.list_lv)
    ListView list_lv;
    @CodeNote(id = R.id.bottom_ll)
    LinearLayout bottom_ll;
    @CodeNote(id = R.id.no_data_tv)
    TextView no_data_tv;
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
    @CodeNote(id = R.id.title_iv_left)
    ImageView ivLeft;
    private List<GameAccount> checkList;
    private ProgressDialog progressDialog;
    @CodeNote(id = R.id.delete_nick_name_tv)
    TextView tvDelete;
    private ManagerListListener listListener;
    String open_id;
    SmsReciver mSmsReceiver;
    IntentFilter intentFilter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_manage_list);
        mSmsReceiver = new SmsReciver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("sms_received");
        mList = new ArrayList<>();
        listListener = new ManagerListListener(ManageListActivity.this);
        tel = getIntent().getStringExtra(ConfigModel.KEY_Now_Tel);
        open_id = getIntent().getStringExtra(ConfigModel.KEY_OpenId);
        checkList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        no_data_tv.setText("当前无数据~~");
        mAdapter = new CommonAdapter<GameAccount>(this, mList, R.layout.item_game_name) {
            @Override
            public void convert(CommonViewHolder holder, GameAccount gameAccount, int position) {
                if (gameAccount.getAccountNumber().length() >= 4) {
                    holder.setText(R.id.item_name, gameAccount.getAccountNumber().substring(0, 4) + "..");
                } else {
                    holder.setText(R.id.item_name, gameAccount.getAccountNumber());
                }
                if (!("").equals(gameAccount.getPassword()) && gameAccount.getPassword() != null) {
                    holder.setText(R.id.item_pwd, gameAccount.getPassword());
                } else {
                    holder.setText(R.id.item_pwd, "无");

                }
                holder.setOnClickListener(R.id.ll_item_first, v -> {
                    holder.setVisible(R.id.item_iv_open, false);
                    holder.setVisible(R.id.item_ll_open, true);
                });
                holder.setOnClickListener(R.id.item_ll_open, v -> {
                    holder.setVisible(R.id.item_iv_open, true);
                    holder.setVisible(R.id.item_ll_open, false);
                });
                holder.setText(R.id.item_battery, gameAccount.getBatteryGrade() + "炮");
                holder.setText(R.id.item_relief, "材料 " + gameAccount.getReliefFund());
                holder.setText(R.id.item_amount, "充值 " + gameAccount.getAmountCharge());
                holder.setText(R.id.item_bomb, "核弹 " + gameAccount.getBomb());
                holder.setText(R.id.item_bronze, "核弹 " + gameAccount.getBronze());

                holder.setText(R.id.item_change_time, "改:" + gameAccount.getUpdatedAt().substring(5, 7) + "日" + gameAccount.getUpdatedAt().substring(8, 10) + "时");

                holder.setText(R.id.item_time, "定:--日--时");

                holder.setText(R.id.item_diamonds, "钻石 " + gameAccount.getDiamonds());
                holder.setText(R.id.item_gold, "黄金 " + gameAccount.getGold());
                holder.setText(R.id.item_horn, "号角 " + gameAccount.getHorn());
                holder.setText(R.id.item_lock, "锁定 " + gameAccount.getLocking());
                holder.setText(R.id.item_jiu, "黄金 " + gameAccount.getReliefFund());
                //holder.setText(R.id.item_vip,"黄金 "+gameAccount.getReliefFund());
                holder.setText(R.id.item_platinum, "白金 " + gameAccount.getPlatinum());
                holder.setText(R.id.item_silver, "白银 " + gameAccount.getSilver());
                if (gameAccount.getRemark() == null) {
                    holder.setText(R.id.item_rember, "备: 无");
                } else {
                    holder.setText(R.id.item_rember, "备: " + gameAccount.getRemark());
                }
                if (gameAccount.isCheced) {
                    holder.setImageResource(R.id.item_game_ivCheck, R.mipmap.cb_click);
                } else {
                    holder.setImageResource(R.id.item_game_ivCheck, R.mipmap.cb_normal);
                }
                holder.setOnClickListener(R.id.item_game_ivCheck,
                        v -> {
                            if (gameAccount.isCheced) {
                                gameAccount.isCheced = false;
                                checkList.remove(gameAccount);
                                holder.setImageResource(R.id.item_game_ivCheck, R.mipmap.cb_normal);
                            } else {
                                gameAccount.setOpenId(open_id);
                                checkList.add(gameAccount);
                                gameAccount.isCheced = true;
                                holder.setImageResource(R.id.item_game_ivCheck, R.mipmap.cb_click);
                            }
                        });
            }
        };
        list_lv.setAdapter(mAdapter);
        //注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshFriend");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        IntentFilter intentFilters = new IntentFilter();
        intentFilters.addAction("sms_received");
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshFriend")) {
                load();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void initEvents() {
        if (tel != null) {
            load();
        }
        ivLeft.setOnClickListener(v -> finish());
        add_nick_name_tv.setOnClickListener(v -> {//添加昵称
            Intent intent = new Intent(ManageListActivity.this, AddGameActivity.class);
            intent.putExtra("PhoneNumber", tel);
            startActivityForResult(intent, 11);
        });
        Intent intent = new Intent(ManageListActivity.this, SendCodeService.class);
        intent.setAction(SendCodeService.ACTION);
        start_change_tv.setOnClickListener(v -> {//批量操作修改密码
            if (BaseApplication.getmOrder().size() > 0) {
                ToastShort("当前任务已存在，请等待任务完成以后再操作~~");
            } else {
                if (checkList.size() > 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("确定要修改当前选中的游戏账号吗？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", (dialog, which) -> {
                        BaseApplication.setmOrder(checkList);
                        registerReceiver(mSmsReceiver, intentFilter);
                        startService(intent);
                    });
                    builder.show();
                } else {
                    ToastShort("请至少选择一个游戏账号进行修改~~");
                }
            }
        });

        share_tv.setOnClickListener(v -> {//分享

        });
        tvDelete.setOnClickListener(v -> {
            if (checkList.size() > 0) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("确定要删除当前选中的游戏账号吗？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", (dialog, which) -> {
                    listListener.DeleteGame(checkList);
                });
                builder.show();
            } else {
                ToastShort("请选择要删除的信息");
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 121) {
            load();
        }
    }

    @Override
    public void resultDelete(boolean isTrue, String mes) {
        if (isTrue) {
            progressDialog.dismiss();
            load();
        }
        ToastShort(mes);
    }
}
