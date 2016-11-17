package liuliu.waichangepwd.listener;

import android.util.Log;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.view.AddGameView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class AddGameListener {
    private AddGameView view;
    public AddGameListener(AddGameView v){
        view=v;
    }
    //添加游戏账号信息
    public void addGame(GameAccount account){
        if(account!=null){
            account.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        view.ResultAddGame(true,"添加成功");
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        view.ResultAddGame(false,e.getMessage());
                    }
                }
            });
        }
    }
}