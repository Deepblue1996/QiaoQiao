package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.widget.ImageView
import butterknife.BindView
import com.intelligence.dpwork.adapter.DpAdapter
import com.intelligence.dpwork.annotation.DpLayout
import com.intelligence.dpwork.util.DisplayUtil
import com.intelligence.dpwork.util.ToastUtil
import com.intelligence.dpwork.weight.DpRecyclerView
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TBaseScreen
import com.intelligence.kotlindpwork.core.CoreApp
import com.intelligence.kotlindpwork.net.bean.Categories
import com.intelligence.kotlindpwork.net.bean.CategoryPicture
import com.intelligence.kotlindpwork.net.bean.DataExt
import com.intelligence.kotlindpwork.util.Gen
import com.prohua.dove.Dove
import com.prohua.dove.Dover
import com.scwang.smartrefresh.layout.api.RefreshLayout
import io.reactivex.disposables.Disposable

/**
 * Class - 首页
 *
 * Created by Deepblue on 2019/2/25 0025.
 */

@DpLayout(R.layout.activity_main)
class FirstScreen : TBaseScreen() {

    @BindView(R.id.drawerLayout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.menuImg)
    lateinit var menuImg: ImageView

    @BindView(R.id.seeImg)
    lateinit var seeImg: ImageView

    @BindView(R.id.refreshLayout)
    lateinit var refreshLayout: RefreshLayout

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: DpRecyclerView

    @BindView(R.id.drawerLayoutRecyclerView)
    lateinit var drawerLayoutRecyclerView: DpRecyclerView

    /**
     * 分类数据列表
     */
    private var categoriesList: MutableList<Categories> = ArrayList()

    /**
     * 图片列表数据
     */
    private var categoryPictureList: MutableList<CategoryPicture> = ArrayList()

    /**
     * 列表状态
     * 0: 默认
     * -1: 刷新
     * 1: 加载更多
     */
    private var homeListState: Int = 0

    /**
     * 当前分页
     */
    private var pageIndex: Int = 1

    /**
     * 目前选择的分类
     */
    private var cid: Int = 1

    override fun init() {

        // 初始化菜单列表
        initMenuList()

        // 初始化首页列表
        initHomeList()

        // 初始化首页其他配置
        initHome()

        // 加载分类
        loadPictureCategory()

        // 加载首页默认分类
        loadPictureNet()
    }

    /**
     * 初始化菜单列表
     */
    private fun initMenuList() {

        drawerLayoutRecyclerView.layoutManager = LinearLayoutManager(context)

        drawerLayoutRecyclerView.adapter = DpAdapter
            .newLine(context, categoriesList, R.layout.category_item_layout, 0, 0)
            .itemView { p0, p1 ->
                p0.setText(R.id.textName, categoriesList[p1].name)
            }
    }

    /**
     * 初始化首页列表
     */
    private fun initHomeList() {

        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = DpAdapter
            .newLine(context, categoryPictureList, R.layout.category_img_item_layout, 0, 0)
            .itemView { p0, p1 ->
                val img: ImageView = p0.vbi(R.id.imgId) as ImageView
                when {
                    p1 % 4 == 1 -> img.layoutParams.height = DisplayUtil.dip2px(context, 170F)
                    p1 % 4 == 2 -> img.layoutParams.height = DisplayUtil.dip2px(context, 230F)
                    p1 % 4 == 3 -> img.layoutParams.height = DisplayUtil.dip2px(context, 270F)
                    else -> img.layoutParams.height = DisplayUtil.dip2px(context, 200F)
                }
                Gen.show(context, categoryPictureList[p1].url, img)
            }.itemClick { _, i ->
                open(PictureScreen.newInstance(categoryPictureList[i]))
            }

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (isSlideToBottom(recyclerView)) {
//                    pageIndex+=20
//                    loadPictureNet()
//                }
//            }
//
//        })

        refreshLayout.isEnableRefresh = true

        refreshLayout.isEnableLoadMore = true

        refreshLayout.setOnRefreshListener {
            homeListState = -1
            pageIndex = 1
            loadPictureNet()
        }

        refreshLayout.setOnLoadMoreListener {
            homeListState = 1
            pageIndex += 20
            loadPictureNet()
        }
    }

    /**
     * 初始化首页其他配置
     */
    @SuppressLint("RtlHardcoded")
    private fun initHome() {

        menuImg.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        seeImg.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
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
                    finishRefreshLoadMore()
                    ToastUtil.show("Error:" + p1.message)
                }

                override fun don(p0: Disposable?, p1: DataExt<CategoryPicture>) {
                    categoryPictureList.addAll(p1.data!!)
                    recyclerView.adapter!!.notifyDataSetChanged()
                    finishRefreshLoadMore()
                }
            })
    }

    /**
     * 完成刷新
     */
    private fun finishRefreshLoadMore() {
        if (homeListState == -1) {
            refreshLayout.finishRefresh(0)
        } else {
            refreshLayout.finishLoadMore(0)
        }
        homeListState = 0
    }

//    /**
//     * 判断是否到底部
//     */
//    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
//        if (recyclerView == null) return false
//        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
//    }

}