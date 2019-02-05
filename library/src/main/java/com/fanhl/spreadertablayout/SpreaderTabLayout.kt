package com.fanhl.spreadertablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import androidx.annotation.Dimension
import androidx.viewpager.widget.ViewPager.DecorView

/**
 * @author fanhl
 */
@DecorView
class SpreaderTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {
    var tabsWrapperProvider: ITabsWrapperProvider = DefaultTabsWrapperProvider()

    /** FIXME 调试用*/
    var tabCount = 2

    var position = 0f

    init {
        val theme = context.theme

        val a = theme.obtainStyledAttributes(attrs, R.styleable.SpreaderTabLayout, defStyleAttr, R.style.Widget_Spreader_Tab_Layout)

        a.recycle()

        tabsWrapperProvider.tabCount = tabCount
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.RED)

        tabsWrapperProvider.draw(canvas ?: return, position)
    }

    companion object {
        @Dimension(unit = 0)
        private const val DEFAULT_HEIGHT = 48
        @Dimension(unit = 0)
        private val TAB_MIN_WIDTH_MARGIN = 56
    }

    /**
     * Tabs的包裹绘制提供者
     */
    interface ITabsWrapperProvider {
        /**FIXME*/
        var tabCount: Int

        fun draw(canvas: Canvas, position: Float)
    }

    /**
     * 默认Tabs容器
     */
    class DefaultTabsWrapperProvider : ITabsWrapperProvider {
        override var tabCount: Int = 0

        private val paint = Paint()

        init {
            paint.color = Color.BLUE
        }

        override fun draw(canvas: Canvas, position: Float) {
            canvas.drawRect(0F, 0F, TAB_MIN_WIDTH_MARGIN.pxf, canvas.height.toFloat(), paint)
        }
    }
}
