package com.team.elysium.moneyfit.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.team.elysium.moneyfit.*

/**
 * Created by sh on 2018-08-09.
 */
class OnboardRecycleFragment : Fragment() {

    private lateinit var image: ImageView
    private lateinit var mainTextView: TextView
    private lateinit var guideTextView: TextView
    private var position: Int = 0
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments!!.getInt(ARG_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_onboard, container, false)

        init(view)
        setInfo(position)

        return view
    }

    private fun init(view: View) {
        image = view.findViewById(R.id.onboard_image)
        mainTextView = view.findViewById(R.id.onboard_main_text)
        guideTextView = view.findViewById(R.id.onboard_guide_message)
        startButton = view.findViewById(R.id.onboard_start)
        startButton.setOnClickListener {
            val intent = Intent(context!!, SplashActivity::class.java)
            startActivity(intent)
            (context!! as OnboardActivity).finish()
        }
    }

    fun setInfo(position: Int) {

        when(position) {
            0 -> {
                GlideApp.with(App.getGlobalApplicationContext())
                        .load(R.drawable.onboarding_artboard_1)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(image)

                mainTextView.text = context!!.resources.getString(R.string.onboard_first_main_text)
                guideTextView.text = context!!.resources.getString(R.string.onboard_guide_first_text)
                startButton.visibility = View.GONE
            }
            1 -> {
                GlideApp.with(App.getGlobalApplicationContext())
                        .load(R.drawable.onboarding_artboard_2)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(image)

                mainTextView.text = context!!.resources.getString(R.string.onboard_second_main_text)
                guideTextView.text = context!!.resources.getString(R.string.onboard_guide_second_text)
                startButton.visibility = View.GONE
            }
            2 -> {
                GlideApp.with(App.getGlobalApplicationContext())
                        .load(R.drawable.onboarding_artboard_3)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(image)

                mainTextView.text = context!!.resources.getString(R.string.onboard_third_main_text)
                guideTextView.text = context!!.resources.getString(R.string.onboard_guide_third_text)
                startButton.visibility = View.GONE
            }
            3 -> {
                GlideApp.with(App.getGlobalApplicationContext())
                        .load(R.drawable.onboarding_artboard_4)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(image)

                mainTextView.text = context!!.resources.getString(R.string.onboard_fourth_main_text)
                guideTextView.text = context!!.resources.getString(R.string.onboard_guide_fourth_text)
                startButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private val ARG_TYPE = "param1"
        /**
         * @param position Parameter 1.
         * @return A new instance of fragment ContactRecycleFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(position: Int): OnboardRecycleFragment {
            val fragment = OnboardRecycleFragment()
            val args = Bundle()
            args.putInt(ARG_TYPE, position)
            fragment.arguments = args
            return fragment
        }
    }
}