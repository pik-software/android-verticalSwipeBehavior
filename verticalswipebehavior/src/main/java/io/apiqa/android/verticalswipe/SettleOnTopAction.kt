package io.apiqa.android.verticalswipe

import android.view.View
import androidx.customview.widget.ViewDragHelper

/**
 * При перемещении view вниз возвращает ее в начальную точку. При перемещении выше - уводит за экран
 */
@Suppress("unused")
class SettleOnTopAction: PostAction {

    private var originTop: Int = -1

    override fun onViewCaptured(child: View) {
        originTop = child.top
    }

    override fun releasedBelow(helper: ViewDragHelper, diff: Int, child: View): Boolean {
        return helper.settleCapturedViewAt(child.left, originTop)
    }

    override fun releasedAbove(helper: ViewDragHelper, diff: Int, child: View): Boolean {
        return helper.settleCapturedViewAt(child.left, -child.height)
    }
}