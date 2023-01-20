package com.ggb.nirvanaclub.adapter

import android.util.Log
import android.widget.ImageView
import androidx.annotation.Nullable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R

class MeBannerAdapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_me_banner) {

    override fun convert(helper: BaseViewHolder?, item: Int) {
        helper?.getView<ImageView>(R.id.iv_carousel_img)?.setImageResource(item)
    }
    override fun getItemCount(): Int = Int.MAX_VALUE

    //重写getItem以免出现无限滑动时当position大于data的size时获得对象为空
    @Nullable
    override fun getItem(position: Int): Int {
        val newPosition = position % data.size
        return data[newPosition]
    }

    //重写此方法，因为BaseQuickAdapter里绘制view时会调用此方法判断，position减去getHeaderLayoutCount小于data.size()时才会调用调用cover方法绘制我们自定义的view
    override fun getItemViewType(position: Int): Int {
        var count = headerLayoutCount + data.size
        //刚开始进入包含该类的activity时,count为0。就会出现0%0的情况，这会抛出异常，所以我们要在下面做一下判断
        if (count <= 0) {
            count = 1
        }
        val newPosition = position % count
        return super.getItemViewType(newPosition)
    }
}