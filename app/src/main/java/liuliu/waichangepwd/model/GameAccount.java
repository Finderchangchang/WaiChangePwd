package liuliu.waichangepwd.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/16.
 */

public class GameAccount extends BmobObject {
    private String phone;//手机号
    private String password;//密码
    private String AccountNumber;//游戏账号
    public boolean isCheced=false;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }
}
