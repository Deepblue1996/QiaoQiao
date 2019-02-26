package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.intelligence.dpwork.adapter.DpAdapter
import com.intelligence.dpwork.annotation.DpLayout
import com.intelligence.dpwork.util.DisplayUtil
import com.intelligence.dpwork.util.ImageUtil
import com.intelligence.dpwork.weight.DpRecyclerView
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TBaseScreen
import com.intelligence.kotlindpwork.core.CoreApp
import com.intelligence.kotlindpwork.net.bean.Categories
import com.intelligence.kotlindpwork.net.bean.CategoryPicture
import com.intelligence.kotlindpwork.net.bean.DataExt
import com.prohua.dove.Dove
import com.prohua.dove.Dover
import io.reactivex.disposables.Disposable

/**
 * Class -
 *
 * Created by Deepblue on 2019/2/25 0025.
 */

@DpLayout(R.layout.activity_main)
class FirstScreen : TBaseScreen() {

    @BindView(R.id.drawerLayout)
    lateinit var drawerLayout: DrawerLayout
    @BindView(R.id.navView)
    lateinit var navView: NavigationView
    @BindView(R.id.seeImg)
    lateinit var seeImg: ImageView
    @BindView(R.id.recyclerView)
    lateinit var recyclerView: DpRecyclerView
    @BindView(R.id.drawerLayoutRecyclerView)
    lateinit var drawerLayoutRecyclerView: DpRecyclerView

    private var categoriesList: MutableList<Categories> = ArrayList()
    private var categoryPictureList: MutableList<CategoryPicture> = ArrayList()

    private var pageIndex: Int = 1
    private var cid: Int = 6

    @SuppressLint("RtlHardcoded")
    override fun init() {

        drawerLayoutRecyclerView.layoutManager = LinearLayoutManager(context)

        drawerLayoutRecyclerView.adapter = DpAdapter
            .newLine(context, categoriesList, com.intelligence.kotlindpwork.R.layout.category_item_layout, 0, 0)
            .itemView { p0, p1 ->
                p0.setText(R.id.textName, categoriesList[p1].name)
            }

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = DpAdapter
            .newLine(context, categoryPictureList, com.intelligence.kotlindpwork.R.layout.category_img_item_layout, 0, 0)
            .itemView { p0, p1 ->
                val img: ImageView = p0.vbi(R.id.imgId) as ImageView
                when {
                    p1 % 4 == 1 -> img.layoutParams.height = DisplayUtil.dip2px(context, 220F)
                    p1 % 4 == 2 -> img.layoutParams.height = DisplayUtil.dip2px(context, 280F)
                    p1 % 4 == 3 -> img.layoutParams.height = DisplayUtil.dip2px(context, 320F)
                    else -> img.layoutParams.height = DisplayUtil.dip2px(context, 250F)
                }
                ImageUtil.show(context, categoryPictureList[p1].url, img)
            }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isSlideToBottom(recyclerView)) {
                    pageIndex+=20
                    loadPictureNet()
                }
            }

        })

        seeImg.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        drawerLayout.openDrawer(Gravity.LEFT)

        loadPictureCategory()

        loadPictureNet()
    }

    /**
     * 加载类别
     */
    private fun loadPictureCategory() {

        Dove.flyLife(_dpActivity, CoreApp.jobTask!!.getPictureCategory(),
            object : Dover<DataExt<Categories>>() {
                override fun die(p0: Disposable?, p1: Throwable) {

                }

                override fun don(p0: Disposable?, p1: DataExt<Categories>) {
                    categoriesList.addAll(p1.data!!)
                    drawerLayoutRecyclerView.adapter!!.notifyDataSetChanged()
                }

            })
    }

    /**
     * 加载类别图片
     */
    private fun loadPictureNet() {

        Dove.flyLife(_dpActivity,
            CoreApp.jobTask!!.getPictureByCategory("WallPaperAndroid", "getAppsByCategory", cid, pageIndex, 20),
            object : Dover<DataExt<CategoryPicture>>() {
                override fun die(p0: Disposable?, p1: Throwable) {

                }

                override fun don(p0: Disposable?, p1: DataExt<CategoryPicture>) {
                    categoryPictureList.addAll(p1.data!!)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

            })
    }

    /**
     * 判断是否到底部
     */
    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }

    override fun statusBarBlackFont(): Boolean {
        return false
    }
}