package com.ggb.nirvanaclub.modules.article


import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.SearchArticleListAdapter
import com.ggb.nirvanaclub.adapter.SearchGuessAdapter
import com.ggb.nirvanaclub.adapter.SearchListAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.IndexTagBean
import com.ggb.nirvanaclub.bean.SearchArticleBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.google.android.flexbox.FlexboxLayoutManager
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_search_article.*
import org.jetbrains.anko.startActivity
import org.json.JSONArray
import org.json.JSONObject


class SearchArticleActivity : BaseActivity(),GGBContract.View {

    private var present: GGBPresent?=null

    private var mAdapter: SearchArticleListAdapter?= null
    private var hAdapter: SearchListAdapter?= null
    private var gAdapter: SearchGuessAdapter?= null

    private var pager = 0

    private var searchHistory = ""

    private var jsonArray = JSONArray()

    private var isInitDiscover = true

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_search_article

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).navigationBarColor(R.color.white).titleBar(tb_search_article).init()

        getSearchList(true)
//        present?.searchArticle(pager,et_search_content.text.toString(),7)

        mAdapter = SearchArticleListAdapter()

        rcy_search_article.layoutManager = object :LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

//        rcy_search_article.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rcy_search_article.adapter = mAdapter
        rcy_search_article.isNestedScrollingEnabled = false

        nsv_search_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                Log.i("TAG", "Scroll DOWN")
            }
            if (scrollY < oldScrollY) {
                Log.i("TAG", "Scroll UP")
            }
            if (scrollY == 0) {
                Log.i("TAG", "TOP SCROLL")
            }
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                Log.i("TAG", "BOTTOM SCROLL")
//                if (enableLoadMore) {
//                    getSearchList(false)
//                }
                getSearchList(false)

            }
        })

        val empty = View.inflate(this,R.layout.item_empty_comment_view_2,null)
        mAdapter?.emptyView = empty

        hAdapter = SearchListAdapter()
        rcy_search_history.layoutManager = FlexboxLayoutManager(this)
        rcy_search_history.adapter = hAdapter

        gAdapter = SearchGuessAdapter()
        rcy_guess_search.layoutManager = GridLayoutManager(this, 2,LinearLayoutManager.HORIZONTAL,false)
        rcy_guess_search.adapter = gAdapter

        initSearchHistory()
        //初始化设置搜索发现
        mAdapter?.initDiscoverOrSearch(2)
        present?.getTag()
    }

    override fun initEvent() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity<ArticleActivity>(Pair("articleId",mAdapter?.getItem(position)?.id))
        }
        hAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.btn_search_item->{
                    pager = 0
                    hAdapter?.getItem(position)?.let { present?.searchArticle(pager, it,7) }
                }
                R.id.iv_search_remove->{
                    deleteSingleHistory(hAdapter!!.data[position])

//                    val afterData = hAdapter?.data?.filter {
//                        it != hAdapter?.data?.get(position)
//                    }
//                    hAdapter?.setNewData(afterData)
                }
            }
        }
        iv_article_col_back.setOnClickListener {
            finish()
        }
        iv_search_article.setOnClickListener {
//            pager = 0
//            present?.searchArticle(pager,et_search_content.text.toString(),7)
            getSearchList(true)
        }

        iv_search_history_del.setOnClickListener {
            hAdapter?.isEnableDelete(true)
            tv_search_history_del_all.visibility = View.VISIBLE
            tv_search_history_del_ok.visibility = View.VISIBLE
            iv_search_history_del.visibility = View.GONE
        }
        tv_search_history_del_all.setOnClickListener {
            SharedPreferencesUtil.clearSearchHistoryData(this)
            ll_search_history.visibility = View.GONE
            ll_search_history_2.visibility = View.GONE
        }
        tv_search_history_del_ok.setOnClickListener {
            tv_search_history_del_all.visibility = View.GONE
            tv_search_history_del_ok.visibility = View.GONE
            iv_search_history_del.visibility = View.VISIBLE
            hAdapter?.isEnableDelete(false)
        }
        et_search_content.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword: String = v.text.toString().trim()

                    if (keyword.isNotEmpty()){
                        isInitDiscover = false
                        pager = 0
                        present?.searchArticle(pager,keyword,7)

                        val json = JSONObject()
                        json.put("content",keyword)

                        //如果出现一样的，删除后加到第一个历史列表
                        var isEqual = false
                        var equalPosition = 0
                        for (i in 0 until jsonArray.length()){
                            val item = jsonArray.getJSONObject(i)
                            val content = item.getString("content")
                            if (keyword == content){
                                isEqual = true
                                equalPosition = i
                                break
                            }
                        }
                        if (isEqual){
                            jsonArray.remove(equalPosition)
                        }else{
                            if (jsonArray.length() >= C.SEARCH_HISTORY_SIZE){
                                jsonArray.remove(0)
                            }
                        }

                        jsonArray.put(json)

                        SharedPreferencesUtil.putSearchHistoryString(this@SearchArticleActivity,"history_search",jsonArray.toString())

                        initSearchHistory()
                    }else{
                        isInitDiscover = true
                    }
                    return true
                }
                return false
            }
        })

        et_search_content.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val str = s.toString()
                isInitDiscover = false
                //文字变动 ， 有未发出的搜索请求，应取消
                if (mHandler.hasMessages(1)) {
                    mHandler.removeMessages(1)
                }
                if (!TextUtils.isEmpty(str)) {
                    //否则延迟500ms开始搜索
                    val msg = Message.obtain()
                    msg.what = 1
                    //携带当前值
                    msg.obj = s.toString()
                    mHandler.sendMessageDelayed(msg, 500)
                }
            }
        })

