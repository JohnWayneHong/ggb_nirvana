package com.ggb.nirvanaclub.modules.community


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityKnowledgeAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.CommunityNavigationBean
import com.ggb.nirvanaclub.bean.CommunityNavigationItem
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.DensityUtils
import com.ggb.nirvanaclub.view.RxToast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder
import com.kunminx.linkage.bean.BaseGroupedItem
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig
import kotlinx.android.synthetic.main.fragment_community_android.*
import kotlinx.android.synthetic.main.fragment_community_navigation.*
import kotlinx.android.synthetic.main.fragment_community_navigation.mh_community_android_header
import kotlinx.android.synthetic.main.fragment_community_navigation.swipe_refresh_layout

class CommunityNavigationFragment :BaseFragment(),GGBContract.View{

    private var present: GGBPresent?=null

    override fun getLayoutResource() = R.layout.fragment_community_navigation

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {

        present?.communityNavigation()

        mh_community_android_header.setColorSchemeResources(R.color.main_color)
        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
    }

    private fun getNewsList(isRefreshList: Boolean){
        present?.communityNavigation()
    }
    
    companion object {
        fun newInstance(): CommunityNavigationFragment {
            val fragment = CommunityNavigationFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.COMMUNITYNAVIGATION -> {
                    data?.let {
                        data as List<CommunityNavigationBean>
                        val header = JsonArray()

                        var isAddNirvana = false

                        data.forEach {

                            val header1 = JsonObject()

                            if (!isAddNirvana){

                                val nirH1 = JsonObject()
                                val nirH2 = JsonObject()
                                val nirH2Info = JsonObject()

                                nirH1.addProperty("header","史上最强网址")
                                nirH1.addProperty("isHeader",true)

                                nirH2.addProperty("isHeader",false)
                                nirH2.add("info",nirH2Info)
                                nirH2Info.addProperty("content",1)
                                nirH2Info.addProperty("imgUrl","https://nirvana1234.xyz/v2")
                                nirH2Info.addProperty("group","史上最强网址")
                                nirH2Info.addProperty("title","Nirvana Blog")

                                header.add(nirH1)
                                header.add(nirH2)

                                isAddNirvana = true
                            }

                            //一级标题
                            header1.addProperty("header",it.name)
                            header1.addProperty("isHeader",true)

                            header.add(header1)

                            if (!it.articles.isNullOrEmpty()){

                                swipe_refresh_layout.finishRefresh()

                                it.articles.forEach {item->
                                    val header2 = JsonObject()
                                    val info = JsonObject()
                                    //一级标题对应的二级标题
                                    header2.addProperty("isHeader",false)
                                    header2.add("info",info)
                                    info.addProperty("content",item.id)
                                    info.addProperty("imgUrl",item.link)
                                    info.addProperty("group",item.chapterName)
                                    info.addProperty("title",item.title)

                                    header.add(header2)
                                }
                            }
                        }

                        val gson = Gson()

                        val items: List<CommunityNavigationItem> = gson.fromJson(header,
                            object : TypeToken<List<CommunityNavigationItem?>?>() {}.type)

                        linkage_navigation.init(items as List<Nothing>?, ElemePrimaryAdapterConfig(), ElemeSecondaryAdapterConfig())
                        linkage_navigation.isGridMode = true



                    }
                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
    }

    private class ElemePrimaryAdapterConfig : ILinkagePrimaryAdapterConfig {
        private var mContext: Context? = null
        override fun setContext(context: Context) {
            mContext = context
        }

        override fun getLayoutId(): Int {
            return com.kunminx.linkage.R.layout.default_adapter_linkage_primary
        }

        override fun getGroupTitleViewId(): Int {
            return com.kunminx.linkage.R.id.tv_group
        }

        override fun getRootViewId(): Int {
            return com.kunminx.linkage.R.id.layout_group
        }

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(
            holder: LinkagePrimaryViewHolder,
            selected: Boolean,
            title: String
        ) {
            val tvTitle = holder.groupTitle as TextView
            tvTitle.text = title
            tvTitle.setBackgroundColor(
                mContext!!.resources.getColor(
                    if (selected) R.color.main_color else com.kunminx.linkage.R.color.colorWhite
                )
            )
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    if (selected) com.kunminx.linkage.R.color.colorWhite else com.kunminx.linkage.R.color.colorGray
                )
            )
            tvTitle.ellipsize =
                if (selected) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
            tvTitle.isFocusable = selected
            tvTitle.isFocusableInTouchMode = selected
            tvTitle.setMarqueeRepeatLimit(if (selected) -1 else 0)
        }

        override fun onItemClick(holder: LinkagePrimaryViewHolder, view: View, title: String) {
            //TODO
        }
    }

    private class ElemeSecondaryAdapterConfig :
        ILinkageSecondaryAdapterConfig<CommunityNavigationItem.ItemInfo> {
        private var mContext: Context? = null
        override fun setContext(context: Context) {
            mContext = context
        }

        override fun getGridLayoutId(): Int {
            return R.layout.adapter_eleme_secondary_grid
        }

        override fun getLinearLayoutId(): Int {
            return 0
//            return R.layout.adapter_eleme_secondary_linear
        }

        override fun getHeaderLayoutId(): Int {
            return com.kunminx.linkage.R.layout.default_adapter_linkage_secondary_header
        }

        override fun getFooterLayoutId(): Int {
            return 0
        }

        override fun getHeaderTextViewId(): Int {
            return R.id.secondary_header
        }

        override fun getSpanCountOfGridMode(): Int {
            return 2
        }

        override fun onBindViewHolder(
            holder: LinkageSecondaryViewHolder,
            item: BaseGroupedItem<CommunityNavigationItem.ItemInfo>
        ) {
            (holder.getView<View>(R.id.tv_knowledge_item_name) as TextView).text = item.info.title
            (holder.getView<View>(R.id.tv_knowledge_item_name) as TextView).setTextColor(DensityUtils.randomColor())

            holder.getView<TextView>(R.id.tv_knowledge_item_name).setOnClickListener {
                CommunityWebContentActivity.start(mContext,item.info.content.toInt(),item.info.title,item.info.imgUrl)
            }

//            Glide.with(mContext!!).load(item.info.getImgUrl())
//                .into(holder.getView<View>(R.id.iv_goods_img) as ImageView)
//            holder.getView<View>(R.id.iv_goods_item).setOnClickListener { v: View? -> }
//            holder.getView<View>(R.id.iv_goods_add).setOnClickListener { v: View? -> }
        }

        override fun onBindHeaderViewHolder(
            holder: LinkageSecondaryHeaderViewHolder,
            item: BaseGroupedItem<CommunityNavigationItem.ItemInfo>
        ) {
            (holder.getView<View>(R.id.secondary_header) as TextView).text = item.header
        }

        override fun onBindFooterViewHolder(
            holder: LinkageSecondaryFooterViewHolder,
            item: BaseGroupedItem<CommunityNavigationItem.ItemInfo>
        ) {
        }
    }

}