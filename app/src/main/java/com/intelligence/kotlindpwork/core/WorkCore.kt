package com.intelligence.kotlindpwork.core

import android.Manifest
import android.app.Activity
import android.widget.Toast
import com.intelligence.dpwork.DpWorkCore
import com.intelligence.dpwork.annotation.DpInit
import com.intelligence.kotlindpwork.view.FirstScreen
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


@DpInit(FirstScreen::class)
class WorkCore : DpWorkCore() {

    override fun initCore() {
        initPermissions(this)
    }

    /**
     * 申请权限
     *
     * @param activity
     */
    private fun initPermissions(activity: Activity) {
        val rxPermissions = RxPermissions(activity)
        // Must be done during an initialization phase like onCreate
        rxPermissions.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE
        )
            .subscribe(object : Observer<Boolean> {

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (!t) {
                        Toast.makeText(baseContext, "申请权限失败,应用程序无法正常运行！", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }
            })
    }
}
