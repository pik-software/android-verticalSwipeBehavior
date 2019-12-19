package io.apiqa.android.verticalswipe

import android.view.View

/**
 * Не изменяет никакие свойства view
 */
class WithoutSideEffect: SideEffect {

    override fun onViewCaptured(child: View) {
        // ignore
    }

    override fun apply(child: View, factor: Float) {
        // ignore
    }
}