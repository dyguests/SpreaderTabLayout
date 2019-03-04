package com.fanhl.spreadertablayout

import androidx.viewpager.widget.ViewPager

/**
 * 将SpreaderTabLayout与ViewPager绑定
 */
class SpreaderViewPagerHelper private constructor(
    private val tabLayout: SpreaderTabLayout,
    private val viewPager: ViewPager?
) {
    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            Log.d(TAG, "onPageScrolled: position:$position positionOffset:$positionOffset positionOffsetPixels:$positionOffsetPixels")

        }

        override fun onPageSelected(position: Int) {
            tabLayout.selectedPosition = position
        }
    }

    private fun setupWithViewPager() {
        if (viewPager == null) {
            tabLayout.setupWith(null)
            return
        }

        viewPager.addOnPageChangeListener(onPageChangeListener)

        tabLayout.setupWith(object : SpreaderTabLayout.IBindView {
            override fun onBindViewPositionChange(position: Float) {

            }

            override fun onTabLayoutPositionChange(position: Int) {
                viewPager.currentItem = position
            }

            override fun detach() {
                viewPager.removeOnPageChangeListener(onPageChangeListener)
            }
        })
    }

    companion object {
        private val TAG = SpreaderViewPagerHelper::class.java.simpleName
        /**
         * 将SpreaderTabLayout与ViewPager绑定
         */
        fun attachWith(tabLayout: SpreaderTabLayout, viewPager: ViewPager?) {
            SpreaderViewPagerHelper(tabLayout, viewPager).setupWithViewPager()
        }
    }
}
