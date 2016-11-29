package liuliu.waichangepwd.listener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import liuliu.waichangepwd.method.Utils;
import liuliu.waichangepwd.model.OpenIdModel;
import liuliu.waichangepwd.model.PhoneNumberManager;
import liuliu.waichangepwd.view.getOpenidView;

/**
 * Created by Administrator on 2016/11/17.
 */

public class getOpenIDListener {
    getOpenidView openView;

    public getOpenIDListener(getOpenidView view) {
        openView = view;
    }

    //保存手机号
    public void addPhone(int type, String phone, String openid, String objectid) {
        PhoneNumberManager phoneNumberManager = new PhoneNumberManager();
        phoneNumberManager.setPhonenumber(phone);
        if (objectid.equals("")) {
            //添加
            phoneNumberManager.setOpenid(openid);
            phoneNumberManager.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        openView.addPhoneResult(type, true, "绑定手机号成功！");
                    } else if (e.getMessage().contains("errorCode:100")) {
                        openView.addPhoneResult(type,false, "服务器维护中...");
                    } else if (e.getMessage().contains("errorCode:401")) {
                        openView.addPhoneResult(type,false, "游戏账号已存在");
                    } else {
                        openView.addPhoneResult(type, false, "绑定手机号失败:" + e.getMessage());
                    }
                }
            });
        } else {
            //修改
            //phoneNumberManager.setOpenid(openid);
            phoneNumberManager.update(objectid, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        openView.addPhoneResult(type, true, "重新绑定手机号成功！");
                    } else if (e.getMessage().contains("errorCode:100")) {
                        openView.addPhoneResult(type,false, "服务器维护中...");
                    } else if (e.getMessage().contains("errorCode:401")) {
                        openView.addPhoneResult(type,false, "游戏账号已存在");
                    } else {
                        openView.addPhoneResult(type, false, "重新绑定手机号失败:" + e.getMessage());
                    }
                }
            });
        }
    }

    //保存openid  “”为添加  有则修改
    public void addOpenid(String oid, String objectid) {
        OpenIdModel openIdModel = new OpenIdModel();
        openIdModel.setOpenid(oid);
        if (("").equals(objectid)) {
            //添加
            openIdModel.setUserid(Utils.getCache("key"));
            openIdModel.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        openView.addOpenidResult(true, s + "," + oid);
                    } else {
                        openView.addOpenidResult(false, "绑定OPENID失败:" + e.getMessage());
                    }
                }
            });
        } else {
            //修改
            openIdModel.update(objectid, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        openView.addOpenidResult(true, "重新绑定OPENID成功！");
                    } else {
                        openView.addOpenidResult(false, "重新绑定OPENID失败:" + e.getMessage());
                    }
                }
            });
        }
    }

    //获取账号的Openid
    public void getOpenid(String uid) {
        BmobQuery<OpenIdModel> query = new BmobQuery<OpenIdModel>();
        query.addWhereEqualTo("userid", uid);
        query.findObjects(new FindListener<OpenIdModel>() {
            @Override
            public void done(List<OpenIdModel> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        openView.result(true, list.get(0));
                    } else {
                        openView.result(false, null);
                    }
                } else {
                    System.out.println(e.getMessage());
                    openView.result(false, null);
                }
            }
        });
    }

    //获取Openid的手机号
    public void getPhones(String openid) {
        BmobQuery<PhoneNumberManager> query = new BmobQuery<PhoneNumberManager>();
        query.addWhereEqualTo("openid", openid);
        query.findObjects(new FindListener<PhoneNumberManager>() {
            @Override
            public void done(List<PhoneNumberManager> list, BmobException e) {
                if (e == null) {
                    openView.resultPhone(true, list);
                } else {
                    System.out.println(e.getMessage());
                    openView.resultPhone(false, null);
                }
            }
        });
    }

}

