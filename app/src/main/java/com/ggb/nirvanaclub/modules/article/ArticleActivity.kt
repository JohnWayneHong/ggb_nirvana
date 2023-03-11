package com.ggb.nirvanaclub.modules.article

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.ArticleContentBean
import com.ggb.nirvanaclub.bean.UserBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.MarkwonUtils
import com.ggb.nirvanaclub.utils.ScreenUtils
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import com.tamsiree.rxui.view.likeview.RxShineButton
import kotlinx.android.synthetic.main.activity_article.*
import org.litepal.LitePal
import per.goweii.anylayer.Layer
import per.goweii.anylayer.guide.GuideLayer
import per.goweii.anylayer.notification.NotificationLayer

class ArticleActivity : BaseActivity() , GGBContract.View{

    private var articleId = ""

    private lateinit var present: GGBPresent

    override fun getTitleType() =  PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource(): Int = R.layout.activity_article

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        articleId = intent.getStringExtra("articleId").toString()
        Log.e("TAG", "收到fragment传递过来的id: $articleId")

        present.getArticle(articleId)
        rsb_article_like.init(this)
    }

    override fun initEvent() {
        //测试导航
//        window.decorView.doOnLayout {
//            showGuideDialogIfNeeded()
//        }
        nsv_article_scroll.apply {
            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                // scrollY 为总滑动距离
                if (scrollY != 0 && iv_article_col_avatar.visibility == View.INVISIBLE) {
                    iv_article_col_avatar.visibility = View.VISIBLE
                    tv_article_col_auth.visibility = View.VISIBLE
                    btn_article_col_subscribe.visibility = View.VISIBLE
                } else if (scrollY == 0 && iv_article_col_avatar.visibility == View.VISIBLE) {
                    iv_article_col_avatar.visibility = View.INVISIBLE
                    tv_article_col_auth.visibility = View.INVISIBLE
                    btn_article_col_subscribe.visibility = View.INVISIBLE
                }
            })
        }
        article_col_back.setOnClickListener {
            finish()
        }
    }

    private fun initArticle(data: ArticleContentBean) {
        tv_article_title.text = data.title
        tv_article_authName.text = data.authName
        tv_article_all_Count.text = "阅读 ${data.readCount} 点赞 ${data.likeCount} 评论 ${data.commentCount}"
        tv_article_createTime.text = data.createTime

        tv_article_bottom_commentCount.text = data.commentCount.toString()
        tv_article_like_count.text = data.likeCount.toString()

        ImageLoaderUtil().displayHeadImage(this,data.authAvatar,iv_article_avatar)
        ImageLoaderUtil().displayHeadImage(this,data.authAvatar,iv_article_col_avatar)
        tv_article_col_auth.text = data.authName

        rsb_article_like.setChecked(data.amILike)
        rsb_article_like.setOnCheckStateChangeListener(object :RxShineButton.OnCheckedChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onCheckedChanged(view: View?, checked: Boolean) {
                if (isLoginCheck()){
                    present.likeOrCancelArticle(articleId,checked)
                    if (checked){
                        val count = tv_article_like_count.text.toString().toLong()

                        tv_article_like_count.text = (count+1).toString()
                        val rxDialog = RxDialogSureCancel(this@ArticleActivity)
                        rxDialog.setContent("你还没有关注文章作者，是否同时关注TA")
                        rxDialog.sureView.setOnClickListener {
                            RxToast.info(this@ArticleActivity,"关注成功！", Toast.LENGTH_SHORT, true)?.show()
                            rxDialog.cancel() }
                        rxDialog.cancelView.setOnClickListener { rxDialog.cancel() }
                        rxDialog.show()
                    }else{
                        val count = tv_article_like_count.text.toString().toLong()
                        tv_article_like_count.text = (count-1).toString()
                    }
                }
            }
        })
        rsb_article_like.setOnClickListener {
            if (!isLoginCheck()){
                rsb_article_like.setChecked(data.amILike)
                val rxDialog = RxDialogSureCancel(this@ArticleActivity)
                rxDialog.setContent("你还没有登录，无法进行点赞哦")
                rxDialog.sureView.setOnClickListener {
                    RxToast.info(this@ArticleActivity,"快去登录叭！", Toast.LENGTH_SHORT, true)?.show()
                    rxDialog.cancel() }
                rxDialog.cancelView.setOnClickListener { rxDialog.cancel() }
                rxDialog.show()
                return@setOnClickListener
            }
        }
        ll_article_share.setOnClickListener {

        }
    }

    private fun isLoginCheck():Boolean{
        val user = LitePal.findLast(UserBean::class.java)

        if (user!=null){
            //第三方登录 账号虚假 后期可改
            if (user.userLoginType==1){
//                rsb_article_like.isEnabled = false
//                btn_article_subscribe.isEnabled = false
//                btn_article_col_subscribe.isEnabled = false
                return false
            }else {
                rsb_article_like.isEnabled = true
                btn_article_subscribe.isEnabled = true
                btn_article_col_subscribe.isEnabled = true
                return true
            }
        }else{
//            rsb_article_like.isEnabled = false
//            btn_article_subscribe.isEnabled = false
//            btn_article_col_subscribe.isEnabled = false
            return false
        }
    }

    private fun showGuideDialogIfNeeded() {
//        if (GuideSPUtils.getInstance().isArticleGuideShown) {
//            return
//        }
        window?.decorView?.post {
            showGuideBackBtnDialog {
//                showGuideDoubleTapDialog {
//                    showGuidePreviewImageDialog {
//                        GuideSPUtils.getInstance().setArticleGuideShown()
//                    }
//                }
            }
        }
    }

    private fun showGuideBackBtnDialog(onDismiss: () -> Unit) {
        GuideLayer(this@ArticleActivity)
            .backgroundColorInt(resources.getColor(R.color.colorDialogBg))
            .mapping(GuideLayer.Mapping().apply {
                targetView(article_col_back)
                cornerRadius(9999F)
                guideView(LayoutInflater.from(this@ArticleActivity)
                    .inflate(R.layout.dialog_guide_tip, null, false).apply {
                        findViewById<TextView>(R.id.dialog_guide_tv_tip).apply {
                            text = "长按返回按钮有更多快捷菜单~"
                        }
                    })
                marginLeft(16)
                horizontalAlign(GuideLayer.Align.Horizontal.TO_RIGHT)
                verticalAlign(GuideLayer.Align.Vertical.CENTER)
            })
            .mapping(GuideLayer.Mapping().apply {
                val cx = window?.decorView?.width ?: 0 / 2
                val cy = window?.decorView?.height ?: 0 / 2
                targetRect(Rect(cx, cy, cx, cy))
                guideView(LayoutInflater.from(this@ArticleActivity)
                    .inflate(R.layout.dialog_guide_btn, null, false).apply {
                        findViewById<TextView>(R.id.dialog_guide_tv_btn).apply {
                            text = "下一个"
                        }
                    })
                marginBottom(48)
                horizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                verticalAlign(GuideLayer.Align.Vertical.CENTER)
                onClick(Layer.OnClickListener { layer, _ ->
                    layer.dismiss()
                }, R.id.dialog_guide_tv_btn)
            })
            .onVisibleChangeListener(object : Layer.OnVisibleChangeListener {
                override fun onShow(layer: Layer) {
                }

                override fun onDismiss(layer: Layer) {
                    onDismiss.invoke()
                }
            })
            .show()

    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                GGBContract.ARTICLE -> {
                    data as ArticleContentBean
                    initArticle(data)
                    // 设置 md
                    val markwon = MarkwonUtils.basicMarkwon(this)
                    val markwonAdapter = MarkwonUtils.basicAdapter()
                    rcy_article_markdown.apply {
                        layoutManager = LinearLayoutManager(this@ArticleActivity)
                        adapter = markwonAdapter
                    }
                    markwonAdapter.setMarkdown(markwon,data.contentMd)
                    markwonAdapter.notifyDataSetChanged()
                }
                GGBContract.LIKEORCANCEL -> {
                    RxToast.info(this@ArticleActivity,"操作成功！", Toast.LENGTH_SHORT, true)?.show()
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