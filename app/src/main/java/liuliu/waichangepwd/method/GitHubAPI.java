package liuliu.waichangepwd.method;

import java.util.Map;

import liuliu.waichangepwd.model.MessageModel;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/20.
 */

public interface GitHubAPI {
    @GET("safety.findp.html")
    Observable<String> openPwd();
    @GET("checkBindPhone.do")
    Observable<MessageModel> checkName(@QueryMap Map<String, String> map);

    /*
     *  发送短信操作
     */
    @GET("sendMessageCode.do")
    Observable<MessageModel> sendMsg(@QueryMap Map<String, String> map);

    /*
     *  修改密码操作
     *  type=findp
     *  nickName=%E6%8B%9C%E5%B8%88%E5%BF%AB%E9%80%92
     *  password=e0c10f451217b93f76c2654b2b729b85
     *  confirmPassword=e0c10f451217b93f76c2654b2b729b85
     *  openid=ovPbFs9GEQidN3Wod-vQjNOawHxU
     *  identityCard=
     *  bindPhone=17093215800
     *  messageCode=5853
     */
    @GET("safetyOp.do")
    Observable<MessageModel> changePwd(@QueryMap Map<String, String> map);
}
