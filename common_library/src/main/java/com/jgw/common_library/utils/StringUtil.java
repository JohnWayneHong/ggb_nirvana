package com.jgw.common_library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     */
    public static String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 描述：手机号格式验证.
     *
     * @param str 指定的手机号码字符串
     * @return 是否为手机号码格式:是为true，否则false
     */
    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * 描述：是否是合规的密码
     * (设置6~20个字符，包含数字和英文字母组合不能包含空格)
     *
     * @param str 指定的密码串
     * @return 是否为合规的密码:是为true，否则false
     */
    public static boolean isRulePassword(String str) {
        boolean isPassword = false;
        try {
            Pattern p = Pattern.compile("^(?!.*\\s)(?!\\d{1,9}$).{6,20}$");
            Matcher m = p.matcher(str);
            isPassword = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPassword;
    }

    public static Spanned getHtml2String(int total) {
        return Html.fromHtml("共<font color='#03A9F4'>" + total + "</font>头");
    }

    public static Spanned getHtml3String(int total) {
        return Html.fromHtml("共<font color='#03A9F4'>" + total + "</font>栏");
    }

    /**
     * 是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean matcherNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
