package com.ggb.nirvanaclub.modules.user

import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.adapter.ShopCartAdapter
import com.ggb.nirvanaclub.base.BaseActivity
import com.ggb.nirvanaclub.bean.ShopCartBean
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.view.SwipeItemLayout
import com.ggb.nirvanaclub.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_shop_cart.*

import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.toast

class ShopCartActivity : BaseActivity() , GGBContract.View{

    private lateinit var present: GGBPresent

    private lateinit var reminderDialog: ReminderDialog

    private lateinit var emptyView : View
    private lateinit var errorView : View

    private lateinit var mAdapter: ShopCartAdapter

    private var isEdit = false
    private var isAllChecked = false

    override fun getTitleType() = PublicTitleData (C.TITLE_RIGHT_TEXT,"购物车","编辑",0,R.color.text_main_color)

    override fun getLayoutResource() = R.layout.activity_shop_cart

    override fun beForSetContentView() {
        present = GGBPresent(this)
    }

    override fun initView() {
        reminderDialog = ReminderDialog(this)

        mAdapter = ShopCartAdapter()
        rcy_shop_cart.layoutManager = LinearLayoutManager(this)
        rcy_shop_cart.adapter = mAdapter
        rcy_shop_cart.addOnItemTouchListener(object: SwipeItemLayout.OnSwipeItemTouchListener(this){})

        emptyView = View.inflate(this,R.layout.empty_public_view,null)
        errorView = View.inflate(this,R.layout.empty_public_network,null)
        mAdapter.isUseEmpty(false)

        getShopCartList()
    }

    override fun initEvent(){
        tv_public_right.setOnClickListener {
            isEdit = !isEdit
            if(isEdit){
                tv_public_right.text = "完成"
                tv_shop_confirm.text = "删除"
                tv_total_money.visibility = View.INVISIBLE
            }else{
                tv_public_right.text = "编辑"
                tv_shop_confirm.text = "结算"
                tv_total_money.visibility = View.VISIBLE
            }
        }
        ll_all_checked.setOnClickListener {
            if(mAdapter.data.isEmpty()){
                return@setOnClickListener
            }
            isAllChecked = !isAllChecked
            tb_all_checked.isChecked = isAllChecked
            mAdapter.data.forEach {
                it.isSelected = isAllChecked
            }
            mAdapter.notifyDataSetChanged()
            selectTotalPrice()
        }

        swipe_refresh_layout.setOnRefreshListener {
            getShopCartList()
        }

        tv_shop_confirm.setOnClickListener {
            if(isEdit){
                var isSelected = false
                mAdapter.data.forEach {
                    if(it.isSelected){
                        isSelected = true
                        return@forEach
                    }
                }
                if(isSelected){
                    reminderDialog.showReminder(3)
                }
            }
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.ll_shop_cart -> {

                }
                R.id.ll_shop_checked -> {
                    mAdapter.data[position].isSelected = !mAdapter.data[position].isSelected
                    for(i in mAdapter.data.indices){
                        if(mAdapter.data[i].isSelected != isAllChecked){
                            isAllChecked = !isAllChecked
                            break
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                    tb_all_checked.isChecked = isAllChecked
                    selectTotalPrice()
                }
                R.id.iv_shop_add -> {

                }
                R.id.iv_shop_sub -> {
                    if(mAdapter.data[position].goodsNumber>1){

                    }else{
                        toast("不能再减少了").setGravity(Gravity.CENTER, 0, 0)
                    }
                }
                R.id.tv_cart_delete -> {

                }
            }
        }
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                val ids = arrayListOf<String>()
                mAdapter.data.forEach {
                    if(it.isSelected){
                        ids.add(it.id)
                    }
                }

            }
        })


    }

    private fun getShopCartList(){
        val fakeData = arrayListOf(
            ShopCartBean("1","乖乖熊","1","1001","一只小熊",1,"https://nirvana1234.xyz/img/dota2(1).0f43f2ab.jpg",1.0,false),
            ShopCartBean("2","乖乖兔","2","1002","一只小兔",2,"https://nirvana1234.xyz/img/dota2(2).1f7de4fc.jpg",2.0,false),
            ShopCartBean("3","神经达","3","1003","一只小达",3,"https://nirvana1234.xyz/img/dota2(3).474a38ce.jpg",3.0,false)
        )

        mAdapter.setNewData(fakeData)
        swipe_refresh_layout.finishRefresh()
    }

    private fun selectTotalPrice(){
        var totalPrice = 0.0
        var isSelect = false
        mAdapter.data.forEach {
            if(it.isSelected){
                totalPrice+=(it.goodsNumber*it.price)
                isSelect = true
            }
        }
        tv_total_money.text = "合计：¥${String.format("%.2f", totalPrice)}"
        tv_shop_confirm.setBackgroundColor(resources.getColor(if(isSelect) R.color.main_color else R.color.main_color_1))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 4001){
            getShopCartList()
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {

            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        mAdapter.isUseEmpty(true)
        mAdapter.emptyView = emptyView
        swipe_refresh_layout.finishRefresh()
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            swipe_refresh_layout.finishRefresh()
            mAdapter.isUseEmpty(true)
            mAdapter.emptyView = errorView
        }else{
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}