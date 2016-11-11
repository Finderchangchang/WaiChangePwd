package liuliu.waichangepwd.model;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MessageModel {

    /**
     * msg : 手机号码今日获取验证码次数已达到上限，请明日再来！
     * type : findp
     */

    private String msg;
    private String type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
