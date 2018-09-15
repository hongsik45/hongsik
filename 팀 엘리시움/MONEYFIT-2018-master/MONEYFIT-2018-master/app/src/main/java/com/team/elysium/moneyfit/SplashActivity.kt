package com.team.elysium.moneyfit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile

/**
 * Created by sh on 2018-07-07.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO : sharedPref 이용해서 login status 설정. 0 : 미로그인, 1 : 카톡, 2 : 페북, 3 : 구글

        if(!Util.isNetworkConnected()) {
            val t = Toast.makeText(App.getGlobalApplicationContext(), resources.getString(R.string.check_network_status), Toast.LENGTH_LONG)
            t.show()
            finish()
            return
        }

        val sharedPrefs = this.getSharedPreferences(
                resources.getString(R.string.first_execution), Context.MODE_PRIVATE)

        if(Session.getCurrentSession().checkAndImplicitOpen()) {

            // 자동로그인 부분
            UserManagement.getInstance().requestMe(object : MeResponseCallback() {
                override fun onSuccess(result: UserProfile) {

                    var imagePath = ""
                    if(result.thumbnailImagePath != null) {
                        imagePath = result.thumbnailImagePath
                        Util.setThumbnail(imagePath)
                    }
                    goMainActivity(result.id, result.nickname, imagePath)
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {}
                override fun onNotSignedUp() {}
            })
        } else {
            // 세션 없을 때
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

//        val status = sharedPrefs.getInt(resources.getString(R.string.status), 0)
//        if(status == 0) {
//            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        } else {
//            when(status) {
//                1 -> { // Kakao auto login
//
//                }
//                2 -> { // Facebook auto login
//
//                }
//                3 -> { // Google auto login
//
//                }
//                else -> {
//                    Util.toast(this, resources.getString(R.string.toast_invalid))
//                }
//            }
//        }

    }

    private fun goMainActivity(id: Long, userNickname: String, imagePath: String) {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)

        intent.putExtra(resources.getString(R.string.id_k), id)
        intent.putExtra(resources.getString(R.string.nick_k), userNickname)

        intent.putExtra(resources.getString(R.string.thumb_k), imagePath)

        Util.setID(id.toString())
        Util.setNickname(userNickname)

        startActivity(intent)
        finish()
    }
}