package liuliu.waichangepwd.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.config.ConfigModel;
import liuliu.waichangepwd.listener.ManagerListListener;
import liuliu.waichangepwd.method.CommonAdapter;
import liuliu.waichangepwd.method.CommonViewHolder;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.model.UserModel;
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
    @CodeNote(id = R.id.yz_tv)
    TextView yz_tv;
    @CodeNote(id = R.id.wz_tv)
    TextView wz_tv;
    @CodeNote(id = R.id.xz_tv)
    TextView xz_tv;
    @CodeNote(id = R.id.vip_tv)
    TextView vip_tv;
    @CodeNote(id = R.id.jj_tv)
    TextView jj_tv;
    @CodeNote(id = R.id.qx_tv)
    TextView qx_tv;

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

                holder.setText(R.id.item_change_time, "改:" + gameAccount.getUpdatedAt().substring(8, 10) + "日" + gameAccount.getUpdatedAt().substring(11, 13) + "时");

                holder.setText(R.id.item_time, "定:--日--时");

                holder.setText(R.id.item_diamonds, "钻石 " + gameAccount.getDiamonds());
                holder.setText(R.id.item_gold, "黄金 " + gameAccount.getGold());
                holder.setText(R.id.item_horn, "号角 " + gameAccount.getHorn());
                holder.setText(R.id.item_lock, "锁定 " + gameAccount.getLocking());
                holder.setText(R.id.item_jiu, gameAccount.getReliefFund() + "万");
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
                switch (gameAccount.getState()) {
                    case "已租":
                        holder.setBG(R.id.tag_ll, R.mipmap.item_tag1_bg);
                        break;
                    case "续租":
                        holder.setBG(R.id.tag_ll, R.mipmap.item_tag_bg);
                        break;
                    default:
                        holder.setBG(R.id.tag_ll, R.mipmap.item_tag2_bg);
                        break;
                }
                switch (gameAccount.getVipGrade()) {
                    case 0:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_0);
                        break;
                    case 1:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_1);
                        break;
                    case 2:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_2);
                        break;
                    case 3:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_3);
                        break;
                    case 4:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_4);
                        break;
                    case 5:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_5);
                        break;
                    case 6:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_6);
                        break;
                    case 7:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_7);
                        break;
                    case 8:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_8);
                        break;
                    default:
                        holder.setImageResource(R.id.item_vip, R.mipmap.vip_9);
                        break;
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
        list_lv.setOnItemLongClickListener((parent, view, position, id) -> {
            Utils.IntentPost(AddGameActivity.class, listener -> {
                listener.putExtra("id",mList.get(position));
            });
            return false;
        });
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
                load("");
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
            load("");
        }
        ivLeft.setOnClickListener(v -> finish());
        add_nick_name_tv.setOnClickListener(v -> {//添加昵称
            Intent intent = new Intent(ManageListActivity.this, AddGameActivity.class);
            intent.putExtra("PhoneNumber", tel);
            startActivityForResult(intent, 11);
        });
        yz_tv.setOnClickListener(v -> load("state"));
        wz_tv.setOnClickListener(v -> load("-state"));
        xz_tv.setOnClickListener(v -> load("state"));
        vip_tv.setOnClickListener(v -> load("VipGrade"));
        jj_tv.setOnClickListener(v -> load("ReliefFund"));
        Intent intent = new Intent(ManageListActivity.this, SendCodeService.class);
        intent.setAction(SendCodeService.ACTION);
        start_change_tv.setOnClickListener(v -> {//批量操作修改密码
            if (BaseApplication.getmOrder().size() > 0) {
                ToastShort("当前任务已存在，请等待任务完成以后再操作~~");
            } else {
                if (checkList.size() > 0) {
                    BmobQuery<UserModel> query = new BmobQuery<UserModel>();
                    query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
                        @Override
                        public void done(UserModel userModel, BmobException e) {
                            if (userModel.getYue() >= checkList.size() * 0.5) {//余额能支付
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ManageListActivity.this);
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
                                ToastShort("您的余额不足，请充值~~");
                            }
                        }
                    });
                } else {
                    ToastShort("请至少选择一个游戏账号进行修改~~");
                }
            }
        });

        share_tv.setOnClickListener(v -> {//分享
            if (BaseApplication.getmOrder().size() > 0) {
                wechatShare(0);
            } else {
                ToastShort("请至少选择一个游戏账号进行修改~~");
            }
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

    private IWXAPI api;

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.dakedaojia.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "提示";
        msg.description = "您的账号：" + flag + "\n您的密码：";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon_delete);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api = WXAPIFactory.createWXAPI(this, "wx83a95cef0734f331", true);
        api.registerApp("wx83a95cef0734f331");
        api.sendReq(req);
    }

    private void load(String order) {
        BmobQuery<GameAccount> query = new BmobQuery<>();
        query.addWhereEqualTo("phone", tel);
        if (!("").equals(order)) {
            query.order(order);
        }
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
            load("");
        }
    }

    @Override
    public void resultDelete(boolean isTrue, String mes) {
        if (isTrue) {
            progressDialog.dismiss();
            load("");
        }
        ToastShort(mes);
    }
}
