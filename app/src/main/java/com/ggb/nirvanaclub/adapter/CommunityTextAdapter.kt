package com.ggb.nirvanaclub.adapter

import android.Manifest
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.bean.DevelopJokesListBean
import com.ggb.nirvanaclub.utils.EncryptionUtils
import com.ggb.nirvanaclub.utils.ImageLoaderUtil
import com.ggb.nirvanaclub.utils.RegularUtil
import com.tamsiree.rxui.view.likeview.RxShineButton
import java.io.File

/**
 * 纯文字和图片通用一个adapter
 */
class CommunityTextAdapter : BaseQuickAdapter<DevelopJokesListBean, BaseViewHolder>(R.layout.item_community_text),
    BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener {

    private var mCurrentClickNpl: BGANinePhotoLayout? = null

    override fun convert(helper: BaseViewHolder?, item: DevelopJokesListBean) {
        ImageLoaderUtil().displayHeadImage(mContext,item.user.avatar,helper?.getView(R.id.rci_community_author))
        helper?.setText(R.id.tv_community_authorName,item.user.nickName)
        helper?.setText(R.id.tv_community_authorSign,item.user.signature)
        helper?.setText(R.id.tv_community_content,item.joke.content)

        helper?.setText(R.id.tv_community_likeCount,item.info.likeNum.toString())
        helper?.setText(R.id.tv_community_dislikeCount,item.info.disLikeNum.toString())
        helper?.setText(R.id.tv_community_commentCount,item.info.commentNum.toString())
        helper?.setText(R.id.tv_community_shareCount,item.info.shareNum.toString())

        helper?.getView<RxShineButton>(R.id.rsb_community_like)?.setChecked(item.info.isLike)
        helper?.getView<RxShineButton>(R.id.rsb_community_dislike)?.setChecked(item.info.isUnlike)

        if (item.joke.type<2){
            helper?.getView<RelativeLayout>(R.id.rl_community_picture_content)?.visibility = View.GONE
        }else{
            //处理多图数据
            val formulaStr = item.joke.imageUrl.split(",")
            val decodeString = arrayListOf<String>()
            formulaStr.forEach {
                decodeString.add(EncryptionUtils.decrypt(
                    "cretinzp**273846", RegularUtil.truncateHeadString(it,6)
                ))
            }

            helper?.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)?.setDelegate(this)
            helper?.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)?.data = decodeString
        }

    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    private fun photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return
        }
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val downloadDir =
            File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload")
        val photoPreviewIntentBuilder = BGAPhotoPreviewActivity.IntentBuilder(mContext)
        if (true) {
            // 保存图片的目录，如果传 null，则没有保存图片功能
            photoPreviewIntentBuilder.saveImgDir(downloadDir)
        }
        if (mCurrentClickNpl!!.itemCount == 1) {
            // 预览单张图片
            photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl!!.currentClickItem)
        } else if (mCurrentClickNpl!!.itemCount > 1) {
            // 预览多张图片
            photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl!!.data)
                .currentPosition(mCurrentClickNpl!!.currentClickItemPosition) // 当前预览图片的索引
        }
        mContext.startActivity(photoPreviewIntentBuilder.build())
    }

    override fun onClickNinePhotoItem(
        ninePhotoLayout: BGANinePhotoLayout,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        mCurrentClickNpl = ninePhotoLayout
        photoPreviewWrapper()
    }

    override fun onClickExpand(
        ninePhotoLayout: BGANinePhotoLayout,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        ninePhotoLayout.setIsExpand(true)
        ninePhotoLayout.flushItems()
    }

    override fun onRVItemClick(parent: ViewGroup?, itemView: View?, position: Int) {
        TODO("Not yet implemented")
    }
}