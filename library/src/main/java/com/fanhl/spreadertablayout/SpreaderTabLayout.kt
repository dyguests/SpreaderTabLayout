package com.fanhl.spreadertablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class SpreaderTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    // ---------- 变量 ----------


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specSizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val specSizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(specSizeWidth, measureDimension(heightMeasureSpec))
    }

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
}