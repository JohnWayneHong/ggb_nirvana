package com.ggb.nirvanaclub.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.ShopCartBean
import com.ggb.nirvanaclub.utils.ImageLoaderUtil

class ShopCartAdapter: BaseQuickAdapter<ShopCartBean, BaseViewHolder>(R.layout.item_shop_cart){
    override fun convert(helper: BaseViewHolder?, item: ShopCartBean) {
        ImageLoaderUtil().displayImage(mContext,item.firstImage,helper?.getView(R.id.iv_shop_cart))
        helper?.setText(R.id.tv_shop_name,item.goodsName)
        helper?.setText(R.id.tv_shop_price,"¥${item.price}")
        helper?.setText(R.id.tv_shop_spec,item.constitute)
        helper?.setText(R.id.tv_shop_count,item.goodsNumber.toString())
        helper?.setChecked(R.id.tb_shop_checked,item.isSelected)

        helper?.addOnClickListener(R.id.ll_shop_cart)
        helper?.addOnClickListener(R.id.ll_shop_checked)
        helper?.addOnClickListener(R.id.iv_shop_add)
        helper?.addOnClickListener(R.id.iv_shop_sub)
        helper?.addOnClickListener(R.id.tv_cart_delete)
    }

}