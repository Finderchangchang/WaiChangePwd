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
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.method.Utils;
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
                BmobQuery<UserModel> query = new BmobQuery<>();
                query.getObject(Utils.getCache("key"), new QueryListener<UserModel>() {
                    @Override
                    public void done(UserModel userModel, BmobException e) {
                        if (userModel != null) {
                            int now = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                            int lastShare;
                            boolean result = false;
                            try {
                                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(userModel.getShareTime());
                                lastShare = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(d));
                                if (now == lastShare) {
                                    if (!userModel.getShare()) {
                                        result = true;
                                    }
                                }
                            } catch (ParseException s) {

                            }
                            if (result) {
                                userModel.setShare(true);
                                userModel.setShareTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                userModel.update(userModel.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        Toast.makeText(WXEntryActivity.this, "分享成功，已给您返钱，请注意查收", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(WXEntryActivity.this, "您已经分享过一次，本次分享不返钱~~", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
        }
        this.finish();
    }
}
