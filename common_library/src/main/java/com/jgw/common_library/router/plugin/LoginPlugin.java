package com.jgw.common_library.router.plugin;

import android.content.Context;
import android.content.Intent;

import com.jgw.common_library.router.CustomRouter;
import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.base.BasePlugin;


/**
 * 活动路由插件
 */

public class LoginPlugin implements BasePlugin {

    public static final String DOMAIN = "login";

    public static final String ACTION_TOKEN_USELESS = "tokenUseless";


    @Override
    public void execute(Context context, RouterCommand command) {
        switch (command.action) {
            case ACTION_TOKEN_USELESS:
                login(context,command);
                break;
            default:
                break;
        }
    }

    /**
     * 登录超时跳转登录界面
     */
    private void login(Context context, RouterCommand command) {
        Class<?> router = CustomRouter.findRouter(DOMAIN);
        Intent intent=new Intent(context,router);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("action",command.action);
        context.startActivity(intent);
    }
}
