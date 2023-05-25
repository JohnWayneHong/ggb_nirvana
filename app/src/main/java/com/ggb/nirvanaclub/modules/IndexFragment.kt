package com.ggb.nirvanaclub.modules

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import cn.jpush.android.api.JPushInterface
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.IndexTagAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.AppUpdateListBean
import com.ggb.nirvanaclub.bean.ArticleContentBean
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.TagChangeEvent
import com.ggb.nirvanaclub.modules.article.ArticleActivity
import com.ggb.nirvanaclub.modules.article.ArticleInfoFragment
import com.ggb.nirvanaclub.modules.article.SearchArticleActivity
import com.ggb.nirvanaclub.modules.login.LoginActivity
import com.ggb.nirvanaclub.modules.tag.IndexTagSettingActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.AppUtils
import com.ggb.nirvanaclub.utils.ScreenUtils
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.ggb.nirvanaclub.view.CircleImageView
import com.ggb.nirvanaclub.view.dialog.DownloadProgressDialog
import com.king.app.updater.AppUpdater
import com.king.app.updater.callback.UpdateCallback
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialog
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.lib.tinker.TinkerInstaller
import kotlinx.android.synthetic.main.fragment_index.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import per.goweii.anylayer.Layer
import per.goweii.anylayer.notification.NotificationLayer
import java.io.File

class IndexFragment :BaseFragment(),GGBContract.View{

    private var mIndex = 0

    private var present:GGBPresent?=null

    private var tAdapter :IndexTagAdapter?=null

    private var tagList = arrayListOf<IndexTagBean>()

    private var fragments = arrayListOf<BaseFragment>()

    private var processDialog: DownloadProgressDialog? = null

    private var patchPath = ""

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
        initLastTimeRead()
        processDialog = DownloadProgressDialog(requireContext())
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
//        tv_index_search.text = "当前有Bug！我草,我累个槽,熊熊被入侵"
//        iv_test_2.visibility = View.VISIBLE
        ll_index_search.setOnClickListener {
            Log.e("TAG", "RegistrationID=============>: "+JPushInterface.getRegistrationID(context) )
            activity?.startActivity<SearchArticleActivity>()
        }
        iv_test.setOnClickListener {
            present?.getIsHavePatch(AppUtils.getVersionName(context),AppUtils.getAppVersionCode(context).toString())

//            val path =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                Environment.DIRECTORY_PICTURES + File.separator + "GGBScreen/"
//            } else {
//                Environment.getExternalStorageDirectory().path + "/GGBScreen/"
//            }
//
//            val patch: String = path + "patch_signed.apk"
//            if (Tinker.isTinkerInstalled()) {
//
//                TinkerInstaller.onReceiveUpgradePatch(context, patch)
//                context?.toast("补丁加载成功！！！ 加载路径是===》  $patch")
//            }else{
//                context?.toast("补丁包更新失败!!!")
//            }

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

    private fun initLastTimeRead(){
        val lastReadId = SharedPreferencesUtil.getLastTimeReadString(context,"last_time_read")
        if (!lastReadId.isNullOrEmpty()){
            Log.e("TAG", "调用搜索文章:==> "+lastReadId )
            present?.getArticle(lastReadId)
        }
    }

    private fun initTips(data: ArticleContentBean) {
        NotificationLayer(requireContext())
            .contentView(R.layout.dialog_message_notification)
            .bindData {
                val child: View = it.requireView<View>(R.id.dialog_message_notification)
                child.setPadding(0, ScreenUtils.getStatusHeight(requireContext()), 0, 0)
                val tvTitle: TextView =
                    it.requireView<TextView>(R.id.dialog_read_later_notification_tv_title)
                val tvDesc: TextView =
                    it.requireView<TextView>(R.id.dialog_read_later_notification_tv_desc)
                val ivAvatar: CircleImageView =
                    it.requireView<CircleImageView>(R.id.civ_message_notification_avatar)

                tvTitle.text = "是否继续阅读？"
                tvDesc.text = data.introduction
            }
            .onClickToDismiss({ layer, view ->
                activity?.startActivity<ArticleActivity>(Pair("articleId",data.id))

            },R.id.dialog_message_notification_ll_content)
            .onDismissListener(object : Layer.OnDismissListener{
                override fun onDismissing(layer: Layer) {
                }

                override fun onDismissed(layer: Layer) {
                }

            }).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun tagChange(event: TagChangeEvent){
        tagList.clear()
        tagList.addAll(C.userTag)
        initTag()
    }

    private fun initPatchDownLoad(apkBean: AppUpdateListBean) {
        processDialog?.setCanceledOnTouchOutside(false)

        val mAppupdater = requireContext().let {
            AppUpdater.Builder(it)
                .setUrl(apkBean.downloadUrl)
                .setVersionCode(apkBean.versionCode.toLong())
                .setFilename("patch_signed.apk")
                .setVibrate(true)
                .setSound(true)
                .setInstallApk(false)
                .setShowNotification(false)

                .build()
        }



        mAppupdater.setUpdateCallback(object : UpdateCallback {
            override fun onDownloading(isDownloading: Boolean) {
                processDialog?.show()
                processDialog?.setTitle("发现补丁包，补丁包下载进度如下")

            }

            override fun onStart(url: String?) {
                patchPath = mAppupdater.downLoadSavePath

                Log.e("TAG", "initPatchDownLoad:====>$patchPath ", )
            }

            override fun onProgress(progress: Long, total: Long, isChanged: Boolean) {
                processDialog?.setCurrentValue(total.toInt(),progress.toInt())

            }

            override fun onFinish(file: File?) {
                processDialog?.dismiss()


                val patch = "$patchPath/patch_signed.apk"
                if (Tinker.isTinkerInstalled()) {
                    TinkerInstaller.onReceiveUpgradePatch(requireContext(), patch)
                    RxToast.success("补丁包下载完成，App重启后生效！"+patch)
                }else{
                    RxToast.error("补丁包下载完成，但是加载补丁包装置出了问题！"+patch)
                }

                val rxDialog = RxDialogSureCancel(requireContext())
                rxDialog.setContent("牛蛙呐刚才修复了一个Bug，建议一会重启App")
                rxDialog.sureView.text = "我知道了，一会重启"
                rxDialog.cancelView.text = "我不重启"
                rxDialog.sureView.setOnClickListener {
                    rxDialog.cancel()
                }
                rxDialog.cancelView.setOnClickListener {
                    RxToast.info(requireContext(),"必须重启！！！", Toast.LENGTH_SHORT, true)?.show()
                }
                rxDialog.show()
            }

            override fun onError(e: Exception?) {
                e?.message?.let { RxToast.error(it) }
            }

            override fun onCancel() {

            }

        }).start()


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
                GGBContract.ARTICLE -> {
                    data as ArticleContentBean
                    initTips(data)
                    //检查当前版本是否有补丁包
//                    present?.getIsHavePatch(AppUtils.getVersionName(context),AppUtils.getAppVersionCode(context).toString())
                }
                GGBContract.GETPATCH -> {
                    data?.let {
                        data as AppUpdateListBean
                        initPatchDownLoad(data)
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