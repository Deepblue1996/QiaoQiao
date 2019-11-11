package com.intelligence.kotlindpwork.base

import com.deep.dpwork.dialog.DialogScreen

import butterknife.ButterKnife

/**
 * Class - 弹窗基类
 *
 *
 * Created by Deepblue on 2019/9/29 0029.
 */
abstract class TDialogScreen : DialogScreen() {

    override fun initView() {
        // 默认使用 ButterKnife
        ButterKnife.bind(this, superView)
        init()
    }

    abstract fun init()
}
