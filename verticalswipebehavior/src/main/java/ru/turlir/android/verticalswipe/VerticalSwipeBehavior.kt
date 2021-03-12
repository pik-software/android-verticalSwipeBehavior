package ru.turlir.android.verticalswipe

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.customview.widget.ViewDragHelper

class VerticalSwipeBehavior<V: View>: CoordinatorLayout.Behavior<V> {

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <V: View> from(v: V): VerticalSwipeBehavior<V> {
            val lp = v.layoutParams
            require(lp is CoordinatorLayout.LayoutParams)
            val behavior = lp.behavior
            requireNotNull(behavior)
            require(behavior is VerticalSwipeBehavior)
            return behavior as VerticalSwipeBehavior<V>
        }
    }

    var sideEffect: SideEffect = AlphaElevationSideEffect()
    var clamp: VerticalClamp = FractionClamp(1f, 1f)
    var settle: PostAction = OriginSettleAction()
    var listener: SwipeListener? = null

    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var dragHelper: ViewDragHelper? = null
    private var interceptingEvents = false

    private val callback = object: ViewDragHelper.Callback() {

        private val INVALID_POINTER_ID = -1
        private var currentPointer = INVALID_POINTER_ID
        private var originTop: Int = 0

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return currentPointer == INVALID_POINTER_ID || pointerId == currentPointer
        }

        override fun onViewCaptured(child: View, activePointerId: Int) {
            originTop = child.top
            currentPointer = activePointerId
            //
            sideEffect.onViewCaptured(child)
            settle.onViewCaptured(child)
            clamp.onViewCaptured(child.top)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return clamp.constraint(child.height, top, dy)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return child.left
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return child.height
        }

        override fun onViewReleased(child: View, xvel: Float, yvel: Float) {
            val diff = child.top - originTop
            val settled = dragHelper?.let {
                if (diff > 0) {
                    settle.releasedBelow(it, diff, child)
                } else {
                    settle.releasedAbove(it, diff, child)
                }
            } ?: false
            if (settled) {
                listener?.onPreSettled(diff)
                child.postOnAnimation(RecursiveSettle(child, diff))
            }
            currentPointer = INVALID_POINTER_ID
        }

        override fun onViewPositionChanged(child: View, left: Int, top: Int, dx: Int, dy: Int) {
            val factor = if (top < originTop) {
                val diff = originTop - top
                -clamp.upCast(diff, top, child.height, dy)
            } else {
                val diff = top - originTop
                clamp.downCast(diff, top, child.height, dy)
            }
            sideEffect.apply(child, factor)
        }
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        var isIntercept = interceptingEvents
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isIntercept = parent.isPointInChildBounds(child, ev.x.toInt(), ev.y.toInt())
                interceptingEvents = isIntercept
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                interceptingEvents = false
            }
        }
        return if (isIntercept) {
            helper(parent).shouldInterceptTouchEvent(ev)
        } else false
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        val helper = helper(parent)
        if (helper.capturedView == child || helper.isViewUnder(child, ev.x.toInt(), ev.y.toInt())) {
            helper.processTouchEvent(ev)
            return true
        } else {
            return false
        }
    }

    private fun helper(parent: ViewGroup): ViewDragHelper {
        var h = dragHelper
        if (h == null) {
            h = ViewDragHelper.create(parent, callback)
            dragHelper = h
            return h
        }
        return h
    }

    private inner class RecursiveSettle(private val child: View, private val diff: Int) : Runnable {

        override fun run() {
            if (dragHelper?.continueSettling(true) == true) {
                child.postOnAnimation(this)
            } else {
                child.removeCallbacks(this)
                listener?.onPostSettled(diff)
            }
        }
    }

    interface SwipeListener {

        /**
         * Сalled before settle
         * @param diff passed distance
         */
        fun onPreSettled(diff: Int)

        /**
         * Сalled after settle
         * @param diff passed distance
         */
        fun onPostSettled(diff: Int)
    }
}