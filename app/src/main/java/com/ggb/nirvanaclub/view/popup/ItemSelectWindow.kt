package com.ggb.nirvanaclub.view.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ItemSelectAdapter
import com.ggb.nirvanaclub.bean.DataConfigBean
import com.ggb.nirvanaclub.utils.MdDensityUtils


class ItemSelectWindow : PopupWindow {

    private var mContext: Context? = null

    private val mAdapter: ItemSelectAdapter
    private val rcy:RecyclerView

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val contentView = View.inflate(context, R.layout.popup_item_select_view,null)
        setContentView(contentView)
        mContext = context

        rcy = contentView.findViewById(R.id.rcy_select_item)
        rcy.layoutManager = LinearLayoutManager(mContext)
        mAdapter = ItemSelectAdapter()
        rcy.adapter = mAdapter

        initEvent()
    }

    private fun initWindow(width:Int) {
        this.width = width
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        this.update()
        //实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(Color.TRANSPARENT)
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw)
    }

    private fun initRecycleHeight(){
        if(mAdapter.data.size>4){
            val params = rcy.layoutParams as LinearLayout.LayoutParams
            params.height = MdDensityUtils.dp2px(mContext,150)
            rcy.layoutParams = params
        }
    }

    private fun initEvent(){
        mAdapter.setOnItemClickListener { adapter, view, position ->
            mOnItemSelectListener?.onSelect(mAdapter.data[position])
            dismiss()
        }
    }

    private fun showAtBottom(view: View) {
        initWindow(view.width)
        showAsDropDown(view, 0, 0)
    }

    fun showItemList(view: View,checkList:List<DataConfigBean>){
        mAdapter.setNewData(checkList)
        showAtBottom(view)
        initRecycleHeight()
    }

    interface OnItemSelectListener{
        fun onSelect(config: DataConfigBean)
    }

    private var mOnItemSelectListener:OnItemSelectListener? = null

    fun setOnItemSelectListener(mOnItemSelectListener:OnItemSelectListener){
        this.mOnItemSelectListener = mOnItemSelectListener
    }

}