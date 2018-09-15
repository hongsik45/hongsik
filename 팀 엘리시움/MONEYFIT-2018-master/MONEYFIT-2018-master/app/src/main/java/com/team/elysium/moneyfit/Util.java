package com.team.elysium.moneyfit;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.Random;

import okhttp3.MediaType;

/**
 * Created by sh on 2018-07-12.
 */

public class Util {

    public static void toast(Context context, String message) { Toast.makeText(context, message, Toast.LENGTH_LONG).show();}

    public static boolean isNetworkConnected() {
        ConnectivityManager c = (ConnectivityManager) App.getGlobalApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);

        if(c != null) {
            NetworkInfo info = c.getActiveNetworkInfo();
            if(info != null && info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }

        return false;
    }

    public static String getRandomString(int range) {
        StringBuilder temp = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < range; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }

        return temp.toString();
    }

    public static Activity getActivityForCurrentContext(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return getActivityForCurrentContext(((ContextWrapper)cont).getBaseContext());

        return null;
    }

    private static String ID = "";
    private static String NICKNAME;
    private static String THUMBNAIL;
    private static String RECOMMENDATION_CODE;

    private static String MONEY;

    private static String MEASUREMENT_FOLDER = "";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType PLAIN_TEXT = MediaType.parse("text/plain; charset=utf-8");

    public static void setID(String id) {ID = id;}
    public static String getID() { return ID; }
    public static void setNickname(String nickname) {NICKNAME = nickname;}
    public static String getNickname() { return NICKNAME; }
    public static void setThumbnail(String path) {THUMBNAIL = path;}
    public static String getThumbnail() {return THUMBNAIL;}
    public static void setRecommendationCode(String code) {RECOMMENDATION_CODE = code;}
    public static String getRecommendationCode() {return RECOMMENDATION_CODE;}
    public static String getUserMoney() {return MONEY;}
    public static void setUserMoney(String MONEY) {Util.MONEY = MONEY;}

    public static String getMeasurementFolder() {return MEASUREMENT_FOLDER;}

    public static void setMeasurementFolder(String measurementFolder) {MEASUREMENT_FOLDER = measurementFolder;}
}
