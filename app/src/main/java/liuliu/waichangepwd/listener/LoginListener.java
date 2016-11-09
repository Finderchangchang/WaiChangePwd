package liuliu.waichangepwd.listener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.ILoginView;

/**
 * Created by Administrator on 2016/11/8.
 */

public class LoginListener implements ILoginMView {
    ILoginView mView;

    public LoginListener(ILoginView mView) {
        this.mView = mView;
    }

    @Override
    public void toLogin(String name, String pwd) {
        BmobQuery<UserModel> list = new BmobQuery<>();
        list.addWhereEqualTo("username", name);
        list.addWhereEqualTo("password", pwd);
        list.findObjects(new FindListener<UserModel>() {
            @Override
            public void done(List<UserModel> list, BmobException e) {
                if (e == null) {
                    mView.loginResult(true);
                } else {
                    mView.loginResult(false);
                }
            }
        });
    }
}

interface ILoginMView {
    void toLogin(String name, String pwd);//登录
}
