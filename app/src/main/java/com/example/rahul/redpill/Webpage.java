package com.example.rahul.redpill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class Webpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        WebView myWebView = (WebView) findViewById(R.id.webView);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("address");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("address");
        }


        myWebView.loadUrl(newString);
    }
}
