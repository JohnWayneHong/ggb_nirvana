package com.ggb.nirvanaclub.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 博客账户是否被注册
 */

class RegisterCheckBean(
    @SerializedName("ok")val ok:Boolean
): Serializable

