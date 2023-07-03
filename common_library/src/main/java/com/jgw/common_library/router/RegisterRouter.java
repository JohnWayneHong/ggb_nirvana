package com.jgw.common_library.router;

import android.app.Activity;

public class RegisterRouter {

    public static boolean register(String domain,Class<? extends Activity> clazz) {
        CustomRouter.realRegister(domain, clazz);
        return true;
    }

}
