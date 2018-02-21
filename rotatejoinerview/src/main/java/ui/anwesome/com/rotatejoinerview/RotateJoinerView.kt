package ui.anwesome.com.rotatejoinerview

/**
 * Created by anweshmishra on 21/02/18.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
class RotateJoinerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
    data class RotateJoiner(var w:Float, var h:Float) {
        val state = State()
        fun draw(canvas: Canvas, paint: Paint) {
            val size = Math.min(w,h)/3
            paint.color = Color.parseColor("#f44336")
            paint.strokeWidth = Math.min(w, h) / 50
            paint.strokeCap = Paint.Cap.ROUND
            canvas.save()
            canvas.translate(w/2, h/2)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i * 90f)
                canvas.save()
                val gap = (size/2) * state.scales[0]
                canvas.translate(-gap, gap)
                canvas.rotate(90f * state.scales[2])
                canvas.drawLine(0f, 0f , 0f, size * state.scales[1], paint)
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb: (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb: () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view:RotateJoinerView, var time:Int = 0) {
        val animator = Animator(view)
        var rotateJoiner:RotateJoiner ?= null
        fun render(canvas:Canvas, paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                rotateJoiner = RotateJoiner(w, h)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            rotateJoiner?.draw(canvas, paint)
            time++
            animator.animate {
                rotateJoiner?.update {
                    animator.stop()
                }
            }

        }
        fun handleTap() {
            rotateJoiner?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity: Activity): RotateJoinerView {
            val view = RotateJoinerView(activity)
            activity.setContentView(view)
            return view
        }
    }
}