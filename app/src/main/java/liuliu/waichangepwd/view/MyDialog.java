package liuliu.waichangepwd.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import liuliu.waichangepwd.R;

/**
 * Created by Administrator on 2016/11/16.
 */

public class MyDialog extends Dialog {
    ImageView ensure_btn;
    EditText middle_et;
    RelativeLayout rlAll;

    public MyDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, null);
        ensure_btn = (ImageView) mView.findViewById(R.id.ensure_btn_ll_dialog);
        rlAll = (RelativeLayout) mView.findViewById(R.id.rl_dialog_all);
        middle_et = (EditText) mView.findViewById(R.id.middle_et_ll_dialog);
        super.setContentView(mView);
    }

    //是否为OPENid
    public void setRLBG(boolean isTrue) {
        if (isTrue) {
            rlAll.setBackgroundResource(R.mipmap.dialog_openid);
        } else {
            rlAll.setBackgroundResource(R.mipmap.dialog_phone);
        }
    }

    /*为中部的EditText赋值*/
    public void setMiddleVal(String val) {
        middle_et.setVisibility(View.VISIBLE);
        middle_et.setText(val);
    }


    /*获得文本框的文字内容*/
    public String getMiddleVal() {
        return middle_et.getText().toString();
    }


    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        ensure_btn.setOnClickListener(listener);
    }


}
