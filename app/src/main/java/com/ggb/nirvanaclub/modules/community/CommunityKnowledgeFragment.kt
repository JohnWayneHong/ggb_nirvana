package com.ggb.nirvanaclub.modules.community


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.CommunityKnowledgeAdapter
import com.ggb.nirvanaclub.base.BaseFragment
import com.ggb.nirvanaclub.bean.CommunityKnowledgeBean
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_community_knowledge.*

class CommunityKnowledgeFragment :BaseFragment(), CommunityKnowledgeAdapter.OnItemClickListener,GGBContract.View{

    private var present: GGBPresent?=null

    private var mAdapter: CommunityKnowledgeAdapter?=null

    override fun getLayoutResource() = R.layout.fragment_community_knowledge

    override fun beForInitView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        mAdapter = CommunityKnowledgeAdapter()
        rcy_community_knowledge.layoutManager = LinearLayoutManager(context)
        rcy_community_knowledge.adapter = mAdapter

        present?.communityKnowledge()

        val empty = View.inflate(context,R.layout.item_article_empty_view,null)
        mAdapter?.emptyView = empty

        mh_community_android_header.setColorSchemeResources(R.color.main_color)

        mAdapter?.setOnItemClickListener(this)
        initEvent()
    }

    private fun initEvent(){
        swipe_refresh_layout.setOnRefreshListener {
            getNewsList(true)
        }
        floating_action_btn.setOnClickListener {
            rcy_community_knowledge.run {
                if (LinearLayoutManager(activity).findFirstVisibleItemPosition() > 20) {
                    scrollToPosition(0)
                } else {
                    smoothScrollToPosition(0)
                }
            }
        }
        rcy_community_knowledge.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offsetY = recyclerView.computeVerticalScrollOffset()

                val al = offsetY / 600f * 0xff
                if (dy <= 0) {
                    if (floating_action_btn.visibility !== View.VISIBLE) {
                        floating_action_btn.visibility = View.VISIBLE
                    }
                } else {
                    if (floating_action_btn.visibility !== View.GONE) {
                        floating_action_btn.visibility = View.GONE
                    }
                }

            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun getNewsList(isRefreshList: Boolean){
        present?.communityKnowledge()
        swipe_refresh_layout.finishRefresh()

    }

    override fun onClick(bean: CommunityKnowledgeBean, pos: Int) {

        RxToast.warning(bean.children[pos].name)
    }

    companion object {
        fun newInstance(): CommunityKnowledgeFragment {
            val fragment = CommunityKnowledgeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.COMMUNITYKNOWLEDGE -> {
                    data?.let {
                        data as List<CommunityKnowledgeBean>

                        mAdapter?.setNewData(data)
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

}