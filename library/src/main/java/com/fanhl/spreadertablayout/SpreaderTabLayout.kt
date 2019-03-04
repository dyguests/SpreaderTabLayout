package com.fanhl.spreadertablayout

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.CheckResult
import androidx.annotation.Dimension
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

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
            onSelectedPositionChange?.invoke(field)
        }

    // ---------- 变量 ----------
    /** 所有child的布局位置 */
    private val childLayouts = mutableListOf<SpreaderRect>()

    private var spreaderAnim: ValueAnimator? = null

    /** 事件是否在当前消费 */
    private var isConsumeCurr = false

    /** 正在动画中的实际的position */
    private var positionProgress = 0f

    // ---------- 回调 ----------

    var onSelectedPositionChange: ((position: Int) -> Unit)? = null

    /**
     * 绑定的视图
     */
    private var bindView: IBindView? = null

    init {
        //启用绘制背景
        setWillNotDraw(false)

        isClickable = true
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        childLayouts.add(SpreaderRect())
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        childLayouts.removeAt(childLayouts.size - 1)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action ?: return false) {
            MotionEvent.ACTION_DOWN -> {
                val index = inChild(ev.x.toInt(), ev.y.toInt())
                if (index < 0) {
                    isConsumeCurr = false
                    return false
                }

                if (childLayouts[index].status == SpreaderRect.STATUS_COLLAPSED
                    || childLayouts[index].status == SpreaderRect.STATUS_EXPENDING
                ) {
                    isConsumeCurr = true
                    return true
                } else if (childLayouts[index].status == SpreaderRect.STATUS_EXPENDED) {
                    isConsumeCurr = false
                    return false
                }
            }
            else -> {
                val index = inChild(ev.x.toInt(), ev.y.toInt())
                if (index < 0) {
                    isConsumeCurr = false
                    return false
                }

                if (childLayouts[index].status == SpreaderRect.STATUS_COLLAPSED
                    || childLayouts[index].status == SpreaderRect.STATUS_EXPENDING
                ) {
                    isConsumeCurr = true
                    return true
                } else if (childLayouts[index].status == SpreaderRect.STATUS_EXPENDED) {
                    isConsumeCurr = false
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
//        return true
    }

    /**
     * 返回(x,y)所在的child的index,没有则返回-1
     */
    @CheckResult
    private fun inChild(x: Int, y: Int): Int {
        childLayouts.forEachIndexed { index, childLayout ->
            if (childLayout.left <= x) {
                if (childLayout.right > x) {
                    return if (childLayout.top <= y && childLayout.bottom >= y) {
                        index
                    } else {
                        -1
                    }
                } else if (childLayout.right == x && index == childLayouts.size - 1) {
                    return if (childLayout.top <= y && childLayout.bottom >= y) {
                        index
                    } else {
                        -1
                    }
                }
            } else {
                return -1
            }
        }
        return -1
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_UP -> {
                if (isConsumeCurr) {
                    val index = inChild(ev.x.toInt(), ev.y.toInt())
                    if (index < 0) {
//                        return false
                        return super.onTouchEvent(ev)
                    }
                    performTabClick(index)
                    return true
                }
            }
        }
        return super.onTouchEvent(ev)
//        return true
    }

    /**
     * 点击第i个tab处理
     */
    private fun performTabClick(position: Int) {
        selectedPosition = position
        bindView?.onTabLayoutPositionChange(position)
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

            //确认tab的展开状态
            if (i == positionProgress.floor() && abs(i.toFloat() - positionProgress) > Float.MIN_VALUE) {
                childLayouts[i].status = SpreaderRect.STATUS_EXPENDING
            } else if (i == positionProgress.ceil() && abs(i.toFloat() - positionProgress) > Float.MIN_VALUE) {
                childLayouts[i].status = SpreaderRect.STATUS_EXPENDING
            } else if (abs(i.toFloat() - positionProgress) < Float.MIN_VALUE) {
                childLayouts[i].status = SpreaderRect.STATUS_EXPENDED
            } else {
                childLayouts[i].status = SpreaderRect.STATUS_COLLAPSED
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

    fun setupWithViewPager(viewPager: ViewPager?) {
        SpreaderViewPagerHelper.attachWidth(this, viewPager)
    }

    fun setupWithViewPager(recyclerView: RecyclerView?) {
        SpreaderRecyclerViewHelper.attachWidth(this, recyclerView)
    }

    /**
     * 将其它视图与TabLayout绑定
     */
    fun setupWith(bindView: IBindView?) {
        if (this.bindView == bindView) {
            return
        }

        if (this.bindView != null) {
            this.bindView?.detach()
        }

        if (bindView != null) {
            this.bindView = bindView
        } else {
            this.bindView = null
        }
    }

    companion object {
        private val TAG = SpreaderTabLayout::class.java.simpleName

        @Dimension(unit = 1)
        private val TAB_ITEM_WIDTH_DEFAULT = 48.px
    }

    data class SpreaderRect(
        var left: Int = 0,
        var top: Int = 0,
        var right: Int = 0,
        var bottom: Int = 0,
        var status: Int = STATUS_COLLAPSED
    ) {
        companion object {
            const val STATUS_COLLAPSED = 0
            const val STATUS_EXPENDED = 1
            const val STATUS_EXPENDING = 2
        }
    }

    /**
     * 用来绑定多种BindedView的接口
     */
    interface IBindView {
        fun onBindViewPositionChange(position: Float)
        fun onTabLayoutPositionChange(position: Int)
        fun detach()
    }
}