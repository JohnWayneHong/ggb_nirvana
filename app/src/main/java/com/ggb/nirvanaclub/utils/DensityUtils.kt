package com.ggb.nirvanaclub.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import java.util.*

object DensityUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (dpValue * scale + 0.5f).toInt()
    }
    fun Activity.dip2px(dpValue: Float): Int = dip2px(this, dpValue)
    fun Fragment.dip2px(dpValue: Float): Int = dip2px(requireContext(), dpValue)
    fun View.dip2px(dpValue: Float): Int = dip2px(context, dpValue)

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (pxValue / scale + 0.5f).toInt()
    }
    fun Activity.px2dip(dpValue: Float): Int = px2dip(this, dpValue)
    fun Fragment.px2dip(dpValue: Float): Int = px2dip(requireContext(), dpValue)
    fun View.px2dip(dpValue: Float): Int = px2dip(context, dpValue)


    /******************************************************************************/

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
    fun Activity.px2sp(pxValue: Float): Int = px2sp(this, pxValue)
    fun Fragment.px2sp(pxValue: Float): Int = px2sp(requireContext(), pxValue)
    fun View.px2sp(pxValue: Float): Int = px2sp(context, pxValue)

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
    fun Activity.sp2px(spValue: Float): Int = sp2px(this, spValue)
    fun Fragment.sp2px(spValue: Float): Int = sp2px(requireContext(), spValue)
    fun View.sp2px(spValue: Float): Int = sp2px(context, spValue)

    /**
     * 分割字符串
     *
     * @param str
     * @return
     */
    @JvmStatic
    fun subStringToList(str: String): List<String> {
        val result: MutableList<String> = ArrayList()
        if (str.isNotEmpty()) {
            val len = str.length
            if (len <= 1) {
                result.add(str)
                return result
            } else {
                for (i in 0 until len) {
                    result.add(str.substring(i, i + 1))
                }
            }
        }
        return result
    }

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        var red = random.nextInt(190)
        var green = random.nextInt(190)
        var blue = random.nextInt(190)
        if (SettingUtil.getIsNightMode()) {
            //150-255
            red = random.nextInt(105) + 150
            green = random.nextInt(105) + 150
            blue = random.nextInt(105) + 150
        }
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }
}