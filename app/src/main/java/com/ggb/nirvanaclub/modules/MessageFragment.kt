package com.ggb.nirvanaclub.modules

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.enums.ConversationType
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.ggb.nirvanaclub.MainActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.MessageFriendAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.modules.message.MessageAddFriendActivity
import com.ggb.nirvanaclub.modules.message.MessageChatActivity
import com.ggb.nirvanaclub.modules.message.MessageFriendListActivity
import com.ggb.nirvanaclub.utils.MessageChatUtils
import com.ggb.nirvanaclub.view.RxToast
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxui.view.dialog.RxDialogEditSureCancel
import kotlinx.android.synthetic.main.fragment_message.*
import org.jetbrains.anko.startActivity

class MessageFragment :BaseFragment(){

    private var mAdapter: MessageFriendAdapter?=null

    private var list:MutableList<Conversation> = mutableListOf()

    override fun getLayoutResource() = R.layout.fragment_message

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_message_main).init()

        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this)

        mAdapter = MessageFriendAdapter(list)
        rv_message_content_list.layoutManager = LinearLayoutManager(context)
        rv_message_content_list.adapter = mAdapter
        rv_message_content_list.setHasFixedSize(true)

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        mh_message_header.setColorSchemeResources(R.color.main_color)
        initEvent()
    }

    private fun initEvent(){
        tv_message_add.setOnClickListener {

            val rxDialog = RxDialogEditSureCancel(context)
            rxDialog.setTitle("请输入对方的能能号")

            rxDialog.sureView.setOnClickListener {
                if (!TextUtils.isEmpty(rxDialog.editText.text)){
                    val s = rxDialog.editText.text
                    JMessageClient.getUserInfo(s.toString(), object : GetUserInfoCallback() {
                        override fun gotResult(
                            responseCode: Int,
                            responseMessage: String,
                            info: UserInfo
                        ) {
                            if (responseCode == 0) {
                                if (info.isFriend) {
                                    RxToast.info(requireContext(), "已经是好友！", Toast.LENGTH_SHORT)?.show()
                                }else{
                                    val rxDialog2 = RxDialogEditSureCancel(requireContext())
                                    rxDialog2.setTitle("是否添加好友"+info.userName)
                                    rxDialog2.editText.setText("请输入验证信息")
                                    rxDialog2.sureView.setOnClickListener {
                                        ContactManager.sendInvitationRequest(info.userName,
                                            "b0935a121eb9326d4cafaad3",
                                            rxDialog.editText.text.toString(), object : BasicCallback() {
                                                override fun gotResult(
                                                    p0: Int,
                                                    p1: String?
                                                ) {
                                                    if (p0 == 0) {
                                                        RxToast.success(requireContext(), "申请发送成功！", Toast.LENGTH_SHORT)?.show()
                                                    }
                                                    Log.v("zzw", p0.toString() + p1)

                                                }

                                            })
                                        RxToast.info(requireContext(),"请等待对方同意！", Toast.LENGTH_SHORT, true)?.show()
                                        rxDialog2.cancel()
                                    }
                                    rxDialog2.cancelView.setOnClickListener { rxDialog2.cancel() }
                                    rxDialog2.show()

                                }
                            }
                        }
                    })
                }
                rxDialog.cancel()
            }
            rxDialog.cancelView.setOnClickListener { rxDialog.cancel() }
            rxDialog.show()
        }

        ll_message_main_friend.setOnClickListener {
            activity?.startActivity<MessageAddFriendActivity>()
        }
        ll_message_main_friend_list.setOnClickListener {
            activity?.startActivity<MessageFriendListActivity>()
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->

            list[position].updateConversationExtra("")
            adapter?.notifyItemChanged(position)

            if(list[position].type == ConversationType.single){

                val userInfo = list[position].targetInfo as UserInfo

                val myIntent = Intent(activity,MessageChatActivity::class.java)
                myIntent.putExtra(C.TYPE, C.SINGLE)
                myIntent.putExtra(C.DATA,userInfo.userName)
                myIntent.putExtra(C.DATA_TWO, MessageChatUtils.getName(userInfo))
                startActivity(myIntent)
            }else{
                val groupInfo = list[position].targetInfo as GroupInfo
                val myIntent = Intent(activity, MessageChatActivity::class.java)
                myIntent.putExtra(C.TYPE, C.GROUP)
                myIntent.putExtra(C.DATA,groupInfo.groupID)
                myIntent.putExtra(C.DATA_TWO,groupInfo.groupName)
                startActivity(myIntent)
            }
            checkNew()
        }

        swipe_refresh_layout.setOnRefreshListener {
            refresh()
        }
    }

    private fun refresh(){
        list.clear()
        if (JMessageClient.getConversationList()!=null){
            list.addAll(JMessageClient.getConversationList())
        }
        swipe_refresh_layout.finishRefresh()
        mAdapter?.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        refresh()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when(requestCode){
            C.REQUEST_CODE_ONE->{
                refresh()
            }
        }
    }

    //接受了在线信息
    public fun onEventMainThread(event: MessageEvent) {
        var handlable = false
        val msg = event.message
        if (msg.targetType == ConversationType.single) {
            val userInfo = msg.targetInfo as UserInfo
            for (bean in list) {
                if (bean.type == ConversationType.single) {
                    val userI = bean.targetInfo as UserInfo
                    if (userI.userName.equals(userInfo.userName)) {
                        bean.updateConversationExtra(C.NEW_MESSAGE)
                        handlable = true
                        mAdapter?.notifyItemChanged(list.indexOf(bean))
                    }
                }
            }
            if(!handlable){
                val conversation = JMessageClient.getSingleConversation(userInfo.userName)
                if (conversation.targetInfo is UserInfo) {
                    val bean = conversation
                    bean.updateConversationExtra(C.NEW_MESSAGE)
                    list.add(bean)

                }
                mAdapter?.notifyItemInserted(list.size - 1)
            }
        } else if (msg.targetType == ConversationType.group) {

            val groupInfo = msg.targetInfo as GroupInfo
            for (bean in list) {
                if (bean.type == ConversationType.group) {
                    val userI = bean.targetInfo as GroupInfo
                    if (userI.groupID == groupInfo.groupID) {
                        bean.updateConversationExtra(C.NEW_MESSAGE)
                        handlable = true
                        mAdapter?.notifyItemChanged(list.indexOf(bean))
                    }
                }
            }
            if(!handlable){
                val conversation = JMessageClient.getGroupConversation(groupInfo.groupID)
                val bean = conversation
                bean.updateConversationExtra(C.NEW_MESSAGE)

                list.add(bean)
                mAdapter?.notifyItemInserted(list.size - 1)
            }
        }
        checkNew()
    }

    private fun checkNew() {
        if (activity == null) {
            return
        }
        var hasNew = false
        for (bean in list) {
            if (bean.extra.equals(C.NEW_MESSAGE)) {
                setNew(true)
                hasNew = true
            }
        }
        if (!hasNew) {
            setNew(false)
        }
    }

    private fun setNew(news:Boolean){
        if(activity==null){
            return
        }
        (activity as MainActivity).setNew(news)
    }

    companion object {
        fun newInstance(): MessageFragment {
            val fragment = MessageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}