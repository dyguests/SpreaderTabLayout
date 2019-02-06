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
    var tabCount = 4

    var position = 0f

    init {
        val theme = context.theme

        val a = theme.obtainStyledAttributes(attrs, R.styleable.SpreaderTabLayout, defStyleAttr, R.style.Widget_Spreader_Tab_Layout)

        a.recycle()

        tabsWrapperProvider.tabCount = tabCount
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //FIXME 测试用
        position = 1.2f

        tabsWrapperProvider.draw(canvas ?: return, position)
    }

    companion object {
        @Dimension(unit = 0)
        private const val TABS_HEIGHT_DEFAULT = 48
        @Dimension(unit = 0)
        private val TAB_MIN_WIDTH = 56
        @Dimension(unit = 0)
        private val TAB_ITEM_WIDTH_DEFAULT = 48
        @Dimension(unit = 0)
        private val TAB_HORIZONTAL_PADDING = 4
        /** 展开后的tab的最小宽度 */
        @Dimension(unit = 0)
        private val TAB_EXPANDED_MIN_WIDTH = 256
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

        private val collapsedPaint = Paint()
        private val expandedPaint = Paint()

        init {
            collapsedPaint.apply {
                color = Color.RED
                strokeWidth = 10.pxf
            }
            expandedPaint.apply {
                color = Color.BLUE
                strokeWidth = 10.pxf
            }
        }

        override fun draw(canvas: Canvas, position: Float) {
            val width = canvas.width

            if (tabCount == 0) {
                return
            }

            if (tabCount == 1) {
                //fixme
                return
            }

            //左边有几个完全收缩的tabItem
            val positionInt = position.toInt()

            //绘制当前tab左边的收缩后的tab
            if (positionInt > 0) {
                val tabWidth = calculateTabWidth(positionInt)
//                canvas.drawRect(0F, 0F, tabWidth, canvas.height.toFloat(), expandedPaint)
                canvas.drawLine(0F, 0F, tabWidth, canvas.height.toFloat(), expandedPaint)
            }

            //右边有几个完全收缩的tabItem
            val positionReverseInt = (tabCount - 1 - position).toInt()

            //绘制当前tab右边的收缩后的tab
            if (positionReverseInt > 0) {
                val tabWidth = calculateTabWidth(positionInt)
//            canvas.drawRect(0F, 0F, TAB_MIN_WIDTH.pxf, canvas.height.toFloat(), expandedPaint)
                canvas.drawLine(width.toFloat() - tabWidth, 0F, width.toFloat(), canvas.height.toFloat(), expandedPaint)
            }
        }

        /**
         * 当前tab有几个item
         */
        @Dimension(unit = 1)
        private fun calculateTabWidth(tabItemCount: Int): Float {
            val tabWidth = TAB_HORIZONTAL_PADDING.pxf + TAB_ITEM_WIDTH_DEFAULT.pxf * tabItemCount + TAB_HORIZONTAL_PADDING.pxf
            return maxOf(tabWidth, TAB_MIN_WIDTH.pxf)
        }
    }
}
