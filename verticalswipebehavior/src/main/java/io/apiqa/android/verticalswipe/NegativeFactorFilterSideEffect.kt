package io.apiqa.android.verticalswipe

import android.view.View
import kotlin.math.abs

/**
 * Применяет переданный делегат [delegate] только если view перемещается вверх
 */
@Suppress("unused")
class NegativeFactorFilterSideEffect(private val delegate: SideEffect): SideEffect by delegate {

    override fun apply(child: View, factor: Float) {
        if (factor < 0) {
            delegate.apply(child, abs(factor))
        }
    }
}