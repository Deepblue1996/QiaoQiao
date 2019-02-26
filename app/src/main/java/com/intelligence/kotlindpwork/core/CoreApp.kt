package com.intelligence.kotlindpwork.core

import com.intelligence.dpwork.DpWorkApplication
import com.intelligence.dpwork.annotation.DpBugly
import com.intelligence.kotlindpwork.net.JobTask
import com.prohua.dove.Dove
import com.prohua.dove.base.Nest


/**
 * Class -
 *
 * Created by Deepblue on 2019/2/25 0025.
 */

@DpBugly("123456")
class CoreApp : DpWorkApplication() {

    // Dove Task.
    companion object {
        var jobTask: JobTask? = null
    }

    override fun initApplication() {

        // 初始化网络层
        val nest = Nest.build()
            .setBaseUrl("http://wallpaper.apc.360.cn/")
            .setInterfaceClass(JobTask::class.java)

        jobTask = Dove.birth<Any>(baseContext, nest) as JobTask?
    }
}
