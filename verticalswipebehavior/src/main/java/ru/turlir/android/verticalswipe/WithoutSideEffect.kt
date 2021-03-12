package ru.turlir.android.verticalswipe

import android.view.View

/**
 * Does not change any properties of view
 */
@Suppress("unused")
class WithoutSideEffect: SideEffect {

    override fun onViewCaptured(child: View) {
        // ignore
    }

    override fun apply(child: View, factor: Float) {
        // ignore
    }
}