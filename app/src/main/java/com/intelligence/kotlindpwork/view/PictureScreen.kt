package com.intelligence.kotlindpwork.view

import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.BindView
import com.intelligence.dpwork.annotation.DpLayout
import com.intelligence.kotlindpwork.R
import com.intelligence.kotlindpwork.base.TBaseScreen
import com.intelligence.kotlindpwork.net.bean.CategoryPicture
import com.intelligence.kotlindpwork.util.Gen
import com.intelligence.kotlindpwork.weight.TouchImageView

/**
 * Class -
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

    private lateinit var categoryPicture: CategoryPicture

    companion object {
        fun newInstance(categoryPicture: CategoryPicture): PictureScreen {
            val pictureScreen = PictureScreen()
            pictureScreen.categoryPicture = categoryPicture
            return pictureScreen
        }
    }

    override fun init() {
        Gen.showBlackBg(_dpActivity, categoryPicture.url, imgBgId)
        Gen.showFitBlackBg(context, categoryPicture.url, imgView)

        closeTouch.setOnClickListener {
            close()
        }
    }

}