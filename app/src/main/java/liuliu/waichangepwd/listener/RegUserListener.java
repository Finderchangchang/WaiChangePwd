package liuliu.waichangepwd.listener;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import liuliu.waichangepwd.model.UserModel;
import liuliu.waichangepwd.view.IRegUserView;

/**
 * Created by Administrator on 2016/11/9.
 */

public class RegUserListener implements IRegUserMView {
    IRegUserView mView;

    public RegUserListener(IRegUserView mView) {
        this.mView = mView;
    }

    @Override
    public void regUser(String user, String pwd) {
        UserModel model = new UserModel();
        model.setUsername(user);
        model.setPassword(pwd);
        model.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {//将s存储在内存中。用来执行记录登录状态
                    mView.regResult(true);
                } else {
                    mView.regResult(false);
                }
            }
        });
    }
}

interface IRegUserMView {
    void regUser(String user, String pwd);
}
