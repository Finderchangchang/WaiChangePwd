package liuliu.waichangepwd.listener;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import liuliu.waichangepwd.method.Utils;
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
        model.setYue(0.0);
        model.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {//将s存储在内存中。用来执行记录登录状态
                    Utils.putCache("key", s);
                    mView.regResult(true, "注册成功");
                } else {
                    if (e.getErrorCode() == 9016) {
                        mView.regResult(false, "网络错误，请检查网络");
                    } else if (e.getErrorCode() == 401) {
                        mView.regResult(false, "此账号已存在");
                    } else {
                        mView.regResult(false, "注册失败");
                    }
                }
            }
        });
    }
}

interface IRegUserMView {
    void regUser(String user, String pwd);
}
