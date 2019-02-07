package com.fanhl.spreadertablayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.core.util.Pools

/**
 * @author fanhl
 */
//@DecorView
class SpreaderTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    var tabsWrapperProvider: ITabsWrapperProvider = DefaultTabsWrapperProvider()

    var position = 0f

    init {
        val theme = context.theme

        val a = theme.obtainStyledAttributes(attrs, R.styleable.SpreaderTabLayout, defStyleAttr, R.style.Widget_Spreader_Tab_Layout)

        a.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val specSizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val specSizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(specSizeWidth, specSizeHeight)

        //剩余宽度
        var widthRemaining = specSizeWidth

        widthRemaining -= TAB_ITEM_WIDTH_DEFAULT.px * childCount

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val itemWidth = when (i) {
                position.floor() -> TAB_ITEM_WIDTH_DEFAULT.px + ((1 + i - position) * widthRemaining).toInt()
                position.ceil() -> TAB_ITEM_WIDTH_DEFAULT.px + widthRemaining - ((i - position) * widthRemaining).toInt()
                else -> TAB_ITEM_WIDTH_DEFAULT.px
            }

            this.measureChild(
                child,
                View.MeasureSpec.makeMeasureSpec(itemWidth, View.MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
        }

        tabsWrapperProvider.tabCount = childCount
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //剩余宽度
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
        super.onDraw(canvas)

        //FIXME 测试用
//        position = 1.2f
        position = 0f

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

        //缓存的RectF池
        private val rectFPool by lazy { Pools.SimplePool<RectF>(12) }
        //缓存的PointF池
        private val pointFPool by lazy { Pools.SimplePool<PointF>(12) }

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
            if (tabCount == 0) {
                return
            }

            if (tabCount == 1) {
                //fixme
                return
            }

            //以下是draw tab 背景

            val width = canvas.width
            val height = canvas.height
            //剩余宽度
            var widthRemaining = width.toFloat()


//            if (position == 0F) {
//                val leftTabRectF = rectFPool.acquire() ?: RectF()
//                val rightTabRectF = rectFPool.acquire() ?: RectF()
//
//                val rightTabWidth = calculateTabWidth(tabCount - 1)
//
//                rightTabRectF.apply {
//                    left = width - rightTabWidth
//                    top = 0f
//                    right = width.toFloat()
//                    bottom = height.toFloat()
//                }
//
//                widthRemaining -= rightTabWidth
//
//                leftTabRectF.apply {
//                    left = 0f
//                    top = 0f
//                    right = widthRemaining
//                    bottom = height.toFloat()
//                }
//
//                widthRemaining = 0f
//
//                drawTab(canvas, leftTabRectF)
//                drawTab(canvas, rightTabRectF)
//
//                rectFPool.release(leftTabRectF)
//                rectFPool.release(rightTabRectF)
//            }

//            //FIXME 以下废弃
//            // -------------------- 这里draw tabs --------------------
//
//            //左边有几个完全收缩的tabItem
//            val positionInt = position.toInt()
//
//            //绘制当前tab左边的收缩后的tab
//            if (positionInt > 0) {
//                val tabWidth = calculateTabWidth(positionInt)
////                canvas.drawRect(0F, 0F, tabWidth, canvas.height.toFloat(), expandedPaint)
//                canvas.drawLine(0F, 0F, tabWidth, canvas.height.toFloat(), expandedPaint)
//            }
//
//            //绘制在收缩、展开中的tabItem
//
//            //position的小数部分
//            val positionDecimal = position - positionInt
//
//
//            //右边有几个完全收缩的tabItem
//            val positionReverseInt = (tabCount - 1 - position).toInt()
//
//            //绘制当前tab右边的收缩后的tab
//            if (positionReverseInt > 0) {
//                val tabWidth = calculateTabWidth(positionInt)
////            canvas.drawRect(0F, 0F, TAB_MIN_WIDTH.pxf, canvas.height.toFloat(), expandedPaint)
//                canvas.drawLine(widthRemaining.toFloat() - tabWidth, 0F, widthRemaining.toFloat(), canvas.height.toFloat(), expandedPaint)
//            }
        }

        /**
         * 绘制背景tab
         */
        private fun drawTab(canvas: Canvas, rectF: RectF) {
//            canvas.drawRect(rectF, collapsedPaint)
            canvas.drawLine(rectF.left, rectF.top, rectF.right, rectF.bottom, expandedPaint)
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
