package com.ggb.nirvanaclub.modules.user

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ggb.nirvanaclub.App.Companion.context
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.UserInfoAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.bean.UserInfoFakeBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.modules.message.MessageUserInfoActivity
import com.ggb.nirvanaclub.utils.DensityUtils
import com.ggb.nirvanaclub.utils.GlideEngine
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_user_info.*
import org.jetbrains.anko.startActivity
import org.litepal.LitePal

class UserInfoActivity : BaseActivity() {

    private var bannerHeight: Int = 0

    private var mAdapter : UserInfoAdapter?=null

    private var fragments = arrayListOf<BaseFragment>()

    private var titleList = arrayListOf<UserInfoFakeBean>()

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_user_info

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(tb_user_info_title).init()

        val user = LitePal.findLast(UserBean::class.java)

        bannerHeight = tb_user_info_title.layoutParams.height - tb_user_info_title.height - ImmersionBar.getStatusBarHeight(this)

        when (user.userLoginType){
            1->{
                val avatar = user.headshot?.let {
                    BitmapFactory.decodeByteArray(user.headshot,0,
                        it.size)
                }
                iv_user_photo.setImageBitmap(avatar)
            }
            3->{
                ImageLoaderUtil().displayHeadImage(this,user.userImg,iv_user_photo)
            }
        }
        mAdapter = UserInfoAdapter()
        rcy_user_info.layoutManager = GridLayoutManager(this,4, GridLayoutManager.VERTICAL,false)
        rcy_user_info.adapter = mAdapter


        titleList.add(UserInfoFakeBean("动态",1,false))
        titleList.add(UserInfoFakeBean("文章",2,false))
        titleList.add(UserInfoFakeBean("帖子",3,false))
        titleList.add(UserInfoFakeBean("更多",4,false))
        mAdapter?.setNewData(titleList)

        initFragment()
        initUserData(user)
    }

    override fun initEvent() {
        nsv_user_info.apply {
            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                // scrollY 为总滑动距离
                val alpha = scrollY.toFloat() / bannerHeight
                tb_user_info_title.alpha = if (alpha > 1) 1f else alpha
            })
        }
        sl_user_select_photo.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(3)
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(1)
                .isCamera(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .isEnableCrop(true)
                .isCompress(true)
                .isDragFrame(true)
                .freeStyleCropEnabled(true)
                .showCropGrid(false)
                .withAspectRatio(1,1)
                .hideBottomControls(true)
                .minimumCompressSize(1024)
                .forResult(4001)
        }
        ll_public_back.setOnClickListener {
            finish()
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            mAdapter?.selectItem(position)
            articleInfoChange(position)
        }
        iv_user_info_code.setOnClickListener {
            startActivity<MessageUserInfoActivity>()
        }
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(UserInfoOneFragment.newInstance())
        fragments.add(UserInfoTwoFragment.newInstance())
        fragments.add(UserInfoThreeFragment.newInstance())
        fragments.add(UserInfoFourFragment.newInstance())
        vp_user_info.initIndexList(fragments.size)

        vp_user_info.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getItemId(position: Int): Long = titleList.get(position).tagId.toLong()
        }

        vp_user_info.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(!fragments[position].isLoadComplete){
                    fragments[position].refreshData()
                }
                articleInfoChange(position)
            }
        })

        vp_user_info.offscreenPageLimit = titleList.size
        articleInfoChange(0)
    }

    private fun initUserData(user: UserBean) {
        tv_user_info_name_2.text = user.userName
        tv_user_info_name.text = user.userName
        if (SharedPreferencesUtil.getCommonBoolean(this,"is_setPhoto")){
            ImageLoaderUtil().displayImage(context,SharedPreferencesUtil.getCommonString(this,"User_save_photo"),iv_user_background)
        }
    }

    private fun articleInfoChange(position: Int) {
        vp_user_info.setCurrentItem(position,true)
        mAdapter?.selectItem(position)
        rcy_user_info.smoothScrollToPosition(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                4001 -> {
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    Log.e("TAG", "选择图片的路径是==========》: "+selectList[0].compressPath )
                    SharedPreferencesUtil.putCommonString(this,"User_save_photo",selectList[0].compressPath.toString())
                    SharedPreferencesUtil.putCommonBoolean(this,"is_setPhoto",true)
                    ImageLoaderUtil().displayImage(context,selectList[0].compressPath,iv_user_background)
                }
            }
        }
    }


}