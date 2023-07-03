package com.jgw.common_library.router;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.jgw.common_library.base.ui.BaseActivity;
import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.base.BasePlugin;
import com.jgw.common_library.router.plugin.base.PluginFactory;
import com.jgw.common_library.router.utils.RouterUrlUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 应用内路由协议
 * 协议示例( jgw://domain?action=xxx&data={xxx} )
 */
public class CustomRouter {
    private static final Map<String, Class<?>> registry = new HashMap<>();

    public static boolean route(Context context, String url) {
        return route(context, url, false);
    }
    public static boolean route(Context context, RouterCommand command) {
        return route(context, RouterUrlUtils.parseCommandToUrl(command), false);
    }

    /**
     * 路由操作 (应用内不同域操作)
     *
     * @param context       上下文
     * @param url           链接（http://xxx mg://xxx）
     * @param handleHttpUrl 是否需要处理http链接
     */
    public static boolean route(Context context, String url, boolean handleHttpUrl) {
        //空链接直接返回
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        RouterCommand command = RouterUrlUtils.parseUrlToCommand(url,handleHttpUrl);
        if (command != null) {
            BasePlugin plugin = PluginFactory.createPlugin(command);
            if (plugin != null) {
                try {
                    plugin.execute(context, command);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
        return false;
    }

    static void realRegister(String domain, Class<? extends Activity> clazz) {
        registry.put(domain,clazz);
    }

    public static Class<?> findRouter(String domain) {
        return registry.get(domain);
    }
}
