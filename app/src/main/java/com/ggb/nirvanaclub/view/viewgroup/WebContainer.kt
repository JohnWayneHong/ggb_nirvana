package com.ggb.nirvanaclub.view.viewgroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat

/**
 * @author hongweijie
 * @date 2022/11/24
 * @desc WebContainer
 */
class WebContainer : CoordinatorLayout {


    private var mDarkTheme: Boolean = false

    private var mMaskColor = Color.TRANSPARENT

    init {
        //暂时不设置夜间模式
//        mDarkTheme = SettingUtil.getIsNightMode()
//        if (mDarkTheme) {
//            mMaskColor = ColorUtil.alphaColor(ContextCompat.getColor(getContext(), R.color.mask_color), 0.6f)
//        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mDarkTheme) {
            canvas.drawColor(mMaskColor)
        }
    }
}
