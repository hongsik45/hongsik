package com.team.elysium.moneyfit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import com.bumptech.glide.Glide
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.*
import com.team.elysium.moneyfit.adapter.WeekAdapter
import com.team.elysium.moneyfit.dialog.StartDialog
import com.team.elysium.moneyfit.thread.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var thumbnailImageView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var bottomCardView: CardView
    private lateinit var bottomCardArrow: ImageView
    private lateinit var semiTransparentBackground: LinearLayout
    private lateinit var weekGridView: GridView
    private lateinit var mainBackground: RelativeLayout
    private lateinit var rewardTextView: TextView
    private lateinit var periodTextView: TextView
    private var weekAdapter: WeekAdapter? = null
    private lateinit var startButton: ImageButton

    private lateinit var timerTextView: TextView
    private lateinit var recommendationTextView: TextView

    private lateinit var showRankButton: ImageButton
    private lateinit var myPageButton: Button

    private var isBottomCardActivated: Boolean = false

    private val BOTTOM_CARD_MOVING_SPEED = 200L

    /**
     * 타이머 관련 스레드
     */
    private var timerHandler: Handler? = null
    private var timerThread: Runnable? = null

    /**
     * 인자 전달을 위한 커스텀 Runnable 클래스
     */
    private inner class TimerRunnable(private val endTime : Long) : Runnable {

        override fun run() {

            val c = Calendar.getInstance(Locale.KOREA)

            c.timeInMillis = endTime - (System.currentTimeMillis() + 32400000)

            val restOfDays = c.get(Calendar.DATE)
            val restOfHours = c.get(Calendar.HOUR_OF_DAY)
            val restOfMinutes = c.get(Calendar.MINUTE)
            val restOfSeconds = c.get(Calendar.SECOND)

            timerTextView.text = resources.getString(R.string.main_timer_text_format, restOfDays, restOfHours, restOfMinutes, restOfSeconds)
            timerHandler?.postDelayed(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!Util.isNetworkConnected()) {
            val t = Toast.makeText(App.getGlobalApplicationContext(), resources.getString(R.string.check_network_status), Toast.LENGTH_LONG)
            t.show()
            finish()
            return
        }

        initView()
        initListener()
        initProfile()
    }

    override fun onResume() {
        startTimerBackground()
        super.onResume()
    }

    override fun onPause() {
        stopTimerBackground()
        super.onPause()
    }

    private fun startTimerBackground() {

        initRewardAndPeriod()
        timerHandler = Handler()
    }

    private fun stopTimerBackground() {
        timerHandler?.removeCallbacks(timerThread)
        timerHandler = null
    }

    private fun initRewardAndPeriod() {

        HttpRequest.getInstance().getEndDate(Util.getID(), endDateCallback)

        // TODO : 상금 서버 설정
        rewardTextView.text = resources.getString(R.string.reward, "100")
        periodTextView.text = resources.getString(R.string.period, 4, 15, 3)
    }

    private fun requestWeekData(id: String) {
        HttpRequest.getInstance().getWeekData(id, resources.getString(R.string.allow), weekDataCallback)
    }

    private fun initProfile() {
        val id = intent.extras.getLong(resources.getString(R.string.id_k), 0L)
        val nickname = intent.extras.getString(resources.getString(R.string.nick_k))
        val thumbnailPath = intent.extras.getString(resources.getString(R.string.thumb_k))

        val sharedPrefs = this.getSharedPreferences(
                resources.getString(R.string.first_execution), Context.MODE_PRIVATE)
        val si = sharedPrefs.getString(resources.getString(R.string.id_k), "")


        if(intent.extras.getString(resources.getString(R.string.first_execution), "") == resources.getString(R.string.start)) {
            // 첫 실행

            val editor = sharedPrefs.edit()
            editor.putString(resources.getString(R.string.id_k), id.toString())
            editor.apply()

            HttpRequest.getInstance().registerUser(id.toString(), nickname, thumbnailPath, resources.getString(R.string.allow), registerUserCallback)
        } else {
            requestWeekData(Util.getID())
        }

        if(thumbnailPath != "") {
            GlideApp.with(App.getGlobalApplicationContext())
                    .load(thumbnailPath)
                    .circleCrop()
                    .into(thumbnailImageView)
        } else {
            GlideApp.with(App.getGlobalApplicationContext())
                    .load(ContextCompat.getDrawable(applicationContext, R.drawable.moneyfit_coin_img))
                    .circleCrop()
                    .into(thumbnailImageView)
        }
        nicknameTextView.text = nickname
    }

    private val registerUserCallback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {}

        override fun onResponse(call: Call?, response: Response?) {
            runOnUiThread { requestWeekData(Util.getID()) }
        }
    }

    private val endDateCallback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {}

        override fun onResponse(call: Call?, response: Response?) {
            val responseString = response?.body()?.string()

            try {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                val endDate = df.parse(responseString)

                timerThread = TimerRunnable(endDate.time)
                timerHandler?.postDelayed(timerThread, 0)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private val weekDataCallback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            Log.e("responseError", "error")
        }

        override fun onResponse(call: Call?, response: Response?) {

            val responseString = response?.body()?.string()

            Log.e("response", responseString)

            val userInfo = responseString?.split(",")

            if(userInfo != null && userInfo.size == 2) {
                val weekData = userInfo[0].toIntOrNull()
                val recommendationCode = userInfo[1]
                Util.setRecommendationCode(recommendationCode)

                if(weekData != null) {
                    this@MainActivity.runOnUiThread {
                        kotlin.run {

                            val succeededList = kotlin.collections.ArrayList<Boolean>()

                            userInfo[0].asIterable().forEach { kotlin.run { if(it.toInt() == 49) succeededList.add(true) else succeededList.add(false) } }

                            succeededList[0] = true
                            succeededList[2] = true

                            weekAdapter = com.team.elysium.moneyfit.adapter.WeekAdapter(this@MainActivity,
                                    R.layout.content_day, succeededList, resources.getStringArray(R.array.days_of_week))

                            weekGridView.adapter = weekAdapter

                            val formattedRecommendationText = resources.getString(R.string.main_recommendation_text_format, recommendationCode)

                            val underLineRecommendationText = SpannableString(formattedRecommendationText)
                            underLineRecommendationText.setSpan(UnderlineSpan(), 0, formattedRecommendationText.length, 0)
                            recommendationTextView.text = underLineRecommendationText
                        }
                    }
                }
            }
        }
    }


    private fun initListener() {

        bottomCardView.animate().translationY(resources.getDimensionPixelSize(R.dimen.bottom_card_down_distance).toFloat()).withLayer()
        bottomCardView.setOnClickListener {
            if(isBottomCardActivated) {
                bottomCardView.animate().translationY(resources.getDimensionPixelSize(R.dimen.bottom_card_down_distance).toFloat()).duration = BOTTOM_CARD_MOVING_SPEED
                isBottomCardActivated = false
                bottomCardArrow.animate().rotationX(0F)
                semiTransparentBackground.bringToFront()
                semiTransparentBackground.visibility = View.INVISIBLE
            } else {
                bottomCardView.animate().translationY(0F).duration = BOTTOM_CARD_MOVING_SPEED
                isBottomCardActivated = true
                bottomCardArrow.animate().rotationX(180F)
                semiTransparentBackground.bringToFront()
                semiTransparentBackground.visibility = View.VISIBLE
            }
         }

        startButton.setOnClickListener {
            StartDialog(this@MainActivity, R.style.FadeDialogTheme).show()
        }

        showRankButton.setOnClickListener {
            val intent = Intent(this@MainActivity, RankActivity::class.java)
            startActivity(intent)
        }

        myPageButton.setOnClickListener {

            val intent = Intent(this@MainActivity, MyPageActivity::class.java)
            startActivity(intent)
        }

        val moreButton = findViewById<ImageButton>(R.id.header_more_button)
        moreButton.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        val moneyManagingButton = findViewById<Button>(R.id.bottom_card_prize_management_button)
        moneyManagingButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MoneyManagingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        mainBackground = findViewById(R.id.main_background)
        val mainBackgroundImage = findViewById<ImageView>(R.id.main_background_image)
        GlideApp.with(App.getGlobalApplicationContext())
                .load(ContextCompat.getDrawable(applicationContext, R.drawable.gradient_main_card))
                .centerCrop()
                .into(mainBackgroundImage)
        val bottomCardBackgroundImage = findViewById<ImageView>(R.id.main_bottom_card_view_background_image)
        GlideApp.with(App.getGlobalApplicationContext())
                .load(ContextCompat.getDrawable(applicationContext, R.drawable.gradient_main_card))
                .centerCrop()
                .into(bottomCardBackgroundImage)
        thumbnailImageView = findViewById(R.id.main_thumbnail_image_view)
        nicknameTextView = findViewById(R.id.main_nickname_text_view)
        bottomCardView = findViewById(R.id.main_bottom_card_view)
        bottomCardArrow = findViewById(R.id.bottom_card_open_up_arrow)
        semiTransparentBackground = findViewById(R.id.main_semi_transparent_background)
        weekGridView = findViewById(R.id.main_week_grid_view)
        rewardTextView = findViewById(R.id.main_reward_text_view)
        periodTextView = findViewById(R.id.main_period_text_view)
        startButton = findViewById(R.id.main_start_button)
        timerTextView = findViewById(R.id.main_timer_text_view)
        recommendationTextView = findViewById(R.id.main_recommendation_view)
        showRankButton = findViewById(R.id.header_show_rank_button)
        myPageButton = findViewById(R.id.bottom_card_show_my_page_button)

        initHeader()
    }

    private fun initHeader() {
        val bi = findViewById<ImageView>(R.id.header_bi)
        Glide.with(App.getGlobalApplicationContext())
                .load(ContextCompat.getDrawable(this@MainActivity, R.drawable.moneyfit_text_bi))
                .into(bi)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 200) {
            var isGranted = true
            for(result in grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED)
                    isGranted = false
            }

            if(isGranted) {
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, 200)
            } else {
                Util.toast(this, resources.getString(R.string.toast_no_permission))
            }
        }
    }


}
