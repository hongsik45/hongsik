package com.team.elysium.moneyfit

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.team.elysium.moneyfit.dialog.RecommendationCodeInputDialog

/**
 * Created by sh on 2018-08-02.
 */
class MyPageActivity : AppCompatActivity() {

    private lateinit var thumbnailView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var myRecommendationCodeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_my_page)


        initView()
        initListener()
    }

    private fun initView() {
        thumbnailView = findViewById(R.id.my_page_thumbnail)
        GlideApp.with(App.getGlobalApplicationContext())
                .load(Util.getThumbnail() ?: ContextCompat.getDrawable(this@MyPageActivity, R.drawable.moneyfit_coin_img))
                .circleCrop()
                .into(thumbnailView)
        nicknameTextView = findViewById(R.id.my_page_nickname)
        nicknameTextView.text = Util.getNickname()
        myRecommendationCodeTextView = findViewById(R.id.my_page_my_recommendation_code)
        val underLineRecommendationText = SpannableString(Util.getRecommendationCode())
        underLineRecommendationText.setSpan(UnderlineSpan(), 0, Util.getRecommendationCode().length, 0)
        myRecommendationCodeTextView.text = underLineRecommendationText
    }

    private fun initListener() {
        val backButton = findViewById<ImageButton>(R.id.my_page_close_button)
        backButton.setOnClickListener { onBackPressed() }

        myRecommendationCodeTextView.setOnClickListener {
            RecommendationCodeInputDialog(this@MyPageActivity).show()
        }
    }

}