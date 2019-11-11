package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.LinearLayout
import butterknife.BindView
import com.bumptech.glide.Glide
import com.deep.dpwork.annotation.DpBlur
import com.deep.dpwork.annotation.DpLayout
import com.deep.dpwork.util.ToastUtil
import com.deep.dpwork.util.TouchUtil
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TDialogScreen
import com.intelligence.kotlindpwork.net.bean.CategoryPicture
import com.intelligence.kotlindpwork.util.Gen
import com.intelligence.kotlindpwork.weight.TouchImageView
import com.yanzhenjie.mediascanner.MediaScanner
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Class - 图片浏览
 *
 * Created by Deepblue on 2019/2/27 0027.
 */
@DpBlur
@DpLayout(R.layout.picture_screen_layout)
class PictureScreen : TDialogScreen() {

    @BindView(R.id.imgView)
    lateinit var imgView: TouchImageView
    @BindView(R.id.closeTouch)
    lateinit var closeTouch: LinearLayout
    @BindView(R.id.positionTouch)
    lateinit var positionTouch: LinearLayout
    @BindView(R.id.scaleTouch)
    lateinit var scaleTouch: LinearLayout
    @BindView(R.id.scrTouch)
    lateinit var scrTouch: LinearLayout
    @BindView(R.id.downTouch)
    lateinit var downTouch: LinearLayout
    @BindView(R.id.biZhiTouch)
    lateinit var biZhiTouch: LinearLayout

    // WallpaperManager类：系统壁纸管理。通过它可以获得当前壁纸以及设置指定图片作为系统壁纸。
    private lateinit var wallpaperManager: WallpaperManager

    private lateinit var categoryPicture: CategoryPicture

    private var biZhiPath: String? = null

    companion object {
        fun newInstance(categoryPicture: CategoryPicture): PictureScreen {
            val pictureScreen = PictureScreen()
            pictureScreen.categoryPicture = categoryPicture
            return pictureScreen
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {

        Gen.showFitBlackBg(context, categoryPicture.url, imgView)

        // 初始化WallpaperManager
        wallpaperManager = WallpaperManager.getInstance(activity)

        imgView.setOneTouch {
            close()
        }

        scrTouch.isLongClickable = true
        scrTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {
                        imgView.scaleImage(0.8f, true)
                        imgView.rotateImage(0f - imgView.rotateDegree, true)
                    }
                })
        }

        scaleTouch.isLongClickable = true
        scaleTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {
                        imgView.scaleImage(0f, true)
                        imgView.rotateImage(0f - imgView.rotateDegree, true)
                    }
                })
        }

        positionTouch.isLongClickable = true
        positionTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {
                        imgView.rotateImage(0f - imgView.rotateDegree, true)
                    }
                })
        }

        downTouch.isLongClickable = true
        downTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {
                        ToastUtil.show("下载壁纸中，请稍等")
                        Thread {
                            val path = downLoadImage(context, categoryPicture.url)
                            val mediaScanner = MediaScanner(activity)
                            biZhiPath = path
                            mediaScanner.scan(path)
                            runUi {
                                ToastUtil.show("已保存至本地-$path")
                            }
                        }.start()

                    }
                })
        }

        biZhiTouch.isLongClickable = true
        biZhiTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {

                        if (biZhiPath == null) {
                            ToastUtil.show("请先下载至本地")
                            return
                        }
                        ToastUtil.show("设置壁纸中，请稍等")

                        Thread {
                            try {
                                // 设置壁纸
                                wallpaperManager.setBitmap(BitmapFactory.decodeFile(biZhiPath, getBitmapOption()))
                                runUi {
                                    ToastUtil.show("设置壁纸成功")
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                                runUi {
                                    ToastUtil.show("设置壁纸失败")
                                }
                            }
                        }.start()

                    }
                })
        }

        closeTouch.isLongClickable = true
        closeTouch.setOnTouchListener { v, event ->
            return@setOnTouchListener TouchUtil.newInstance(R.drawable.shape_corner_touch)
                .get(v!!, event!!, object : TouchUtil.OnUp {
                    override fun no() {

                    }

                    override fun get() {
                        close()
                    }
                })
        }

    }

    private fun getBitmapOption(): BitmapFactory.Options {
        System.gc()
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inSampleSize = 1
        return options
    }

    private fun downLoadImage(context: Context?, url: String?): String {
        val pictureFolder = Environment.getExternalStorageDirectory()
        val appDir = File(pictureFolder, "QiaoQiao")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val fileName = "" + categoryPicture.pid + ".jpg"
        val destFile = File(appDir, fileName)

        if (!destFile.exists()) {
            val sourceFile = Glide.with(context!!).asFile().load(url).submit().get()
            copy(sourceFile, destFile)
        }
        return destFile.absolutePath
    }

    private fun copy(source: File, target: File) {
        val fileInputStream = FileInputStream(source)
        val fileOutputStream = FileOutputStream(target)
        try {
            val buffer = ByteArray(1024)
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream.close()
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}