package liuliu.waichangepwd.method;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/20.
 */

public interface GitHubAPI {
    /*
     *  发送短信操作
     */
    @GET("sendMessageCode.do")
    Observable<String> sendMsg(@QueryMap Map<String, String> map);

    /*
     *  修改密码操作
     */
    @GET("safetyOp.do")
    Observable<String> changePwd(@QueryMap Map map);
}
