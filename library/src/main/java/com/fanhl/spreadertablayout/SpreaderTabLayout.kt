package com.fanhl.spreadertablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension

class SpreaderTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    // ---------- 变量 ----------

    private var position = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specSizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
//        val specSizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val measuredHeightSpec = measureDimension(heightMeasureSpec)
        setMeasuredDimension(specSizeWidth, measuredHeightSpec)

        //剩余宽度
        var widthRemaining = specSizeWidth

        widthRemaining -= TAB_ITEM_WIDTH_DEFAULT * childCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val itemWidth = when (i) {
                position.floor() -> TAB_ITEM_WIDTH_DEFAULT + ((1 + i - position) * widthRemaining).toInt()
                position.ceil() -> TAB_ITEM_WIDTH_DEFAULT + widthRemaining - ((i - position) * widthRemaining).toInt()
                else -> TAB_ITEM_WIDTH_DEFAULT
            }

            measureChild(
                child,
                View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY),
                measuredHeightSpec
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

            child.layout(
                widthUsed,
                0,
                widthUsed + child.measuredWidth,
                child.measuredHeight
            )

            widthUsed += child.measuredWidth
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas ?: return)
        canvas.drawCircle(0f, 0f, 100f, paint)
    }

    companion object {
        @Dimension(unit = 1)
        private val TAB_ITEM_WIDTH_DEFAULT = 48.px
    }
}