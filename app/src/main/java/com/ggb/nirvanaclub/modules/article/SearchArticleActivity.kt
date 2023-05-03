package com.ggb.nirvanaclub.modules.article


import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.SearchArticleListAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.SearchArticleBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_search_article.*
import org.jetbrains.anko.startActivity


class SearchArticleActivity : BaseActivity(),GGBContract.View {

    private var present: GGBPresent?=null

    private var mAdapter: SearchArticleListAdapter?= null

    private var pager = 0

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_search_article

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_search_article).init()

        present?.searchArticle(pager,et_search_content.text.toString(),7)

        mAdapter = SearchArticleListAdapter()
        rcy_search_article.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rcy_search_article.adapter = mAdapter

    }

    override fun initEvent() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity<ArticleActivity>(Pair("articleId",mAdapter?.getItem(position)?.id))
        }
        iv_article_col_back.setOnClickListener {
            finish()
        }
        iv_search_article.setOnClickListener {
            pager = 0
            present?.searchArticle(pager,et_search_content.text.toString(),7)
        }
        tv_search_type_time.setOnClickListener {
            pager = 0
            present?.searchArticle(pager,et_search_content.text.toString(),7)
            tv_search_type_time.setTextColor(resources.getColor(R.color.main_color))
            tv_search_type_hot.setTextColor(resources.getColor(R.color.text_main_color))
        }
        tv_search_type_hot.setOnClickListener {
            pager = 0
            present?.searchArticle(pager,et_search_content.text.toString(),7)
            tv_search_type_hot.setTextColor(resources.getColor(R.color.main_color))
            tv_search_type_time.setTextColor(resources.getColor(R.color.text_main_color))

        }
        et_search_content.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword: String = v.text.toString().trim()
                    pager = 0
                    present?.searchArticle(pager,keyword,7)
                    return true
                }
                return false
            }
        })
        mAdapter?.setOnLoadMoreListener {
            getSearchList(false,false)
        }
        swipe_refresh_layout.setOnRefreshListener {
            getSearchList(true,false)
        }
    }

    private fun getSearchList(isRefreshList: Boolean,isShow:Boolean){
        if(isRefreshList){
            pager = 0
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }
        present?.searchArticle(pager,et_search_content.text.toString(),7)
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.SEARCHARTICLEBYTIME -> {
                    data?.let {
                        data as List<SearchArticleBean>
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