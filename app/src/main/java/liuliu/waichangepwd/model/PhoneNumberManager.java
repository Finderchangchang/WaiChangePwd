package liuliu.waichangepwd.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/16.
 */

public class PhoneNumberManager extends BmobObject {

    private String phonenumber;
    private String openid;


    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
