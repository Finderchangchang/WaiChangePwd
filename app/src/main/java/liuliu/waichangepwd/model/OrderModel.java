package liuliu.waichangepwd.model;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/16.
 */

public class OrderModel extends BmobObject {
    private GameAccount gameid;
    private Boolean state;

    public GameAccount getGameid() {
        return gameid;
    }

    public void setGameid(GameAccount gameid) {
        this.gameid = gameid;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
