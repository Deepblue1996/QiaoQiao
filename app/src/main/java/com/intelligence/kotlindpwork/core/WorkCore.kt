package com.intelligence.kotlindpwork.core

import com.intelligence.dpwork.DpWorkCore
import com.intelligence.dpwork.annotation.DpInit
import com.intelligence.kotlindpwork.view.FirstScreen

@DpInit(FirstScreen::class)
class WorkCore : DpWorkCore() {
    override fun initCore() {
    }
}
