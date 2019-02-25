package com.intelligence.kotlindpwork.base

import butterknife.ButterKnife
import com.intelligence.dpwork.base.BaseScreen

/**
 * Class -
 *
 * Created by Deepblue on 2019/2/25 0025.
 */
abstract class TBaseScreen: BaseScreen() {

    override fun initView() {
        // 默认使用 ButterKnife
        ButterKnife.bind(this, superView)
        init()
    }

    abstract fun init()
}