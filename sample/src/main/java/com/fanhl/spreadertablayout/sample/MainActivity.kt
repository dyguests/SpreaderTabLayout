package com.fanhl.spreadertablayout.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        assignViews()
        initData()
    }

    private fun assignViews() {
        spreader_tab_layout.onSelectedPositionChange = { position ->
        }
    }

    private fun initData() {
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager)

        tab_layout.setupWithViewPager(view_pager)
        spreader_tab_layout.setupWithViewPager(view_pager)

//        SnapHelper()
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) = ItemFragment.newInstance(position)

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return "Item$position"
    }
}

class ItemFragment : Fragment() {
    private val position by lazy { arguments?.getInt(ARG_POSITION) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.item_view, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = "ViewPager\n$position"
    }

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun newInstance(position: Int): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }
}
