package com.team.elysium.moneyfit

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.team.elysium.moneyfit.adapter.RankAdapter
import com.team.elysium.moneyfit.dataclass.UserRank
import com.team.elysium.moneyfit.thread.HttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

/**
 * Created by sh on 2018-08-23.
 */
class RankActivity : AppCompatActivity() {

    private lateinit var thumbnailView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var beforeWeekMoneyTextView: TextView
    private lateinit var totalMoneyTextView: TextView
    private lateinit var numberOfBoostTextView: TextView

    private val rankDataList = ArrayList<UserRank>()

    private lateinit var rankRecyclerView: RecyclerView
    private var rankAdapter: RankAdapter? = null

    private val rankDataCallback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
        }

        override fun onResponse(call: Call?, response: Response?) {

            val result = response?.body()?.string()

            try {

                val jsonArray = JSONArray(result)

                val thumbnailString = resources.getString(R.string.thumb_k)
                val nicknameString = resources.getString(R.string.nick_k)
                val moneyString = resources.getString(R.string.money)

                if(jsonArray.length() > 0) {

                    for (i in 0 until jsonArray.length()) {

                        val info = jsonArray.getJSONObject(i)

                        val thumbnail = info.getString(thumbnailString)
                        val nickname = info.getString(nicknameString)
                        val money = info.getString(moneyString)

                        rankDataList.add(UserRank((i + 1).toString(), thumbnail, nickname, money))
                    }

                    runOnUiThread {

                        rankAdapter = RankAdapter(this@RankActivity, rankDataList, GlideApp.with(App.getGlobalApplicationContext()))

                        rankRecyclerView.adapter = rankAdapter
                        rankAdapter!!.notifyDataSetChanged()
                    }
                }


            } catch (e: JSONException) {

            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_rank)

        initView()
        initRankData()
    }

    private fun initRankData() {
        HttpRequest.getInstance().getRankData(Util.getID(), rankDataCallback)
    }

    private fun initView() {
        thumbnailView = findViewById(R.id.my_rank_thumbnail)
        GlideApp.with(App.getGlobalApplicationContext())
                .load(if(Util.getThumbnail() == null || Util.getThumbnail() == "") ContextCompat.getDrawable(this@RankActivity, R.drawable.moneyfit_coin_img) else Util.getThumbnail())
                .circleCrop()
                .into(thumbnailView)
        nicknameTextView = findViewById(R.id.my_rank_nickname)
        nicknameTextView.text = Util.getNickname()
        beforeWeekMoneyTextView = findViewById(R.id.my_rank_money_before_week_text)
        totalMoneyTextView = findViewById(R.id.my_rank_money_total_week_text)
        numberOfBoostTextView = findViewById(R.id.my_rank_number_of_boost)
        rankRecyclerView = findViewById(R.id.rank_recycler_view)
        rankRecyclerView.layoutManager = LinearLayoutManager(this@RankActivity)
        rankRecyclerView.setHasFixedSize(true)

        val backButton = findViewById<ImageButton>(R.id.rank_close_button)
        backButton.setOnClickListener { onBackPressed() }
    }

}