package com.oiosark07.echowidget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public final class OpenEchoActivity extends Activity {
    private static final Uri ECHO_URL = Uri.parse("https://oiosark07.github.io/echo-phone/?v=5");

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Intent open = new Intent(Intent.ACTION_VIEW, ECHO_URL);
        open.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(open);
        finish();
    }
}
