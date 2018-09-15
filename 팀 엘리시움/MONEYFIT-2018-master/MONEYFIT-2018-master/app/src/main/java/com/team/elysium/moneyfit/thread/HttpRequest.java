package com.team.elysium.moneyfit.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.team.elysium.moneyfit.App;
import com.team.elysium.moneyfit.Camera2BasicFragment;
import com.team.elysium.moneyfit.R;
import com.team.elysium.moneyfit.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sh on 2018-07-14.
 */

public class HttpRequest {

    private OkHttpClient client;
    private static HttpRequest instance = new HttpRequest();
    public static HttpRequest getInstance() {
        return instance;
    }

    private ByteArrayOutputStream bos;

    private HttpRequest() {
        this.client = new OkHttpClient();
        this.bos = new ByteArrayOutputStream();
    }

    public void registerUser(String id, String nickname, String thumbnail, String key, Callback callback) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        Log.e("레지스터", "유저");
        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);
        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.nick_k), nickname);
        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.thumb_k), thumbnail);
        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.allow), key);

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.register_user_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void getEndDate(String id, Callback callback) {
        JSONObject jsonObject = new JSONObject();

        Log.e("아이디~", id);
        try {
            jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.end_date_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void getWeekData(String id, String key, Callback callback) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);
        jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.allow), key);

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.week_data_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void getRankData(String id, Callback callback) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);

            RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(App.getGlobalApplicationContext().getResources().getString(R.string.rank_data_url))
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(String id, byte[] bytes, int number, String outputFolder, boolean isFirstCapture) throws JSONException, IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);

        //bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);

        //byte[] bytes = bos.toByteArray();
        //bos.reset();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("title", "review");
        String userFile = "userfile[]";

        builder.addFormDataPart(userFile, outputFolder, RequestBody.create(Util.JSON, jsonObject.toString()));
        builder.addFormDataPart(userFile, number + ".jpeg", RequestBody.create(Util.PLAIN_TEXT, bytes));
        if(isFirstCapture)
            builder.addFormDataPart(userFile, "isFirst", RequestBody.create(Util.PLAIN_TEXT, "1"));
        RequestBody body = builder.build();

        //RequestBody body = RequestBody.create(Util.PLAIN_TEXT, bytes);

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.image_handling_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(imageHandlingCallback);
    }

    public void requestBuy(Context context, String id, String itemId, Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(context.getResources().getString(R.string.mangement_buy_id), id);
            jsonObject.put(context.getResources().getString(R.string.mangement_buy_item_id), itemId);
            //jsonObject.put(context.getResources().getString(R.string.buy_price), price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(context.getResources().getString(R.string.mangement_buy_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void requestMoneyData(Context context, String id, Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(context.getResources().getString(R.string.mangement_buy_id), id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(context.getResources().getString(R.string.money_data_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void alertMeasurementFinished(String id, String exerciseCode, Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);
            jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.code), exerciseCode);
            //jsonObject.put(context.getResources().getString(R.string.buy_price), price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.alert_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void checkIsAnalyzed(String id, Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(App.getGlobalApplicationContext().getResources().getString(R.string.id_k), id);
            //jsonObject.put(context.getResources().getString(R.string.buy_price), price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(Util.JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(App.getGlobalApplicationContext().getResources().getString(R.string.check_url))
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    private Callback imageHandlingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }


        @Override
        public void onResponse(Call call, @NonNull Response response) throws IOException {

            String result = response.body().string();

            Log.e("이미지핸들 결과", result);
//
//            // 폴더 리턴
//            Camera2BasicFragment.Companion.setIMAGE_FOLDER(result);
//
//            Log.e("바꾼것", Camera2BasicFragment.Companion.getIMAGE_FOLDER());
        }
    };

}
