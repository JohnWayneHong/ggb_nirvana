package com.jgw.common_library.router.plugin;

import android.content.Context;
import android.content.Intent;

import com.jgw.common_library.router.CustomRouter;
import com.jgw.common_library.router.model.RouterCommand;
import com.jgw.common_library.router.plugin.base.BasePlugin;


/**
 * 活动路由插件
 */

public class WebPlugin implements BasePlugin {

    public static final String DOMAIN = "web";

    public static final String ACTION_JUMP_WEBPAGE = "action_jump_webpage";


    @Override
    public void execute(Context context, RouterCommand command) {
        switch (command.action) {
            case ACTION_JUMP_WEBPAGE:
                jumpWebPage(context,command);
                break;
            default:
                break;
        }
    }

    /**
     * 登录超时跳转登录界面
     */
    private void jumpWebPage(Context context,RouterCommand command) {
        Class<?> router = CustomRouter.findRouter(DOMAIN);
        Intent intent=new Intent(context,router);
        intent.putExtra("data",command.data);
        context.startActivity(intent);
    }
}
