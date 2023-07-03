package com.jgw.common_library.router.plugin.base;

import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.LoginPlugin;
import com.jgw.common_library.router.plugin.WebPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * 路由插件创建和注册工厂
 */
public class PluginFactory {

    private static final Map<String, Class<? extends BasePlugin>> pluginMap = new HashMap<>();

    /* 注册plugin */
    static {
        pluginMap.put(LoginPlugin.DOMAIN, LoginPlugin.class);
        pluginMap.put(WebPlugin.DOMAIN, WebPlugin.class);
    }

    /**
     * 生成已经注册的路由插件（根据命令里domain）
     *
     * @param command 命令
     */
    public static BasePlugin createPlugin(RouterCommand command) {
        String domain = command.domain;
        if (pluginMap.containsKey(domain)) {
            //noinspection TryWithIdenticalCatches
            try {
                //noinspection ConstantConditions
                return pluginMap.get(domain).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void registerPlugin(String domain,Class<? extends BasePlugin> clazz){
        pluginMap.put(domain,clazz);
    }
}
