package com.ggb.nirvanaclub.modules.message


import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.ContactManager
import cn.jpush.im.android.api.callback.GetUserInfoListCallback
import cn.jpush.im.android.api.model.UserInfo
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.MessageFriendListAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_friend_list.*


class MessageFriendListActivity : BaseActivity() , GGBContract.View{

    private var listOfFriend:MutableList<UserInfo> = mutableListOf()

    private lateinit var present: GGBPresent

    private var mAdapter: MessageFriendListAdapter?=null


    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_friend_list

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_message_friend_list).init()
        mAdapter = MessageFriendListAdapter()
        rv_message_friend_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv_message_friend_list.adapter = mAdapter

        val empty = View.inflate(this,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

    }

    override fun initEvent() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_message_list_talk->{

                    //TODO 开始聊天，首先获取当前用户的信息
                    val myIntent = Intent(this,MessageChatActivity::class.java)
//                    val myIntent = Intent(this,MessageChatActivity::class.java)
                    myIntent.putExtra(C.TYPE, C.SINGLE)
                    myIntent.putExtra(C.DATA,mAdapter?.data?.get(position)?.userName)

                    myIntent.putExtra(C.DATA_TWO, getUserName(mAdapter?.data?.get(position)))
                    startActivity(myIntent)
                }
            }
        }
    }

    private fun refreshFriend(){
        ContactManager.getFriendList(object : GetUserInfoListCallback() {
            override fun gotResult(i: Int, s: String, data: List<UserInfo>) {
                if (i == 0) {
                    listOfFriend.clear()
                    listOfFriend.addAll(data)

                    mAdapter?.setNewData(listOfFriend)
                }
            }
        })
    }

    fun getUserName(userInfo: UserInfo?): String? {
        if (userInfo == null) {
            return ""
        }
        return if (TextUtils.isEmpty(userInfo.nickname)) {
            userInfo.userName
        } else {
            userInfo.nickname
        }
    }

    override fun onResume() {
        super.onResume()
        refreshFriend()
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