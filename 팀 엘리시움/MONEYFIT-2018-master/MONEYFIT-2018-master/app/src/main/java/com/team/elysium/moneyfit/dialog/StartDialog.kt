package com.team.elysium.moneyfit.dialog

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import com.team.elysium.moneyfit.App
import com.team.elysium.moneyfit.R
import com.team.elysium.moneyfit.adapter.ExerciseSpinnerAdapter
import android.app.Activity
import android.os.Handler
import com.team.elysium.moneyfit.CameraActivity
import com.team.elysium.moneyfit.CameraOneActivity


/**
 * Created by sh on 2018-07-19.
 */
class StartDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {

    private var exerciseSpinner: AppCompatSpinner? = null
    private var exerciseSpinnerAdapter: ExerciseSpinnerAdapter? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, R.color.gray_semi_transparent)))
        if (context is Activity) {
            ownerActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_start_dialog)

        init()
        initAdapter()
        initListener()
    }

    private fun init() {
        exerciseSpinner = findViewById(R.id.start_dialog_exercise_spinner)
    }

    private fun initAdapter() {
        exerciseSpinnerAdapter = ExerciseSpinnerAdapter(context!!, context!!.resources.getStringArray(R.array.exercise_list).toMutableList())
        exerciseSpinner!!.adapter = exerciseSpinnerAdapter
    }

    private fun initListener() {
        val closeButton = findViewById<ImageButton>(R.id.start_dialog_close_button)
        closeButton.setOnClickListener { dismiss() }

        val startButton = findViewById<Button>(R.id.start_dialog_start_button)
        val guideButton = findViewById<Button>(R.id.start_dialog_guide_button)

        startButton.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(App.getGlobalApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ownerActivity,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)

                dismiss()

            } else {

                // TODO : 임시로 Camera1 관련 클래스 [CameraOneActivity.class, CameraPreview.class] 남겨둚. 추후 삭제 요망
                val intent = Intent(context!!, CameraActivity::class.java)
                context!!.startActivity(intent)
                dismiss()
            }
        }
    }

}