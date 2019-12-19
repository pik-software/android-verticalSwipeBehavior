package io.apiqa.android.verticalswipe

import android.view.View
import androidx.customview.widget.ViewDragHelper

/**
 * Отвечает за изменение положения view после завершения жеста
 */
interface PostAction {

    fun onViewCaptured(child: View)

    /**
     * Вызывается, когда view освобождается ниже своего начального положения
     * @param helper "посетитель" для вызова анимации
     * @param diff пройденное расстояние, с которого пользователь отпустил view
     * @param child целевая view
     * @return была-ли инициирована анимация перемещения
     */
    fun releasedBelow(helper: ViewDragHelper, diff: Int, child: View): Boolean

    /**
     * Вызывается, когда view освобождается выше своего начального положения
     * @param helper "посетитель" для вызова анимации
     * @param diff пройденное расстояние, с которого пользователь отпустил view
     * @param child целевая view
     * @return была-ли инициирована анимация перемещения
     */
    fun releasedAbove(helper: ViewDragHelper, diff: Int, child: View): Boolean
}