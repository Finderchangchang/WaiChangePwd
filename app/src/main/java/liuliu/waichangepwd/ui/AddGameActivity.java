package liuliu.waichangepwd.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.listener.AddGameListener;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.view.AddGameView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class AddGameActivity extends BaseActivity implements AddGameView {
    @CodeNote(id = R.id.et_game_name)
    private EditText etname;
    @CodeNote(id = R.id.btn_game_save)
    private Button btnSave;
    private GameAccount gameAccount;
    @CodeNote(id = R.id.title_iv_left)
    ImageView titleLift;
    private AddGameListener listener;
    private String phone = "";

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_game);
        gameAccount = new GameAccount();
        listener = new AddGameListener(this);
        phone = getIntent().getStringExtra("PhoneNumber");
    }

    @Override
    public void initEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameAccount.setAccountNumber(etname.getText().toString().trim());
                gameAccount.setPhone(phone);
                listener.addGame(gameAccount);
            }
        });
        titleLift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void ResultAddGame(boolean isTrue, String mes) {
        ToastShort(mes);
        if (isTrue) {
            setResult(121,null);
            finish();
        }

    }
}
