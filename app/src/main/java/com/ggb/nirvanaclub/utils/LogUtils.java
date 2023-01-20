package com.ggb.nirvanaclub.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by liuming on 2018/3/12.
 * 功能描述：
 * 版本：
 */

public class LogUtils {
    public static void init(){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("DiDiWaiMai")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static void e(String str){
        Logger.e(str);
    }

    public static void i(String str){
        Logger.i(str);
    }

    public static void d(String str){
        Logger.d(str);
    }

    public static void json(String str){
        Logger.json(str);
    }
}
