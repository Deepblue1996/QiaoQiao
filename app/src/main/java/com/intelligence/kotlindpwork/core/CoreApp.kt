package com.intelligence.kotlindpwork.core

import com.deep.dpwork.DpWorkApplication
import com.deep.dpwork.annotation.net.DoveInit
import com.intelligence.kotlindpwork.net.JobTask

/**
 * Class - 核心
 *
 * Created by Deepblue on 2019/2/25 0025.
 */
class CoreApp : DpWorkApplication() {

    // Dove Task.
    companion object {
        @DoveInit(
            url = "http://wallpaper.apc.360.cn/",
            interfaceClass = JobTask::class
        )
        lateinit var jobTask: JobTask
    }

    override fun initApplication() {
    }
}
