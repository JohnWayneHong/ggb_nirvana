package com.jgw.common_library.router.plugin.base;

import android.content.Context;

import com.jgw.common_library.router.model.RouterCommand;

/**
 * 路由插件
 */
public interface BasePlugin {
    /**
     * 执行路由命令
     *
     * @param context 上下文
     * @param command 指令
     */
    void execute(Context context, RouterCommand command);
}
