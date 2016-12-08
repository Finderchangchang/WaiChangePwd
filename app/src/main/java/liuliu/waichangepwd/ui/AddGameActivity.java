package liuliu.waichangepwd.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.AddGameListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.model.PhoneNumberManager;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.AddGameView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class AddGameActivity extends BaseActivity implements AddGameView {
    @CodeNote(id = R.id.yz_ll)
    LinearLayout yz_ll;
    @CodeNote(id = R.id.wz_ll)
    LinearLayout wz_ll;
    @CodeNote(id = R.id.xz_ll)
    LinearLayout xz_ll;
    @CodeNote(id = R.id.yz_cb)
    CheckBox yz_cb;
    @CodeNote(id = R.id.wz_cb)
    CheckBox wz_cb;
    @CodeNote(id = R.id.xz_cb)
    CheckBox xz_cb;
    @CodeNote(id = R.id.vip_et)
    EditText vip_et;
    @CodeNote(id = R.id.pt_et)
    EditText pt_et;//炮台
    @CodeNote(id = R.id.jiuji_et)
    EditText jiuji_et;//救济金
    @CodeNote(id = R.id.game_name_et)
    private EditText game_name_et;
    @CodeNote(id = R.id.save_btn)
    private Button save_btn;
    @CodeNote(id = R.id.title_iv_left)
    ImageView titleLift;
    @CodeNote(id = R.id.hd_et)
    EditText hd_et;//救济金
    @CodeNote(id = R.id.qt_et)
    EditText qt_et;//救济金
    @CodeNote(id = R.id.by_et)
    EditText by_et;//救济金
    @CodeNote(id = R.id.hjj_et)
    EditText hjj_et;//救济金
    @CodeNote(id = R.id.bj_et)
    EditText bj_et;//救济金
    @CodeNote(id = R.id.hj_et)
    EditText hj_et;//救济金
    @CodeNote(id = R.id.sd_et)
    EditText sd_et;//救济金
    @CodeNote(id = R.id.kb_et)
    EditText kb_et;//救济金
    @CodeNote(id = R.id.zs_et)
    EditText zs_et;//救济金
    @CodeNote(id = R.id.cz_et)
    EditText cz_et;//救济金
    private AddGameListener listener;
    private String phone = "";
    @CodeNote(id = R.id.remark_et)
    EditText remark_et;
    @CodeNote(id = R.id.renew_et)
    EditText renew_et;
    private GameAccount gameAccount;
    @CodeNote(id = R.id.user_id_tv)
    private TextView user_id_tv;
    @CodeNote(id = R.id.yue_tv)
    private TextView yue_tv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_game);
        gameAccount = new GameAccount();
        listener = new AddGameListener(this);
        phone = getIntent().getStringExtra("PhoneNumber");
        gameAccount = (GameAccount) getIntent().getSerializableExtra("id");
        loadBase();
    }

    @Override
    public void initEvents() {
        if (gameAccount != null) {
            vip_et.setText(gameAccount.getVipGrade().toString());
            game_name_et.setText(gameAccount.getAccountNumber());
            pt_et.setText(gameAccount.getBatteryGrade().toString());
            jiuji_et.setText(gameAccount.getReliefFund().toString());
            hd_et.setText(gameAccount.getBomb().toString());
            qt_et.setText(gameAccount.getBronze().toString());
            hjj_et.setText(gameAccount.getGold().toString());
            bj_et.setText(gameAccount.getPlatinum().toString());
            hj_et.setText(gameAccount.getHorn().toString());
            sd_et.setText(gameAccount.getLocking().toString());
            kb_et.setText(gameAccount.getRage().toString());
            zs_et.setText(gameAccount.getDiamonds().toString());
            cz_et.setText(gameAccount.getAmountCharge().toString());
            by_et.setText(gameAccount.getSilver().toString());
            //if(gameAccount.getRenew()!=null) {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            renew_et.setText(gameAccount.getRenew());
            //}
            remark_et.setText(gameAccount.getRemark());
        } else {
            gameAccount = new GameAccount();
        }

        yz_ll.setOnClickListener(v -> {
            clearCB();
            yz_cb.setChecked(true);
            renew_et.setText("");
            renew_et.setEnabled(false);
        });
        wz_ll.setOnClickListener(v -> {
            clearCB();
            wz_cb.setChecked(true);
            renew_et.setText("");
            renew_et.setEnabled(false);
        });
        xz_ll.setOnClickListener(v -> {
            clearCB();
            xz_cb.setChecked(true);
            renew_et.setEnabled(true);
        });
        save_btn.setOnClickListener(v -> {

            if (("").equals(vip_et.getText().toString().trim())) {
                ToastShort("vip等级不能为空");
            } else if (("").equals(game_name_et.getText().toString().trim())) {
                ToastShort("账号昵称不能为空");
            } else if (("").equals(pt_et.getText().toString().trim())) {
                ToastShort("炮台等级不能为空");
            } else if (("").equals(jiuji_et.getText().toString().trim())) {
                ToastShort("救济金不能为空");
            } else if (!("").equals(renew_et.getText().toString().trim()) && renew_et.getText().toString().trim().length() != 4) {
                ToastShort("请核对到期时间格式");
            } else {
                if (yz_cb.isChecked()) {
                    gameAccount.setState("已租");
                } else if (wz_cb.isChecked()) {
                    gameAccount.setState("未租");
                } else {
                    gameAccount.setState("续租");
                }
                gameAccount.setVipGrade(getIntger(vip_et));
                gameAccount.setAccountNumber(game_name_et.getText().toString().trim());
                gameAccount.setBatteryGrade(getIntger(pt_et));
                gameAccount.setReliefFund(getIntger(jiuji_et));
                if (gameAccount.getPhoneId() == null) {
                    PhoneNumberManager manager = new PhoneNumberManager();
                    manager.setObjectId(phone);
                    gameAccount.setPhoneId(manager);
                }
                gameAccount.setBomb(getIntger(hd_et));
                gameAccount.setBronze(getIntger(qt_et));
                gameAccount.setSilver(getIntger(by_et));
                gameAccount.setGold(getIntger(hjj_et));
                gameAccount.setPlatinum(getIntger(bj_et));
                gameAccount.setHorn(getIntger(hj_et));
                gameAccount.setLocking(getIntger(sd_et));
                gameAccount.setRage(getIntger(kb_et));
                gameAccount.setAmountCharge(getIntger(cz_et));
                gameAccount.setDiamonds(getIntger(zs_et));
                gameAccount.setRemark(remark_et.getText().toString().trim());
                gameAccount.setRenew(renew_et.getText().toString());
                listener.addGame(gameAccount);
            }
        });
        titleLift.setOnClickListener(v -> finish());
    }

    private int getIntger(EditText et) {
        String e = et.getText().toString().trim();
        if (("").equals(e)) {
            return 0;
        } else {
            return Integer.parseInt(e);
        }
    }

    private boolean YANRIQI(String data) {
        if (data.equals("")) {
            return true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                sdf.parse(data);
            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
            return true;
        }
    }

    private void loadBase() {
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
            @Override
            public void done(UserModel userModel, BmobException e) {
                if (userModel != null) {
                    user_id_tv.setText("账号：" + userModel.getUsername());
                    yue_tv.setText("余额：" + userModel.getYue());
                }
            }
        });
    }

    private void clearCB() {
        yz_cb.setChecked(false);
        wz_cb.setChecked(false);
        xz_cb.setChecked(false);
    }

    @Override
    public void ResultAddGame(boolean isTrue, String mes) {
        ToastShort(mes);
        if (isTrue) {
            setResult(121, null);
            finish();
        }
    }
}
