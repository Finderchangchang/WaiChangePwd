package liuliu.waichangepwd.listener;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.view.ManagerListView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ManagerListListener {
    private ManagerListView view;

    public ManagerListListener(ManagerListView v) {
        view = v;
    }

    public void DeleteGame(List<GameAccount> accounts) {

        for (int i = 0; i < accounts.size(); i++) {
            if (i == accounts.size() - 1) {
                delete(true, accounts.get(i));
            } else {
                delete(false, accounts.get(i));
            }
        }
    }

    public void delete(boolean isTrue, GameAccount account) {
        if (account != null) {
            account.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        if (isTrue) {
                            view.resultDelete(true, "已完成");
                        }
                    } else {
                        view.resultDelete(false, account.getAccountNumber() + "删除失败：" + e.getMessage());
                    }
                }
            });
        }
    }


}
