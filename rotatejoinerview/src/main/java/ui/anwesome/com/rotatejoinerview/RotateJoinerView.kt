package ui.anwesome.com.rotatejoinerview

/**
 * Created by anweshmishra on 21/02/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class RotateJoinerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Animator(var view:View, var animated:Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class State(var prevScale: Float = 0f, var dir:Int = 0, var j:Int = 0, var jDir:Int = 1) {
        val scales:Array<Float> = arrayOf(0f, 0f, 0f)
        fun update(stopcb:(Float) -> Unit) {
            scales[j] += 0.1f * dir
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] += prevScale + dir
                j += dir
                if(j == scales.size || j == -1) {
                    dir = 0
                    jDir *= -1
                    j += jDir
                    prevScale = scales[j]
                    stopcb(prevScale)

                }
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            if(dir == 0) {
                dir = jDir
                startcb()
            }
        }
    }
}