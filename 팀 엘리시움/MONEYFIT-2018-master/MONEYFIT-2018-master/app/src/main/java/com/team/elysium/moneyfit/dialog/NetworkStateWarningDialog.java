package com.team.elysium.moneyfit.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;

/**
 * Created by sh on 2018-07-13.
 */

public class NetworkStateWarningDialog extends AppCompatDialog {

    public NetworkStateWarningDialog(Context context) {
        super(context);
    }

    public NetworkStateWarningDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
