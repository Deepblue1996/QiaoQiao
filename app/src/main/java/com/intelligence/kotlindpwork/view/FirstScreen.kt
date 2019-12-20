package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import com.deep.dpwork.adapter.DpAdapter
import com.deep.dpwork.annotation.DpLayout
import com.deep.dpwork.annotation.DpMainScreen
import com.deep.dpwork.dialog.DpDialogScreen
import com.deep.dpwork.util.DisplayUtil
import com.deep.dpwork.util.ToastUtil
import com.deep.dpwork.weight.DpRecyclerView
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
@DpMainScreen
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

    @BindView(R.id.topBar)
    lateinit var topBar: RelativeLayout

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

    override fun onBack() {
        DpDialogScreen.createBlur()
            .setMsg("确定退出瞧瞧?")
            .addButton(context, "确定") { p0 ->
                p0!!.close()
                _dpActivity.finish()
            }.addButton(
                context, "点错了"
            ) { p0 -> p0!!.close() }.open(fragmentManager())
    }

    /**
     * 初始化菜单列表
     */
    private fun initMenuList() {

        drawerLayoutRecyclerView.layoutManager = LinearLayoutManager(context)

        drawerLayoutRecyclerView.adapter = DpAdapter
            .newLine(context, categoriesList, R.layout.category_item_layout, 0, 0)
            .itemView { p0, p1 ->
                if (p1 == cid) {
                    p0.vbi(R.id.backBg).setBackgroundColor(Color.parseColor("#9999ff"))
                } else {
                    p0.vbi(R.id.backBg).setBackgroundColor(Color.parseColor("#353A3E"))
                }
                p0.setText(R.id.textName, categoriesList[p1].name)
            }
            .itemClick { _, i ->
                cid = i
                drawerLayoutRecyclerView.adapter!!.notifyDataSetChanged()
                refreshLayout.autoRefresh()
                drawerLayout.closeDrawers()
            }
    }

    /**
     * 初始化首页列表
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initHomeList() {

        val layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            3,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
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
                img.setOnTouchListener { _, event ->
                    when {
                        MotionEvent.ACTION_DOWN == event.action -> img.alpha = 0.5f
                        MotionEvent.ACTION_UP == event.action -> {
                            img.alpha = 1.0f
                            PictureScreen.newInstance(categoryPictureList[p1])
                                .open(fragmentManager())
                        }
                        else -> img.alpha = 1.0f
                    }
                    return@setOnTouchListener true
                }
                Gen.show(context, categoryPictureList[p1].url, img)
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
            categoryPictureList.clear()
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

        Dove.flyLife(CoreApp.jobTask.getPictureCategory(),
            object : Dover<DataExt<Categories>>() {
                override fun die(p0: Disposable?, p1: Throwable) {

                }

                override fun don(p0: Disposable?, p1: DataExt<Categories>) {
                    cid = 0
                    categoriesList.addAll(p1.data!!)
                    drawerLayoutRecyclerView.adapter!!.notifyDataSetChanged()
                }

            })
    }

    /**
     * 加载类别图片
     */
    private fun loadPictureNet() {

        var cidStr = "1"

        if (categoriesList.size != 0) {
            cidStr = categoriesList[cid].id!!
        }

        Dove.flyLife(CoreApp.jobTask.getPictureByCategory(
                "WallPaperAndroid", "getAppsByCategory",
                cidStr, pageIndex, 20
            ),
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