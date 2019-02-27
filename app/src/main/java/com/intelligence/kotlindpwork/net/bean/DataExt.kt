package com.intelligence.kotlindpwork.net.bean

import java.io.Serializable

/**
 * Class -
 *
 *
 * Created by Deepblue on 2019/2/26 0026.
 */
class DataExt<T>: Serializable {

    /**
     * errno : 0
     * errmsg : 正常
     * consume : 10
     * total : 16
     * data : []
     */

    var errno: String? = null
    var errmsg: String? = null
    var consume: String? = null
    var total: String? = null
    var data: List<T>? = null
}
