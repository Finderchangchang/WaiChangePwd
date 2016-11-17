package liuliu.waichangepwd.view;

import java.util.List;

import liuliu.waichangepwd.model.OpenIdModel;
import liuliu.waichangepwd.model.PhoneNumberManager;

/**
 * Created by Administrator on 2016/11/17.
 */

public interface getOpenidView {
    void resultPhone(boolean isTrue, List<PhoneNumberManager> list);

    void result(boolean isTrue, OpenIdModel model);
    void addOpenidResult(boolean isTrue,String mes);
    void addPhoneResult(int type,boolean isTrue,String mes);
}
