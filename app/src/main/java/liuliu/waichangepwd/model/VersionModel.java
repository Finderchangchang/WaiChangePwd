package liuliu.waichangepwd.model;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2016/12/6 21:38
 * QQ群481606175
 */

public class VersionModel extends BmobObject {
    String url;//更新地址
    String updateContent;//更新内容
    String version;//当前版本号

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }
}
