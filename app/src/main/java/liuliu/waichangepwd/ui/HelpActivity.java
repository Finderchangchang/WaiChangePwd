package liuliu.waichangepwd.ui;

import android.view.View;
import android.widget.ImageView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.waichangepwd.R;
import liuliu.waichangepwd.base.BaseActivity;

/**
 * Created by Administrator on 2016/11/20.
 */

public class HelpActivity extends BaseActivity {
    @CodeNote(id = R.id.title_iv_left)
    ImageView title_iv_left;

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

    }
}
