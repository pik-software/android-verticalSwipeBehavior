VerticalSwipeBehavior
=====

VerticalSwipeBehavior is a small and flexible vertical swipe behavior for android CoordinatorLayout.
Moves the released view at the given vertical position.

![Example](/image/example.gif)

Download
--------

[ ![Download](https://img.shields.io/maven-central/v/ru.turlir.android/verticalswipebehavior) ](https://search.maven.org/artifact/ru.turlir.android/verticalswipebehavior)

Check repository

``` gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Add dependency in to `build.gradle`

``` gradle
dependencies {
    implementation 'ru.turlir.android:verticalswipebehavior:1.2.0'
}
```

Usage
--------

You should have CoordinatorLayout in parent view. Add tag
`app:layout_behavior="ru.turlir.android.verticalswipe.VerticalSwipeBehavior"` to your banner.
Padding or margin is optional for positioning. View top is a start point for animation.

``` xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/drag"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/drag_me"
        app:layout_behavior="ru.turlir.android.verticalswipe.VerticalSwipeBehavior" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

Set `sideEffect`, `clamp` and `settle` into VerticalSwipeBehavior for customize banner movement.

``` kotlin
val drag = findViewById<View>(R.id.drag)
VerticalSwipeBehavior.from(drag).apply {
   sideEffect = NegativeFactorFilterSideEffect(AlphaElevationSideEffect())
   clamp = BelowFractionalClamp(minFraction = 3f)
   settle = SettleOnTopAction()
}
```

Details
--------

* `SideEffect` – Changes view properties depending on the progress of movement.
Default implementation `AlphaElevationSideEffect` change alpha and elevation of view.
You may use `NegativeFactorFilterSideEffect` for simple composition of behavior.
`WithoutSideEffect` does not change any properties of view.
`PropertySideEffect` - common way for changing several properties of view.

``` kotlin
class AlphaElevationSideEffect: SideEffect {

    override fun apply(child: View, factor: Float) {
        child.elevation = elevation * (1f - abs(factor)) // special for elevation-aware view
        child.alpha = 1f - abs(factor)
    }
}
```

* `VerticalClamp` – Have limits on moving view vertically.
It is often necessary to limit movement to a part of the height of view.
This is done by `FractionClamp`. Also you can use `SensitivityClamp` for tuning sensitivity.

``` kotlin
class FractionClamp(private val maxFraction: Float = 1f, private val minFraction: Float = 1f): VerticalClamp {

    override fun constraint(height: Int, top: Int, dy: Int): Int {
        val min = min(top, originTop + (height * minFraction).toInt())
        return max(min, originTop - (height * maxFraction).toInt())
    }

    override fun downCast(distance: Int, top: Int, height: Int, dy: Int): Float {
        return distance / (height * maxFraction)
    }

    override fun upCast(distance: Int, top: Int, height: Int, dy: Int): Float {
        return distance / (height * minFraction)
    }
}
```

* `PostAction` – Responsible for changing view position after swipe is completed.
ViewDragHelper is used for animation moving of view  in consideration of pointer speed.
`OriginSettleAction` moves the view to the starting position.

``` kotlin
class OriginSettleAction: PostAction {

    override fun releasedBelow(helper: ViewDragHelper, diff: Int, child: View): Boolean {
        return helper.settleCapturedViewAt(child.left, originTop)
    }

    override fun releasedAbove(helper: ViewDragHelper, diff: Int, child: View): Boolean {
        return helper.settleCapturedViewAt(child.left, originTop)
    }
}
```

Blogs
--------

[Пишем гибкий VerticalSwipeBehavior | Хабр](https://habr.com/ru/post/481910/)


