package liuliu.waichangepwd.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/8.
 */

public class UserModel extends BmobObject {
    private String username;
    private String password;
    private Integer yue;

    public Integer getYue() {
        return yue;
    }

    public void setYue(Integer yue) {
        this.yue = yue;
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
