package com.fanhl.spreadertablayout

import androidx.viewpager.widget.ViewPager

class SpreaderViewPagerHelper {
    companion object {
        /**
         * 将SpreaderTabLayout与ViewPager绑定
         */
        fun attachWidth(tabLayout: SpreaderTabLayout, viewPager: ViewPager?) {
            if (viewPager == null) {
                tabLayout.setupWith(null)
            }

            tabLayout.setupWith(object : SpreaderTabLayout.IBindView {
                override fun onBindedViewPositionChange(position: Float) {

                }

                override fun onTabLayoutPositionChange(position: Int) {
                }
            })
        }
    }
}
