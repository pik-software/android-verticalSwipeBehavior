package io.apiqa.android.verticalswipe

import android.view.View

/**
 * Изменение свойств view в зависимости от прогресса перемещения.
 * @see VerticalClamp
 */
interface SideEffect {

    fun onViewCaptured(child: View)

    /**
     * Назначает свойства [child] в зависимости от [factor]
     * @param child объект перемещения
     * @param factor прогресс перемещения
     */
    fun apply(child: View, factor: Float)
}