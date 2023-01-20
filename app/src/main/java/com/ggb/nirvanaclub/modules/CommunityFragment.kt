package com.ggb.nirvanaclub.modules

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.HomeListAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.NewsListBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.RiceRefreshEvent
import com.ggb.nirvanaclub.modules.login.LoginActivity
import com.ggb.nirvanaclub.modules.user.NirvanaEarnActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.utils.GlideImageLoader
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.header_home_view.view.*
import org.jetbrains.anko.startActivity

class CommunityFragment : BaseFragment(), GGBContract.View{

    private lateinit var headView:View

    private lateinit var mAdapter: HomeListAdapter


    private var titleType = 0
    private var targetWalk = 0
    private var isInitView = false

    override fun getLayoutResource() = R.layout.fragment_community

    override fun initView() {
        ImmersionBar.with(requireActivity()).statusBarDarkFont(true).titleBar(tb_home_title).init()

        mAdapter = HomeListAdapter()
        rcy_home_list.layoutManager = LinearLayoutManager(activity)
        rcy_home_list.adapter = mAdapter

        headView = View.inflate(activity,R.layout.header_home_view,null)

        mAdapter.addHeaderView(headView)

        iv_home_head.setImageResource(R.mipmap.ic_home_bg_walk)
        Glide.with(requireActivity()).load(R.mipmap.ic_step_run).into(iv_note_type)

        val news = arrayListOf(
            NewsListBean("123","1-1","1-1-1","1-1-1-1","1-1-1-1-1"),
            NewsListBean("乖乖帮","2-2","2-2-2","2-2-2-2","2-2-2-2-2"),
            NewsListBean("乖乖熊","3-3","3-3-3","3-3-3-3","3-3-3-3-3")
        )
        mAdapter.setNewData(news)

        headView.ll_home_notice.visibility = View.VISIBLE
        headView.ban_top_list.visibility = View.VISIBLE
        headView.ban_bottom_list.visibility = View.VISIBLE


        val noticeList = arrayListOf(
            "乖乖帮的帮主是乖乖熊",
            "乖乖帮的最垃圾成员是乖乖兔",
            "乖乖帮牛逼"
        )
        headView.tb_home_notice.setDatas(noticeList)

        val bannerList = arrayListOf(
            "https://nirvana1234.xyz/img/dota2(1).0f43f2ab.jpg",
            "https://nirvana1234.xyz/img/dota2(2).1f7de4fc.jpg"
        )
        headView.ban_top_list.setImageLoader(GlideImageLoader())
        headView.ban_top_list.setImages(bannerList)
        headView.ban_top_list.start()

        val advertList = arrayListOf(
            "https://nirvana1234.xyz/img/dota2(1).0f43f2ab.jpg",
            "https://nirvana1234.xyz/img/dota2(2).1f7de4fc.jpg"
        )
        headView.ban_bottom_list.setImageLoader(GlideImageLoader())
        headView.ban_bottom_list.setImages(advertList)
        headView.ban_bottom_list.start()


        isInitView = true
        initEvent()
        initDayRice(RiceRefreshEvent(
            100,"2","2",100,1000,10f,20f
        ))
    }

    private fun initEvent(){
        hst_title.setOnSortSelectListener {
            titleType = it
            when(it){
                0 -> {
                    headView.rl_step_count.visibility = View.VISIBLE
                    headView.rl_car_data.visibility = View.GONE
                    headView.ll_step_rice.visibility = View.VISIBLE
                    headView.ll_car_rice.visibility = View.GONE
                    tv_note_list.text = "历史步数"
                    iv_home_head.setImageResource(R.mipmap.ic_home_bg_walk)
                    Glide.with(requireActivity()).load(R.mipmap.ic_step_run).into(iv_note_type)
                }
                1 -> {
                    headView.rl_step_count.visibility = View.GONE
                    headView.rl_car_data.visibility = View.VISIBLE
                    headView.ll_step_rice.visibility = View.GONE
                    headView.ll_car_rice.visibility = View.VISIBLE
                    tv_note_list.text = "车行记录"
                    iv_home_head.setImageResource(R.mipmap.ic_home_bg_car)
                    Glide.with(requireActivity()).load(R.mipmap.bear_crazy).into(iv_note_type)
                }
            }
        }
        headView.iv_home_share.setOnClickListener {
            if(C.USER_ID.isEmpty()){
                activity?.startActivity<LoginActivity>()
            }else{
                activity?.startActivity<NirvanaEarnActivity>()
            }
        }
    }

    fun initStep(step:Int){
        if(isInitView){
            headView.tv_today_step.text = step.toString()
            if(targetWalk != 0){
                headView.cp_walk_progress.value = ((step)/targetWalk.toFloat())*100
            }
        }
    }

    fun initDayRice(rice: RiceRefreshEvent?){
        if(rice == null){
            headView.tv_step_rice.text = "0.00"
            headView.tv_car_rice.text = "0.00"
            headView.tv_distances.text = "0"
            headView.tv_target_step.text = "今日目标0步"
            headView.tv_target_car.text = "今日目标0.0km"
            headView.cp_walk_progress.value = 0f
            headView.cp_car_progress.value = 0f
            headView.iv_target_complete.visibility = View.GONE
            headView.iv_car_complete.visibility = View.GONE
        }else{
            targetWalk = rice.targetWalk
            headView.tv_distances.text = String.format("%.1f", rice.mileage)
            headView.tv_step_rice.text = rice.walkRiceGrains
            headView.tv_car_rice.text = rice.drivingRiceGrains
            headView.tv_target_step.text = "今日目标${rice.targetWalk}步"
            headView.tv_target_car.text = "今日目标${String.format("%.1f", rice.targetDriving)}km"
            if(rice.targetWalk>0){
                if(rice.rotateMinStep+rice.minStep<rice.targetWalk){
                    headView.cp_walk_progress.value = ((rice.rotateMinStep+rice.minStep)/rice.targetWalk.toFloat())*100
                    headView.iv_target_complete.visibility = View.GONE
                }else{
                    headView.cp_walk_progress.value = 100f
                    headView.iv_target_complete.visibility = View.VISIBLE
                }
            }else{
                headView.iv_target_complete.visibility = View.GONE
            }

            if(rice.targetDriving>0){
                if(rice.mileage<rice.targetDriving){
                    headView.cp_car_progress.value = (rice.mileage/rice.targetDriving)*100
                    headView.iv_car_complete.visibility = View.GONE
                }else{
                    headView.cp_car_progress.value = 100f
                    headView.iv_car_complete.visibility = View.VISIBLE
                }
            }else{
                headView.iv_car_complete.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        headView.tb_home_notice.startViewAnimator()
        headView.ban_top_list.startAutoPlay()
        headView. ban_bottom_list.startAutoPlay()
    }



    override fun onStop() {
        super.onStop()
        headView.tb_home_notice.stopViewAnimator()
        headView.ban_top_list.stopAutoPlay()
        headView.ban_bottom_list.stopAutoPlay()
    }

    companion object {
        fun newInstance(): CommunityFragment {
            val fragment = CommunityFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {

    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }
}