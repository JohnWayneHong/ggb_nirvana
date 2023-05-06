package com.ggb.nirvanaclub.modules

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import cn.jpush.android.api.JPushInterface
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.IndexTagAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.TagChangeEvent
import com.ggb.nirvanaclub.modules.article.ArticleInfoFragment
import com.ggb.nirvanaclub.modules.article.SearchArticleActivity
import com.ggb.nirvanaclub.modules.login.LoginActivity
import com.ggb.nirvanaclub.modules.tag.IndexTagSettingActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.google.gson.JsonArray
import com.tamsiree.rxkit.view.RxToast
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.lib.tinker.TinkerInstaller
import kotlinx.android.synthetic.main.fragment_index.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File

class IndexFragment :BaseFragment(),GGBContract.View{

    private var mIndex = 0

    private var present:GGBPresent?=null

    private var tAdapter :IndexTagAdapter?=null

    private var tagList = arrayListOf<IndexTagBean>()

    private var fragments = arrayListOf<BaseFragment>()

    override fun getLayoutResource() = R.layout.fragment_index

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        initVariable()

        tAdapter = IndexTagAdapter()
        index_tags_rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        index_tags_rv.adapter = tAdapter

        present?.getTag()
        initEvent()
    }

    private fun initEvent() {
        tAdapter?.setOnItemClickListener { adapter, view, position ->
            tAdapter?.selectItem(position)
            articleInfoChange(position)
        }
        ll_index_tags_setting.setOnClickListener {
            if (!C.IS_LOGIN){
                context?.toast(resources.getString(R.string.login_toast))
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<IndexTagSettingActivity>()
            }
        }
        ll_index_search.setOnClickListener {
//            RxToast.success("成功修复Bug！！！恭喜")
            Log.e("TAG", "RegistrationID=============>: "+JPushInterface.getRegistrationID(context) )
            activity?.startActivity<SearchArticleActivity>()
        }
        iv_test.setOnClickListener {
            val path =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.DIRECTORY_PICTURES + File.separator + "GGBScreen"
            } else {
                Environment.getExternalStorageDirectory().path + "/GGBScreen/"
            }

            val patch: String = path + "patch_signed.apk"
            if (Tinker.isTinkerInstalled()) {

                TinkerInstaller.onReceiveUpgradePatch(context, patch)
                context?.toast("补丁加载成功！！！")
            }else{
                context?.toast("补丁包更新失败!!!")
            }
//            CrashReport.testJavaCrash()
//            activity?.startActivity<AvatarActivity>()
        }

    }

    private fun initTag(){
        tAdapter?.setNewData(tagList)
        tAdapter?.selectItem(0)
        initTagFragment()
    }

    private fun initVariable(){
        tagList.clear()
        //新版本暂无内置推荐标签
//        tagList.add(IndexTagBean("-1", "综合", "", 0, JsonArray(),false))
//        tagList.add(IndexTagBean("-2", "最新", "", 0, JsonArray(),false))
    }

    private fun initTagFragment(){
        fragments.clear()
        tagList.forEach {
            fragments.add(ArticleInfoFragment.newInstance(it.tagId))
        }

        vp_index_article_vp.adapter = object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }
            override fun getCount(): Int = fragments.size
            override fun getItemId(position: Int): Long {
                return tagList.get(position).tagId.toLong()
            }

        }

        vp_index_article_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(!fragments[position].isLoadComplete){
                    fragments[position].refreshData()
                }
                articleInfoChange(position)
                mIndex = position
            }
        })

        vp_index_article_vp.offscreenPageLimit = tagList.size
        articleInfoChange(0)
    }

    private fun articleInfoChange(position: Int) {
        vp_index_article_vp.setCurrentItem(position,true)
        tAdapter?.selectItem(position)
        index_tags_rv.smoothScrollToPosition(position)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun tagChange(event: TagChangeEvent){
        tagList.clear()
        tagList.addAll(C.userTag)
        initTag()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETTAG -> {
                    data?.let {
                        data as List<IndexTagBean>
                        initVariable()
                        tagList.addAll(data)
                        //存储用户标签数据
                        C.setUserTag(tagList)
                        initTag()
                    }
                }
                GGBContract.GETTAGALL -> {

                }
                else -> {

                }

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {
    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {
        RxToast.error("网络出差了")
    }

    companion object {
        fun newInstance(): IndexFragment {
            val fragment = IndexFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }

}