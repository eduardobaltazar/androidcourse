package com.unosquare.actionbarnavigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends Activity {
    EditText edit;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();
        setClick();
    }

    private void initUI() {
        edit = (EditText) findViewById(R.id.editT);
        btnFinish = (Button) findViewById(R.id.finishT);
    }

    private void setClick() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit.getText().toString();

                if (text != null && text.length() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("title", text);
                    setResult(500, intent);
                    finish();
                } else {
                    edit.setError("Error");
                }
            }
        });
    }


}