//        mAdapter?.setOnLoadMoreListener {
//            getSearchList(false)
//        }
        swipe_refresh_layout.setOnRefreshListener {
            getSearchList(true)
        }

        tv_search_change.setOnClickListener {
            present?.getTag()
        }
    }

    private fun getSearchList(isRefreshList: Boolean){
        if(isRefreshList){
            pager = 0
            mAdapter?.loadMoreComplete()
            mAdapter?.setEnableLoadMore(false)
        }else{
            pager++
            swipe_refresh_layout.finishRefresh()
        }

        mAdapter?.initDiscoverOrSearch(if (isInitDiscover) 2 else 1)
        present?.searchArticle(pager,et_search_content.text.toString(),10)
    }

    private fun initSearchHistory(){
        searchHistory = SharedPreferencesUtil.getSearchHistoryString(this@SearchArticleActivity,"history_search")
        val history = arrayListOf<String>()
        jsonArray = if (searchHistory.isEmpty()){
            JSONArray()
        }else{
            JSONArray(searchHistory)
        }
        if (searchHistory.isEmpty()){
            ll_search_history.visibility = View.GONE
            ll_search_history_2.visibility = View.GONE
        }else{
            ll_search_history.visibility = View.VISIBLE
            ll_search_history_2.visibility = View.VISIBLE
        }
        for (i in 0 until jsonArray.length()){
            val item = jsonArray.getJSONObject(i)
            val content = item.getString("content")
            history.add(content)
        }
        hAdapter?.setNewData(history.reversed())
    }

    private fun deleteSingleHistory(deleteHistory:String){
        val afterDeleteHistory = arrayListOf<String>()
        var deletePosition = 0

        for (i in 0 until jsonArray.length()){
            val item = jsonArray.getJSONObject(i)
            val content = item.getString("content")
            if (content == deleteHistory){
                deletePosition = i
            }else{
                afterDeleteHistory.add(content)
            }
        }

        jsonArray.remove(deletePosition)
        if (afterDeleteHistory.isEmpty()){
            SharedPreferencesUtil.clearSearchHistoryData(this)
            ll_search_history.visibility = View.GONE
            ll_search_history_2.visibility = View.GONE
        }else{
            SharedPreferencesUtil.clearSearchHistoryData(this)
            SharedPreferencesUtil.putSearchHistoryString(this,"history_search",jsonArray.toString())
            hAdapter?.setNewData(afterDeleteHistory.reversed())
        }
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what == 1){
                if (msg.obj.toString() == et_search_content.text.toString()){//进行判断
//                    pager = 0
                    getSearchList(true)
//                    present?.searchArticle(pager,et_search_content.text.toString(),7)
                }
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when(flag) {
                GGBContract.SEARCHARTICLEBYTIME -> {
                    data?.let {
                        data as List<SearchArticleBean>
                        if (isInitDiscover){
                            if (pager<=0){
                                swipe_refresh_layout.finishRefresh()
                                mAdapter?.setEnableLoadMore(true)
                                var count = 1
                                data.forEach {
                                    if (count<=3){
                                        it.selfLevel = count
                                        count++
                                    }
                                }
                                mAdapter?.setNewData(data)
                            }else{
                                if (data.isNullOrEmpty()){
                                    mAdapter?.loadMoreEnd()
                                }else{
                                    mAdapter?.addData(data)
                                    mAdapter?.loadMoreComplete()
                                }
                            }
                        }else{
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
                }
                GGBContract.GETTAG -> {
                    data?.let {
                        data as List<IndexTagBean>
                        val guess = arrayListOf<IndexTagBean>()
                        for (i in 0 until 6){
                            guess.add(data.random())
                        }
                        gAdapter?.setNewData(guess)
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

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}