package com.team.elysium.moneyfit

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by sh on 2018-07-07.
 */
class LoginActivity : AppCompatActivity() {

    private var callback: SessionCallback? = null
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_login)

        try {
            val info = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("MY KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

        progressBar = findViewById(R.id.login_progress_bar)

        val image = findViewById<ImageView>(R.id.coin_img)
        Glide.with(App.getGlobalApplicationContext())
                .load(ContextCompat.getDrawable(App.getGlobalApplicationContext(), R.drawable.moneyfit_coin_img))
                .into(image)


        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun goMainActivity(id: Long, userNickname: String, imagePath: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)

        intent.putExtra(resources.getString(R.string.id_k), id)
        intent.putExtra(resources.getString(R.string.nick_k), userNickname)
        intent.putExtra(resources.getString(R.string.thumb_k), imagePath)
        intent.putExtra(resources.getString(R.string.first_execution), resources.getString(R.string.start))

        Util.setID(id.toString())
        Util.setNickname(userNickname)
        //intent.putExtra("thumbnail", userThumbnail)
        startActivity(intent)
        finish()
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {

            progressBar.visibility = View.VISIBLE
            UserManagement.getInstance().requestMe(object : MeResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {

                    Log.e("failure", errorResult!!.errorMessage)
                }

                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.errorMessage)
                }

                override fun onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp")
                }

                override fun onSuccess(userProfile: UserProfile) {

                    Log.e("UserProfile", userProfile.toString())
                    Log.e("UserProfile", userProfile.id.toString() + "")

                    var imagePath = ""
                    if(userProfile.thumbnailImagePath != null) {
                        imagePath = userProfile.thumbnailImagePath
                        Util.setThumbnail(imagePath)
                    }

                    goMainActivity(userProfile.id, userProfile.nickname, imagePath)

                    finish()
                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {

            if (exception != null) {
                Log.d("exception", exception.message)

                //Logger.e(exception);
            }
        }
    }
}