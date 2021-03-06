package liuliu.waichangepwd.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.base.BaseApplication;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.GameAccount;
import liuliu.waichangepwd.model.UserModel;

/**
 * Created by Finder丶畅畅 on 2016/11/26 00:21
 * QQ群481606175
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx83a95cef0734f331", false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        String s = "";
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                List<GameAccount> list = BaseApplication.getmOrder();
                if (BaseApplication.getmOrder().size() > 0) {//fenxiang zhanghao
                    //循环修改账号状态---就这一步
                    for (int i = 0; i < BaseApplication.getmOrder().size(); i++) {
                        GameAccount account = BaseApplication.getmOrder().get(i);
                        account.setState("已租");
                        account.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    System.out.println("分享成功");
                                } else {
                                    System.out.println("分享失败");
                                }
                            }
                        });
                    }
                    BaseApplication.setmOrder(new ArrayList<>());//清空操作
                } else {//fenxiang app
                    BmobQuery<UserModel> query = new BmobQuery<>();
                    query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
                        @Override
                        public void done(UserModel userModel, BmobException e) {
                            if (userModel != null) {
                                int now = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                                int lastShare;
                                boolean result = false;
                                if(userModel.getShareTime()==null||userModel.getShareTime().equals("")){
                                    result=true;
                                }else {
                                    try {
                                        Date d = new SimpleDateFormat("yyyy-MM-dd").parse(userModel.getShareTime());
                                        lastShare = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(d));
                                        if (now == lastShare) {
                                            if (!userModel.getShare()) {
                                                result = true;
                                            }
                                        }
                                    } catch (ParseException s) {
                                        e.printStackTrace();
                                    }
                                }
                                if (result) {
                                    userModel.setShare(true);
                                    userModel.setYue(userModel.getYue()+2);
                                    userModel.setShareTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                    userModel.update(userModel.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(WXEntryActivity.this, "分享成功，已给您返钱，请注意查收", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(WXEntryActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(WXEntryActivity.this, "您已经分享过一次，本次分享不返钱~~", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(WXEntryActivity.this, "数据处理失败~~", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
        }
        this.finish();
    }
}
