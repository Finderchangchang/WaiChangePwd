package liuliu.waichangepwd.listener;

import liuliu.waichangepwd.view.ILoginView;

/**
 * Created by Administrator on 2016/11/8.
 */

public class LoginListener implements ILoginMView{
    ILoginView mView;

    public LoginListener(ILoginView mView) {
        this.mView = mView;
    }

    @Override
    public void toLogin(String name, String pwd) {

    }
}
interface ILoginMView{
    void toLogin(String name,String pwd);//登录
}
