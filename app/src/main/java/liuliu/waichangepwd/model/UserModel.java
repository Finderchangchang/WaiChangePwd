package liuliu.waichangepwd.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/8.
 */

public class UserModel extends BmobObject {
    private String username;
    private String password;
    private String myMoney;

    public String getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(String myMoney) {
        this.myMoney = myMoney;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
