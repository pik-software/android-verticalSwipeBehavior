package io.apiqa.android.exampleverticalswipe

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import ru.turlir.android.verticalswipe.*

class MainActivity : AppCompatActivity() {

    private val eventListener = object: VerticalSwipeBehavior.SwipeListener {

        override fun onPreSettled(diff: Int) {
            // nothing
        }

        override fun onPostSettled(diff: Int) {
            if (diff < 0) {
                Toast.makeText(this@MainActivity, "Banner out", Toast.LENGTH_SHORT).show()
            } // else banner moved to origin position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_refresh) {
            setup()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setup() {
        setContentView(R.layout.activity_main)
        val drag = findViewById<View>(R.id.drag)
        VerticalSwipeBehavior.from(drag).apply {
            settle = SettleOnTopAction()
            val sideDelegate = PropertySideEffect(View.ALPHA, View.SCALE_X, View.SCALE_Y)
            sideEffect = NegativeFactorFilterSideEffect(sideDelegate)
            val clampDelegate = BelowFractionalClamp(minFraction = 3f)
            clamp = SensitivityClamp(downSensitivity = 0.5f, delegate = clampDelegate)
            listener = eventListener
        }
    }
}
