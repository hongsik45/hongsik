package com.team.elysium.moneyfit;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.elysium.moneyfit.thread.HttpRequest;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.elysium.moneyfit.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by sh on 2018-08-10.
 */

public class MoneyManagingActivity extends AppCompatActivity {

    MoneyManagingActivity cd4;

    private TextView currentMoney;

    interface DialogCallback {
        void onFinished(String money);
    }

    private com.team.elysium.moneyfit.MoneyManagingActivity.DialogCallback dialogCallback = new com.team.elysium.moneyfit.MoneyManagingActivity.DialogCallback() {
        @Override
        public void onFinished(String money) {

            if (money.equals("nomoney") ) {

                Toast.makeText(com.team.elysium.moneyfit.MoneyManagingActivity.this,"돈이 부족합니다",Toast.LENGTH_LONG).show();
                currentMoney.setText(Util.getUserMoney());

            } else {

                Log.e("반응", money);
                currentMoney.setText(money);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_money_managing);

        ImageView moneyh = findViewById(R.id.moneyh);  //이미지둥글게하기위해
        ImageView moneyth = findViewById(R.id.moneyth);
        ImageView moneyfh = findViewById(R.id.moneyfh);
        ImageView moneytenh = findViewById(R.id.moneytenh);
        currentMoney = findViewById(R.id.currentmoney);
        GlideApp.with(this)
                .load(ContextCompat.getDrawable(this, R.drawable.manage_money1000))
                .centerCrop()
                .into(moneyh);
        GlideApp.with(this)
                .load(ContextCompat.getDrawable(this, R.drawable.manage_money3000))
                .centerCrop()
                .into(moneyth);
        GlideApp.with(this)
                .load(ContextCompat.getDrawable(this, R.drawable.manage_money5000))
                .centerCrop()
                .into(moneyfh);


        GlideApp.with(this)
                .load(ContextCompat.getDrawable(this, R.drawable.manage_money10000))
                .centerCrop()
                .into(moneytenh);



        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        LinearLayout money1000 = (LinearLayout) findViewById(R.id.money1000);


        Button withdraw = (Button) findViewById(R.id.withdraw);//이미지뷰~

        LinearLayout money5000 = (LinearLayout) findViewById(R.id.money5000);//이미지뷰~

        LinearLayout money10000 = (LinearLayout) findViewById(R.id.money10000);//이미지뷰~

        LinearLayout money3000 = (LinearLayout) findViewById(R.id.money3000);//이미지뷰~

        money1000.setOnClickListener(v -> new com.team.elysium.moneyfit.MoneyManagingDialog(MoneyManagingActivity.this, "15215621", dialogCallback).show());
        money3000.setOnClickListener(v -> new com.team.elysium.moneyfit.MoneyManagingDialog(MoneyManagingActivity.this, "35453217", dialogCallback).show());
        money5000.setOnClickListener(v -> new com.team.elysium.moneyfit.MoneyManagingDialog(MoneyManagingActivity.this, "55458484" ,dialogCallback).show());
        money10000.setOnClickListener(v -> new com.team.elysium.moneyfit.MoneyManagingDialog(MoneyManagingActivity.this, "19545641", dialogCallback).show());


        HttpRequest.getInstance().requestMoneyData(MoneyManagingActivity.this, "803592488", moneyDataCallback);

    }

    private Callback moneyDataCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            final String result = response.body().string();

            if(result=="Restricted"){


            }else {
                final Integer money = Integer.valueOf(result);

                runOnUiThread(() -> {

                    currentMoney.setText(String.valueOf(money));
                    Util.setUserMoney(result);
                });
            }
        }
    };
}
