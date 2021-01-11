package com.intelligence.kotlindpwork.core

import android.Manifest
import com.deep.dpwork.annotation.DpMainScreenKt
import com.deep.dpwork.annotation.DpPermission
import com.deep.dpwork.core.kotlin.DpInitCoreKt
import com.deep.dpwork.util.ToastUtil
import com.intelligence.kotlindpwork.view.FirstScreen

@DpPermission(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.SET_WALLPAPER
)
@DpMainScreenKt(FirstScreen::class)
class WorkCore : DpInitCoreKt() {

    override fun permissionOnGranted(permissions: MutableList<String>?, all: Boolean) {
        if (!all) ToastUtil.showError("申请权限失败,应用程序无法正常运行！")
    }

}
