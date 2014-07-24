package me.biubiubiu.justifytext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ccheng on 7/24/14.
 */
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void onDisplayChinese(View view) {
        Intent intent = new Intent(this, TextActivity.class);
        intent.putExtra(TextActivity.KEY_FILE_NAME, "1.txt");
        startActivity(intent);
    }

    public void onDisplayEnglish(View view) {
        Intent intent = new Intent(this, TextActivity.class);
        intent.putExtra(TextActivity.KEY_FILE_NAME, "1en.txt");
        startActivity(intent);
    }
}
