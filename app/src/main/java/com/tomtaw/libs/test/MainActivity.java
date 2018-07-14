package com.tomtaw.libs.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickStickFrame(View view) {
        startActivity(new Intent(this, StickFrameLayoutActivity.class));
    }

    public void clickSwipeLayout(View view) {
        startActivity(new Intent(this, SuggestTemplateActivity.class));
    }
}
