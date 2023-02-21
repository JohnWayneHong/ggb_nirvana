package com.ggb.nirvanaclub.modules.message

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.event.ContactNotifyEvent
import cn.jpush.im.api.BasicCallback
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunitySubscriptAdapter
import com.ggb.nirvanaclub.adapter.MessageAddFriendAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.CacheDataUtil
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.activity_add_friend.*


class MessageAddFriendActivity : BaseActivity() , GGBContract.View{

    private var newList:MutableList<ContactNotifyEvent> = mutableListOf()

    private lateinit var present: GGBPresent

    private var mAdapter: MessageAddFriendAdapter?=null


    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_add_friend

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {

        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_message_add_friend).init()
        mAdapter = MessageAddFriendAdapter()
        rv_message_add_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_message_add_list.adapter = mAdapter

        val empty = View.inflate(this,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        newList.addAll(C.friendApply)
        mAdapter?.setNewData(newList)
    }

    override fun initEvent() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_message_add_confirm->{
                    val rxDialog = RxDialogSureCancel(this)
                    rxDialog.setContent("是否通过好友申请")
                    rxDialog.sureView.setOnClickListener {
                        ContactManager.acceptInvitation(
                            newList[0].fromUsername,
                            newList[0].getfromUserAppKey(),
                            object : BasicCallback() {
                                override fun gotResult(responseCode: Int, responseMessage: String) {
                                    if (0 == responseCode) {
                                        Log.e("TAG", "接收好友请求成功 ", )
                                        newList.removeAt(position)
                                        adapter.notifyItemRemoved(position)
                                    } else {
                                        //接收好友请求失败
                                        Log.e("TAG", "接收好友请求失败 ", )
                                    }
                                }
                            })
                        RxToast.success(this,"你们已成为好友啦！", Toast.LENGTH_SHORT, true)?.show()
                        rxDialog.cancel()
                    }
                    rxDialog.cancelView.setOnClickListener { rxDialog.cancel() }
                    rxDialog.show()
                }
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {

            }
        }
    }

    override fun onFailed(string: String?, isRefreshList: Boolean) {

    }

    override fun onNetError(boolean: Boolean, isRefreshList: Boolean) {

    }

}