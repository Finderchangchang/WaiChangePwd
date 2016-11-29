package liuliu.waichangepwd.listener;

import android.util.Log;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.view.AddGameView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class AddGameListener {
    private AddGameView view;

    public AddGameListener(AddGameView v) {
        view = v;
    }

    //添加游戏账号信息
    public void addGame(GameAccount account) {
        if (account != null) {
            if (account.getObjectId() != null) {
                account.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            view.ResultAddGame(true, "修改成功");
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            if (e.getMessage().contains("errorCode:100")) {
                                view.ResultAddGame(false, "服务器维护中...");
                            } else if (e.getMessage().contains("errorCode:401")) {
                                view.ResultAddGame(false, "游戏账号已存在");
                            } else {
                                view.ResultAddGame(false, e.getMessage());
                            }
                        }
                    }
                });
            } else {
                account.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            view.ResultAddGame(true, "添加成功");
                        }
                        if (e.getMessage().contains("errorCode:100")) {
                            view.ResultAddGame(false, "服务器维护中...");
                        } else if (e.getMessage().contains("errorCode:401")) {
                            view.ResultAddGame(false, "游戏账号已存在");
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                            view.ResultAddGame(false, e.getMessage());
                        }
                    }
                });
            }
        }
    }
}
