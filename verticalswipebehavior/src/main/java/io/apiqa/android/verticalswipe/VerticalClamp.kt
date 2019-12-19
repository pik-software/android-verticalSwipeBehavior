package io.apiqa.android.verticalswipe

/**
 * Задает ограничения на перемещение view по вертикали
 */
interface VerticalClamp {

    fun onViewCaptured(top: Int)

    /**
     * Ограничивает максимальную и/или минимальную позицию для view
     * @param height высота view
     * @param top положение view
     * @param dy последнее смещение view
     * @return максимальное или минимально допостимое положение view [android.view.View.getTop]
     */
    fun constraint(height: Int, top: Int, dy: Int): Int

    /**
     * @param distance пройденная дистанция
     * @param top положение view
     * @param height высота view
     * @param dy последнее смещение view
     * @return прогресс перемещения вниз
     */
    fun downCast(distance: Int, top: Int, height: Int, dy: Int): Float

    /**
     * @param distance пройденная дистанция
     * @param top положение view
     * @param height высота view
     * @param dy последнее смещение view
     * @return прогресс перемещения вверх
     */
    fun upCast(distance: Int, top: Int, height: Int, dy: Int): Float
}