package com.team.elysium.moneyfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.team.elysium.moneyfit.thread.HttpRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MoneyManagingDialog extends Dialog implements View.OnClickListener{

    private Context context;
    Button buy;
    Button cancel;


    TextView moneyname;
    TextView much;
    TextView contents;
    ImageView moneyview;

    private ImageView buyButton;

    // private int price;
    private String Itemid;
    private MoneyManagingActivity.DialogCallback dialogCallback;

    public MoneyManagingDialog(Context context, String Itemid, MoneyManagingActivity.DialogCallback dialogCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.context = context;
        // this.price = price;
        this.dialogCallback = dialogCallback;

        this.Itemid =Itemid;
    }


    //onemore time chcek
    private void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage("정말 구매하시겠습니까 ?").setCancelable(
                false).setPositiveButton("Yes",
                (dialog, id) -> {
                    //결제
                    HttpRequest.getInstance().requestBuy(context, "803592488", Itemid, buyCallback);
                }).setNegativeButton("No",
                (dialog, id) -> {
                    // Action for 'NO' Button
                    dialog.cancel();
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Title");
        // Icon for AlertDialog
        alert.show();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_managing);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        initListener();

        switch (Itemid) {
            case "15215621":
                GlideApp.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.manage_money1000))
                        .centerCrop()
                        .into(moneyview);
                contents.setText("문화상품권 1000원권의 대한 내용");
                moneyname.setText("문화상품권 1000");
                much.setText("1300");

                break;

            case "35453217":
                GlideApp.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.manage_money3000))
                        .centerCrop()
                        .into(moneyview);
                contents.setText("문화상품권 3000원권의 대한 내용");
                moneyname.setText("문화상품권 3000");
                much.setText("3900");
                break;

            case "55458484":
                GlideApp.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.manage_money5000))
                        .centerCrop()
                        .into(moneyview);
                contents.setText("문화상품권 5000원권의 대한 내용");
                moneyname.setText("문화상품권 5000");
                much.setText("6500");
                break;
            case "19545641":
                GlideApp.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.manage_money10000))
                        .centerCrop()
                        .into(moneyview);
                contents.setText("문화상품권 10000원권의 대한 내용");
                moneyname.setText("문화상품권 10000");
                much.setText("13000");
                break;
        }

    }

    private void initListener() {
        cancel.setOnClickListener(this);
        buy.setOnClickListener(this);

    }

    private void initView() {

        moneyname= findViewById(R.id.moneyname);
        much= findViewById(R.id.much);
        contents= findViewById(R.id.contents);
        buy =findViewById(R.id.buy);
        cancel = findViewById(R.id.cancel);
        moneyview = findViewById(R.id.moneyview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                cancel();
                break;
            case R.id.buy:
                //HttpRequest.getInstance().requestBuy(context, "803592488", Itemid, buyCallback);

                DialogSimple();

                break;
        }
    }









    private Callback buyCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, @NonNull Response response) throws IOException {

            final String result = response.body().string();




            //   Log.e("반응", result);

            if(context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> {
                    dialogCallback.onFinished(result);
                    dismiss();
                });
            } else {
                Log.e("ㅇㅇㅇ", "액티비티x");
            }
        }
    };
}
