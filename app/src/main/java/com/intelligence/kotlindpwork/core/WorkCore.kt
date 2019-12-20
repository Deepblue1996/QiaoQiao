package com.intelligence.kotlindpwork.core

import android.Manifest
import com.deep.dpwork.DpWorkCore
import com.deep.dpwork.annotation.DpPermission
import com.deep.dpwork.util.ToastUtil

@DpPermission(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.SET_WALLPAPER
)
class WorkCore : DpWorkCore() {

    override fun permissionComplete(b: Boolean) {
        if (!b) ToastUtil.showError("申请权限失败,应用程序无法正常运行！")
    }

}
