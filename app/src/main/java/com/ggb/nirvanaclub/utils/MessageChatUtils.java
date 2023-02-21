package com.ggb.nirvanaclub.utils;


import android.text.TextUtils;

import cn.jpush.im.android.api.model.UserInfo;

/** 
 * 跟聊天相关的辅助类
 */  
public class MessageChatUtils
{

    private MessageChatUtils()
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");
  
    }  
  
    /** 
     * 获取聊天账户 或者昵称
     */
    public static String getName(UserInfo userInfo){
        if(userInfo == null){
            return "";
        }

        if(TextUtils.isEmpty(userInfo.getNickname())){
            return userInfo.getUserName();
        }else{
            return userInfo.getNickname();
        }
    }
} 