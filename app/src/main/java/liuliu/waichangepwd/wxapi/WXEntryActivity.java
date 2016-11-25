package liuliu.waichangepwd.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Finder丶畅畅 on 2016/11/26 00:21
 * QQ群481606175
 */

public class WXEntryActivity  extends Activity implements IWXAPIEventHandler{
    IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api= WXAPIFactory.createWXAPI(this,"wx83a95cef0734f331",false);
        api.handleIntent(getIntent(),this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
