package com.unosquare.webservicesapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;


public class MyActivity extends Activity {
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initUI();
    }

    private void initUI(){

        hideProgressBar();

        findViewById(R.id.button_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                    @Override
                    public void onTaskCompleted(String response) {
                        //Toast.makeText(MyActivity.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                        parseResponse(response);

                    }
                    @Override
                    public void onTaskError(String error) {
                        Toast.makeText(MyActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                    }
                });

                ntserv.setProgressBarBack(new FunctionProgressBar() {
                    @Override
                    public void showProgress() {
                        showProgressBar();
                    }

                    @Override
                    public void hideProgress() {
                        hideProgressBar();
                    }
                });

                ntserv.execute(NetServices.WS_CALL_GET);

            }

            private void parseResponse(String response) {
                Gson gson = new Gson();
                ModelGet model = gson.fromJson(response, ModelGet.class);
                //String code = model.getQuery().getResults().getChannel().getItem().getForecast()[0].getCode();
                //Toast.makeText(MyActivity.this, "Code: "+code, Toast.LENGTH_SHORT).show();
                ForecastAdapter adapter = new ForecastAdapter(MyActivity.this, model.getQuery().getResults().getChannel().getItem().getForecast());
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
            }
        });

        findViewById(R.id.button_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                    @Override
                    public void onTaskCompleted(String response) {
                        Toast.makeText(MyActivity.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTaskError(String error) {
                        Toast.makeText(MyActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                    }
                });

                ntserv.setProgressBarBack(new FunctionProgressBar() {
                    @Override
                    public void showProgress() {
                        showProgressBar();
                    }

                    @Override
                    public void hideProgress() {
                        hideProgressBar();
                    }
                });

                ntserv.execute(NetServices.WS_CALL_POST);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}
