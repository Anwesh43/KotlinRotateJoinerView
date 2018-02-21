package ui.anwesome.com.kotlinrotatejoinerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.rotatejoinerview.RotateJoinerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RotateJoinerView.create(this)
    }
}
