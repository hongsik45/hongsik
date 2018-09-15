package com.team.elysium.moneyfit.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.team.elysium.moneyfit.R

/**
 * Created by sh on 2018-08-09.
 */
class RecommendationCodeInputDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_recommendation_code_input)
    }
}