package com.fanhl.spreadertablayout

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

/**
 * 将SpreaderTabLayout与RecyclerView绑定
 *
 * see [https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424]
 */
class SpreaderRecyclerViewHelper private constructor(
    private val tabLayout: SpreaderTabLayout,
    private val recyclerView: RecyclerView?
) {
    private val snapHelper = PagerSnapHelper()

    private val onScrollListener = SnapOnScrollListener(snapHelper) { position ->
        tabLayout.selectedPosition = position
    }

    private fun setupWithViewPager() {
        if (recyclerView == null) {
            tabLayout.setupWith(null)
            return
        }

        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(onScrollListener)

        tabLayout.setupWith(
            object : SpreaderTabLayout.IBindView {
                override fun onBindViewPositionChange(position: Float) {

                }

                override fun onTabLayoutPositionChange(position: Int) {
//                recyclerView.currentItem = position
                }

                override fun detach() {
                    recyclerView.removeOnScrollListener(onScrollListener)
                }
            })
    }

    companion object {
        fun attachWith(tabLayout: SpreaderTabLayout, recyclerView: RecyclerView?) {
            SpreaderRecyclerViewHelper(tabLayout, recyclerView).setupWithViewPager()
        }
    }

    class SnapOnScrollListener(
        private val snapHelper: SnapHelper,
        var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL,
        var onSnapPositionChange: ((position: Int) -> Unit)? = null
    ) : RecyclerView.OnScrollListener() {

        enum class Behavior {
            NOTIFY_ON_SCROLL,
            NOTIFY_ON_SCROLL_STATE_IDLE
        }

        private var snapPosition = RecyclerView.NO_POSITION

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (behavior == Behavior.NOTIFY_ON_SCROLL) {
                maybeNotifySnapPositionChange(recyclerView)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
                && newState == RecyclerView.SCROLL_STATE_IDLE
            ) {
                maybeNotifySnapPositionChange(recyclerView)
            }
        }

        private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
            val snapPosition = snapHelper.getSnapPosition(recyclerView)
            val snapPositionChanged = this.snapPosition != snapPosition
            if (snapPositionChanged) {
                onSnapPositionChange?.invoke(snapPosition)
                this.snapPosition = snapPosition
            }
        }
    }
}

internal fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}
