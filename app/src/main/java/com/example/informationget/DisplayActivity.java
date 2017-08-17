package com.example.informationget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by glenn_cui on 17-8-11.
 */
public class DisplayActivity extends AppCompatActivity{

    public static final String INFO_URL ="info_url";
    public static final String INFO_TYPE = "info_type";
    private String TAG ="gtest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        Intent intent =getIntent();
        String url = intent.getStringExtra(INFO_URL);
        String type =intent.getStringExtra(INFO_TYPE);

        Log.d(TAG, "onCreate: type is "+type);
        toolbar.setTitle(type);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        WebView webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            default:
                break;

        }
        return true;

    }
}
