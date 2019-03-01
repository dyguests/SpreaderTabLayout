package com.fanhl.spreadertablayout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.constraintlayout.motion.widget.MotionLayout

class SpreaderTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }


    // ---------- 输入变量 ----------

    var selectedPosition = 0
        set(value) {
            if (field == value) {
                return
            }

            field = value

            startSpreaderAnim()
        }

    // ---------- 变量 ----------
    /** 所有child的布局位置 */
    private val childLayouts = mutableListOf<Rect>()

    private var spreaderAnim: ValueAnimator? = null

    /** 正在动画中的实际的position */
    private var positionProgress = 0f

    init {
        //启用绘制背景
        setWillNotDraw(false)

        isClickable = true
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        childLayouts.add(Rect())
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        childLayouts.removeAt(childLayouts.size - 1)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        return super.onInterceptTouchEvent(ev)
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_UP -> {
                Toast.makeText(context, "test touch", Toast.LENGTH_SHORT).show()
            }
        }
//        return super.onTouchEvent(event)
        return true
    }

    /**
     * 这里设置的宽度都是展开后的宽度
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specSizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
//        val specSizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val measuredHeight = measureDimension(heightMeasureSpec)
        setMeasuredDimension(specSizeWidth, measuredHeight)

        //剩余宽度
        val widthRemaining = specSizeWidth - TAB_ITEM_WIDTH_DEFAULT * childCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val itemWidth = when (i) {
                positionProgress.floor() -> TAB_ITEM_WIDTH_DEFAULT + ((1 + i - positionProgress) * widthRemaining).toInt()
                positionProgress.ceil() -> TAB_ITEM_WIDTH_DEFAULT + widthRemaining - ((i - positionProgress) * widthRemaining).toInt()
                else -> TAB_ITEM_WIDTH_DEFAULT
            }

            measureChild(
                child,
                View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(measuredHeight, View.MeasureSpec.EXACTLY)
            )
        }
    }

    /**
     * 设置最小值是 48dp
     */
    private fun measureDimension(measureSpec: Int): Int {
        var result: Int
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = 48.px
            if (mode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //使用了的宽度
        var widthUsed = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            childLayouts[i].apply {
                left = widthUsed
                top = 0
                right = widthUsed + child.measuredWidth
                bottom = child.measuredHeight
            }

            widthUsed += child.measuredWidth


            child.layout(
                childLayouts[i].left,
                childLayouts[i].top,
                childLayouts[i].right,
                childLayouts[i].bottom
            )

            // [0,1] 为0时是收到状态，为1时是展开状态
            val spreaderProgress = when (i) {
                positionProgress.floor() -> 1 + i - positionProgress
                positionProgress.ceil() -> i - positionProgress
                else -> 0f
            }

            if (child is MotionLayout) {
                child.progress = 1 - spreaderProgress
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas ?: return)
//        canvas.drawCircle(0f, 0f, 100f, paint)
    }

    /**
     * 执行切换position的动画
     */
    private fun startSpreaderAnim() {
        if (spreaderAnim?.isRunning == true) {
            spreaderAnim?.cancel()
        }
        spreaderAnim = ValueAnimator.ofFloat(positionProgress, selectedPosition.toFloat())
        spreaderAnim?.interpolator = DecelerateInterpolator()
        spreaderAnim?.addUpdateListener { animator ->
            positionProgress = (animator.animatedValue as? Float ?: return@addUpdateListener)
            requestLayout()
        }
        spreaderAnim?.start()
    }

    companion object {
        private val TAG = SpreaderTabLayout::class.java.simpleName

        @Dimension(unit = 1)
        private val TAB_ITEM_WIDTH_DEFAULT = 48.px
    }
}