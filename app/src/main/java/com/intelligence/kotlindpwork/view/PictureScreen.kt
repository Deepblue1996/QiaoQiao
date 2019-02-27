package com.intelligence.kotlindpwork.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.BindView
import com.bumptech.glide.Glide
import com.intelligence.dpwork.annotation.DpLayout
import com.intelligence.dpwork.util.ToastUtil
import com.intelligence.dpwork.util.TouchUtil
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TBaseScreen
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
@DpLayout(R.layout.picture_screen_layout)
class PictureScreen : TBaseScreen() {

    @BindView(R.id.imgBgId)
    lateinit var imgBgId: ImageView
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

    private lateinit var categoryPicture: CategoryPicture

    companion object {
        fun newInstance(categoryPicture: CategoryPicture): PictureScreen {
            val pictureScreen = PictureScreen()
            pictureScreen.categoryPicture = categoryPicture
            return pictureScreen
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        Gen.showBlackBg(_dpActivity, categoryPicture.url, imgBgId)
        Gen.showFitBlackBg(context, categoryPicture.url, imgView)

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
                        Thread {
                            val path = downLoadImage(context, categoryPicture.url)
                            val mediaScanner = MediaScanner(_dpActivity)
                            mediaScanner.scan(path)
                            runUi {
                                ToastUtil.show("已保存至本地-$path")
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

    private fun downLoadImage(context: Context?, url: String?): String {
        val pictureFolder = Environment.getExternalStorageDirectory()
        val appDir = File(pictureFolder, "QiaoQiao")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val fileName = "" + System.currentTimeMillis() + ".jpg"
        val destFile = File(appDir, fileName)

        val sourceFile = Glide.with(context!!).asFile().load(url).submit().get()
        copy(sourceFile, destFile)
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