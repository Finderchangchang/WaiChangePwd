package liuliu.waichangepwd.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.UserModel;

/**
 * Created by Administrator on 2016/11/20.
 */

public class HelpActivity extends BaseActivity {
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;
    @CodeNote(id = R.id.user_id_tv)
    TextView user_id_tv;
    @CodeNote(id = R.id.yue_tv)
    TextView yue_tv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_help);
    }

    @Override
    public void initEvents() {
        title_iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadBase();
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
}
