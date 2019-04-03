package com.example.rahul.redpill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView positive_listView;
    ListView negative_listView;
    ArrayList<String> positiveUrl;
    ArrayList<String> positiveHead;
    ArrayList<String> negativeUrl;
    ArrayList<String> negativeHead;

    String[] positiveArray = {};
    String[] negativeArray = {};
    EditText queryEditText;
    Button queryButton;
    ProgressBar progressBar;

    ArrayAdapter positive_adapter;
    ArrayAdapter negative_adapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        positiveUrl = new ArrayList<String>();
        positiveHead = new ArrayList<String>();
        negativeUrl = new ArrayList<String>();
        negativeHead = new ArrayList<String>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        queryEditText = (EditText) findViewById(R.id.query_editText);
        queryButton = (Button) findViewById(R.id.query_Button);

        requestQueue = Volley.newRequestQueue(this);
        positive_adapter = new ArrayAdapter<String>(this, R.layout.activity_news_text_view, positiveArray);


        positive_listView = (ListView) findViewById(R.id.positive_listView);
        positive_listView.setAdapter(positive_adapter);

        negative_listView = (ListView) findViewById(R.id.negative_listView);
        negative_listView.setAdapter(negative_adapter);

        progressBar.setVisibility(View.INVISIBLE);

        positive_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(MainActivity.this, Webpage.class);
                myIntent.putExtra("address", positiveUrl.get(i));
                MainActivity.this.startActivity(myIntent);
            }
        });

        negative_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(MainActivity.this, Webpage.class);
                myIntent.putExtra("address", negativeUrl.get(i));
                MainActivity.this.startActivity(myIntent);
            }
        });

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = queryEditText.getText().toString();
                query = query.replace(' ', '_');
                String url = "http://redpill.eu-gb.mybluemix.net/show/" + query + "/";
                progressBar.setVisibility(View.VISIBLE);
                sendjsonrequest(url);
            }
        });



    }

    public void sendjsonrequest(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    positiveHead.clear();
                    positiveUrl.clear();
                    negativeHead.clear();
                    negativeUrl.clear();

                    JSONArray positive = response.getJSONArray("positive");
                    JSONArray negative = response.getJSONArray("negative");
                    for(int i = 0; i < positive.length(); i++)
                    {
                        JSONObject cur = positive.getJSONObject(i);
                        positiveUrl.add(cur.getString("url"));
                        positiveHead.add(cur.getString("title"));
                    }
                    for(int i = 0; i < negative.length(); i++)
                    {
                        JSONObject cur = negative.getJSONObject(i);
                        negativeUrl.add(cur.getString("url"));
                        negativeHead.add(cur.getString("title"));
                    }


                    showNews();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }
        );

        requestQueue.add(jsonObjectRequest);
    }


    public void showNews()
    {
        progressBar.setVisibility(View.INVISIBLE);
        String[] positive = new String[positiveHead.size()];
        for(int i = 0; i < positiveHead.size(); i++)
            positive[i] = positiveHead.get(i).toString();

        String[] negative = new String[negativeHead.size()];
        for(int i = 0; i < negativeHead.size(); i++)
            negative[i] = negativeHead.get(i).toString();

        positive_adapter = new ArrayAdapter<String>(this, R.layout.activity_news_text_view, positive);
        positive_listView.setAdapter(positive_adapter);

        negative_adapter = new ArrayAdapter<String>(this, R.layout.activity_news_text_view_negative, negative);
        negative_listView.setAdapter(negative_adapter);
    }
}
