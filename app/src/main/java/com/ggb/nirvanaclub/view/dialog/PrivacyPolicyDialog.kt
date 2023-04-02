package com.ggb.nirvanaclub.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.ggb.nirvanaclub.App
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.modules.community.CommunityWebContentActivity
import com.ggb.nirvanaclub.modules.message.MessageWebViewActivity
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import per.goweii.anylayer.Layer
import per.goweii.anylayer.dialog.DialogLayer

/**
 * @author HongWeijie
 * @date 2023/4/2
 */
class PrivacyPolicyDialog(context: Context) : DialogLayer(context) {

    public override fun onAttach() {
        super.onAttach()
        val content = requireView<TextView>(R.id.dialog_privacy_policy_tv_content)
        val text = activity.getString(R.string.privacy_policy_content)
        val link = activity.getString(R.string.privacy_policy_content_link)
        val start = text.indexOf(link)
        val end = start + link.length
        val spannableString = SpannableString(text)
        spannableString.setSpan(Clickable(View.OnClickListener {

            val i = Intent(activity, MessageWebViewActivity::class.java)
            i.putExtra("url", "file:///android_asset/user_protocol.html")
            i.putExtra("title", "隐私权限")
            activity.startActivity(i)

//            CommunityWebContentActivity.start(activity,
//                -2, "隐私权限", "file:///android_asset/user_protocol.html"
//            )

        }), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        content.text = SpannableStringBuilder().append(spannableString)
        content.movementMethod = LinkMovementMethod.getInstance()
    }

    private inner class Clickable(private val mListener: View.OnClickListener) :
        ClickableSpan(), View.OnClickListener {
        override fun onClick(v: View) {
            mListener.onClick(v)
        }

        @SuppressLint("ResourceAsColor")
        override fun updateDrawState(ds: TextPaint) {
            ds.color = R.color.main_color
            ds.isUnderlineText = false
        }
    }

    fun showIfFirst(context: Context) {
        if (!SharedPreferencesUtil.getUserBoolean(context, "KEY_POLICY_GUIDE")) {
            return
        }
        PrivacyPolicyDialog(context).show()
    }

    init {
        onDismissListener(object : OnDismissListener {
            override fun onDismissing(layer: Layer) {}
            override fun onDismissed(layer: Layer) {

            }
        })
        contentView(R.layout.dialog_privacy_policy)
        backgroundBlurRadius(4f)
        backgroundBlurSimple(20f)
        cancelableOnClickKeyBack(false)
        cancelableOnTouchOutside(false)
        onClickToDismiss({ layer, v ->
            SharedPreferencesUtil.putUserBoolean(context, "KEY_POLICY_GUIDE", false)
        }, R.id.dialog_privacy_policy_tv_yes)

        onClickToDismiss({ layer, v ->
            App.instance.exitApp()
        }, R.id.dialog_privacy_policy_tv_no)
    }
}