package com.ggb.nirvanaclub.modules.tag


import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.IndexTagSettingChoseAdapter
import com.ggb.nirvanaclub.adapter.IndexTagSettingUnChoseAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.TagChangeEvent
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.google.android.flexbox.FlexboxLayoutManager
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_index_tag_setting.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class IndexTagSettingActivity : BaseActivity(),GGBContract.View {

    private var present: GGBPresent?=null

    private var mAdapter:IndexTagSettingChoseAdapter?= null
    private var uAdapter: IndexTagSettingUnChoseAdapter?= null

    private val itemTouchHelper = ItemTouchHelper(IndexTagSettingChosenItemTouchHelperCallback())

    private var tagList = arrayListOf<IndexTagBean>()
    private var tagAllList = arrayListOf<IndexTagBean>()

    override fun getTitleType() =  PublicTitleData(C.TITLE_NORMAL,"标签管理")

    override fun getLayoutResource(): Int = R.layout.activity_index_tag_setting

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(public_title).init()

        itemTouchHelper.attachToRecyclerView(rcy_index_tag_chosen_rv)

        uAdapter = IndexTagSettingUnChoseAdapter()
        rcy_index_tag_unchosen_rv.layoutManager = FlexboxLayoutManager(this)
        rcy_index_tag_unchosen_rv.adapter = uAdapter

        mAdapter = IndexTagSettingChoseAdapter(itemTouchHelper)
        rcy_index_tag_chosen_rv.layoutManager = FlexboxLayoutManager(this)
        rcy_index_tag_chosen_rv.adapter = mAdapter


        initTagAdapter()
        present?.getTagAll()

    }

    override fun initEvent() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.iv_index_tag_setting_item_remove->{
                    mAdapter?.remove(position)
                }
            }
        }
        uAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.btn_index_tag_setting_item->{
                    if (mAdapter?.getEnableChange() == true){
                        val addTag = uAdapter?.data?.get(position)
                        if (addTag != null) {
                            mAdapter?.addData(addTag)
                            uAdapter?.remove(position)
                            uAdapter?.notifyItemChanged(position)
                            mAdapter?.notifyDataSetChanged()
                        }
                    }

                }
            }
        }
        index_tag_setting_edit_btn.setOnClickListener {
            if (mAdapter?.getEnableChange() == true){
                mAdapter?.isEnableChange(false)
                index_tag_setting_edit_btn.text = "编辑"
                tagList.clear()
                mAdapter?.data?.forEach {
                    tagList.add(it)
                }
                C.setUserTag(tagList)

                present?.saveUserTags(tagList.joinToString(separator = ",") { it.tagId })
                EventBus.getDefault().post(TagChangeEvent(1))
            }else{
                mAdapter?.isEnableChange(true)
                index_tag_setting_edit_btn.text = "完成"

            }
        }
    }

    private fun initTagAdapter(){
        mAdapter?.setNewData(C.userTag)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.GETTAGALL -> {
                    data?.let {
                        data as List<IndexTagBean>
                        tagAllList.clear()
                        tagAllList.addAll(data)
                        uAdapter?.setNewData(tagAllList)
                    }
                }
                GGBContract.SAVEUSERTAGS ->{
                    toast("用户标签保存成功！")
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

    inner class IndexTagSettingChosenItemTouchHelperCallback : ItemTouchHelper.Callback() {

        /**
         * 返回值 int 根据位移包含很多数据，拖拽只需要设置 dragFlags，不用设置 swipe
         */
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            // 全方位都可以拖动
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        /**
         * @param viewHolder 当前被拖拽的 vh
         * @param target 拖拽到的地方的 vh
         */
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val data = mAdapter?.data?.get(viewHolder.adapterPosition)
            // 得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            // 拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            return if (toPosition==0||toPosition==1){
                false
            }else{
                mAdapter?.data?.removeAt(fromPosition)
                mAdapter?.data?.add(toPosition,data)
                mAdapter?.notifyItemMoved(fromPosition,toPosition)
                true
            }

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }

}