package com.kcode.lib.bean;

import com.kcode.lib.log.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by caik on 2017/3/8.
 */

public class VersionModel implements Serializable{

    /**
     * versionCode : 1
     * versionName : 1.0.0
     * content : 更新描述
     * mustUpdate : true
     * url : 文件下载地址
     */

    private int versionCode;
    private String versionName;
    private String content;
    private boolean mustUpdate;
    private String url;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VersionModel parse(String json) {
        try {
            JSONObject object = new JSONObject(json);
            versionCode = object.getInt("versionCode");
            versionName = object.getString("versionName");
            content = object.getString("content");
            url = object.getString("url");
            mustUpdate = object.getBoolean("mustUpdate");

            return this;

        } catch (JSONException e) {
            L.e("updateLib","数据格式错误");
            return null;

        }

    }
}
