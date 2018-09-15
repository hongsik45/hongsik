package com.team.elysium.moneyfit

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import com.team.elysium.moneyfit.adapter.OnboardAdapter
import com.team.elysium.moneyfit.fragment.OnboardRecycleFragment

/**
 * Created by sh on 2018-08-09.
 */
class OnboardActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var guideViewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private lateinit var guideViewAdapter: OnboardAdapter

    private var dots: ArrayList<ImageView> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_onboard)

        guideViewPager = findViewById(R.id.onboard_view_pager)
        dotsLayout = findViewById(R.id.slider_dots_layout)

        guideViewAdapter = OnboardAdapter(supportFragmentManager)

        val firstOnboardFragment = OnboardRecycleFragment.newInstance(0)
        val secondOnboardFragment = OnboardRecycleFragment.newInstance(1)
        val thirdOnboardFragment = OnboardRecycleFragment.newInstance(2)
        val fourthOnboardFragment = OnboardRecycleFragment.newInstance(3)

        guideViewAdapter.addFragment(firstOnboardFragment)
        guideViewAdapter.addFragment(secondOnboardFragment)
        guideViewAdapter.addFragment(thirdOnboardFragment)
        guideViewAdapter.addFragment(fourthOnboardFragment)

        guideViewPager.adapter = guideViewAdapter
        createDots(guideViewPager, guideViewAdapter)

    }

    private fun createDots(viewPager: ViewPager, adapter: OnboardAdapter) {
        dots.clear()
        if(dotsLayout.childCount > 0) {
            dotsLayout.removeAllViews()
        }

        for (i in 0 until adapter.count) {
            val img = ImageView(this@OnboardActivity)

            if(i == 0)
                img.setImageDrawable(ContextCompat.getDrawable(this@OnboardActivity, R.drawable.ic_pager_selector_activated_12dp))
            else
                img.setImageDrawable(ContextCompat.getDrawable(this@OnboardActivity, R.drawable.ic_pager_selector_nonactivated_10dp))

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

            params.setMargins(4, 0, 4, 0)

            dots.add(img)

            dotsLayout.addView(dots[i], params)
        }

        viewPager.addOnPageChangeListener(this)
        viewPager.setCurrentItem(0, true)
    }


    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        for (i in 0 until dots.size) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this@OnboardActivity, R.drawable.ic_pager_selector_nonactivated_10dp))
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(this@OnboardActivity, R.drawable.ic_pager_selector_activated_12dp))
    }
}