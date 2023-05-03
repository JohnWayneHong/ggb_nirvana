package com.ggb.nirvanaclub.modules.user


import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ArticleHistoryListAdapter
import com.ggb.nirvanaclub.adapter.SearchArticleListAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.ArticleHistoryBean
import com.ggb.nirvanaclub.bean.SearchArticleBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.modules.article.ArticleActivity
import com.ggb.nirvanaclub.modules.login.LoginActivity
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.CopyUtils
import com.gyf.immersionbar.ImmersionBar
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialog
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.activity_history_article.*
import org.jetbrains.anko.startActivity


class HistoryArticleActivity : BaseActivity(),GGBContract.View {

    private var present: GGBPresent?=null

    private var mAdapter: ArticleHistoryListAdapter?= null

    private var pager = 0

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_history_article

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_history_article).init()

        present?.getArticleHistory(pager,7)

        mAdapter = ArticleHistoryListAdapter()
        rcy_history_article.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rcy_history_article.adapter = mAdapter

        val empty = View.inflate(this,R.layout.item_empty_comment_view,null)
        mAdapter?.emptyView = empty

    }

    override fun initEvent() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            mAdapter?.closeAll(null)
            when(view.id){
                R.id.tv_history_copy->{
                    CopyUtils.copyText(C.NIRVANA_WEB_ADDRESS+"blog/"+mAdapter?.getItem(position)?.blog?.id)
                    RxToast.success("复制成功！")
                }
                R.id.tv_history_open->{
                    if (mAdapter?.getItem(position)?.blog?.id.isNullOrEmpty()){
                        RxToast.error("链接为空")
                        return@setOnItemChildClickListener
                    }
                    CopyUtils.openBrowser(this, C.NIRVANA_WEB_ADDRESS+"blog/"+mAdapter?.getItem(position)?.blog?.id)

                }
                R.id.tv_history_delete->{
                    mAdapter?.getItem(position)?.id?.let { present?.deleteSingleArticleHistory(it) }
                }
                R.id.ll_history_content->{
                    RxToast.info(mAdapter?.getItem(position)?.id.toString())
                }

            }
        }
        iv_article_col_back.setOnClickListener {
            finish()
        }
        iv_history_clear.setOnClickListener {
            if (mAdapter?.data?.isEmpty() == true){
                RxToast.info("暂无可清除的浏览记录，快去阅读叭！")
                return@setOnClickListener
            }
            val rxDialog = RxDialogSureCancel(this)
            rxDialog.setContent("此操作无法恢复，您确定要删除吗？！")
            rxDialog.sureView.setOnClickListener {
                present?.deleteAllArticleHistory()
                rxDialog.cancel()
            }
            rxDialog.cancelView.setOnClickListener {
                rxDialog.cancel()
            }
            rxDialog.show()
        }
        mAdapter?.setOnLoadMoreListener {
            getHistoryList(false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getHistoryList(true)
        }
    }

    private fun getHistoryList(isRefreshList: Boolean){
        if(isRefreshList){
            pager = 0
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present?.getArticleHistory(pager,7)
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.ARTICLEHISTORY -> {
                    data?.let {
                        data as List<ArticleHistoryBean>
                        if (pager<=0){
                            swipe_refresh_layout.finishRefresh()
                            mAdapter?.setEnableLoadMore(true)
                            mAdapter?.setNewData(data)
                        }else{
                            if (data.isNullOrEmpty()){
                                mAdapter?.loadMoreEnd()
                            }else{
                                mAdapter?.addData(data)
                                mAdapter?.loadMoreComplete()
                            }
                        }
                    }
                }
                GGBContract.DELETESINGLEARTICLEHISTORY->{
                    RxToast.success("删除记录成功")
                    getHistoryList(true)
                }
                GGBContract.DELETEALLARTICLEHISTORY->{
                    RxToast.success("所有浏览记录已删除成功")
                    getHistoryList(true)
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