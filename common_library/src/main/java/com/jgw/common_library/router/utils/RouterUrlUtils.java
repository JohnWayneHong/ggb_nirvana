package com.jgw.common_library.router.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.WebPlugin;


import java.net.URLEncoder;

/**
 * 路由协议转换(jgw://domain?action=xxx&data={xxx})
 */
public class RouterUrlUtils {

    private static final String SCHEMA_JGW = "jgw";

    /**
     * 路由地址转换Command
     *
     * @param url 路由地址
     */
    public static RouterCommand parseUrlToCommand(String url) {
        return parseUrlToCommand(url,false);
    }

    /**
     * 路由地址转换Command
     *
     * @param url 路由地址
     */
    public static RouterCommand parseUrlToCommand(String url,boolean handleHttpUrl) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (handleHttpUrl && url.startsWith("http")) {
            RouterCommand command = new RouterCommand();
            command.domain= WebPlugin.DOMAIN;
            command.action= WebPlugin.ACTION_JUMP_WEBPAGE;
            command.data= url;
            return command;
        }
        Uri uri = Uri.parse(url);
        String schema = uri.getScheme();
        if (!TextUtils.equals(SCHEMA_JGW, schema)) {
            return null;
        }
        RouterCommand command = new RouterCommand();
        command.domain = uri.getHost();
        command.action = uri.getQueryParameter("action");
        command.data = uri.getQueryParameter("data");
        return command;
    }

    /**
     * 路由Command转换地址
     *
     * @param command 路由指令
     */
    public static String parseCommandToUrl(RouterCommand command) {
        if (command == null) {
            return "";
        }
        String data = command.data;
        try {
            data = URLEncoder.encode(command.data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SCHEMA_JGW + "://" + command.domain + "?" + "action=" + command.action + "&" + "data=" + data;
    }

    /**
     * 是否可以路由执行
     */
    public static boolean canRoute(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        String schema = uri.getScheme();
        return TextUtils.equals(SCHEMA_JGW, schema);
    }

}
