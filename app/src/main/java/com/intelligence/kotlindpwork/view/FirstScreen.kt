package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.widget.TextView
import butterknife.BindView
import com.intelligence.dpwork.annotation.DpLayout
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TBaseScreen

/**
 * Class -
 *
 * Created by Deepblue on 2019/2/25 0025.
 */

@DpLayout(R.layout.activity_main)
class FirstScreen : TBaseScreen() {

    @BindView(R.id.titleText)
    lateinit var titleText: TextView

    @SuppressLint("SetTextI18n")
    override fun init() {
        titleText.text = "Hi DpWork!"
    }

    override fun statusBarBlackFont(): Boolean {
        return true
    }
}