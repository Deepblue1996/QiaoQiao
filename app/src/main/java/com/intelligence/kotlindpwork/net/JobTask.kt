package com.intelligence.kotlindpwork.net

import com.intelligence.kotlindpwork.net.bean.Categories
import com.intelligence.kotlindpwork.net.bean.CategoryPicture
import com.intelligence.kotlindpwork.net.bean.DataExt
import com.intelligence.kotlindpwork.net.bean.SearchPicture
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Class - Api接口
 *
 *
 * Created by Deepblue on 2019/2/26 0026.
 */
interface JobTask {

    /**
     * 获取壁纸类别
     *
     * @return
     */
    @GET("index.php")
    fun getPictureCategory(
        @Query("c") c: String = "WallPaperAndroid",
        @Query("a") a: String = "getAllCategories"
    ): Observable<DataExt<Categories>>

    /**
     * 获取指定类别的壁纸
     *
     * @return
     */
    @GET("index.php")
    fun getPictureByCategory(
        @Query("c") c: String = "WallPaperAndroid",
        @Query("a") a: String = "getAppsByCategory",
        @Query("cid") cid: String = "1",
        @Query("start") start: Int = 1,
        @Query("count") count: Int = 20
    ): Observable<DataExt<CategoryPicture>>

    /**
     * 按关键字搜索指定壁纸
     *
     * @return
     */
    @GET("index.php")
    fun getPictureBySearch(
        @Query("c") c: String = "WallPaperAndroid",
        @Query("a") a: String = "search",
        @Query("kw") kw: String = "",
        @Query("start") start: Int = 1,
        @Query("count") count: Int = 20
    ): Observable<DataExt<SearchPicture>>

}
