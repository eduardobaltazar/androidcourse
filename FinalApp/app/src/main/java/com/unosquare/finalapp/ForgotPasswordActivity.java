package com.unosquare.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by admin on 19/10/2014.
 */
public class ForgotPasswordActivity extends Activity {
    EditText edit_email;
    Button btn_forgotpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        initUI();
        setClick();

    }

    private void initUI() {
        edit_email = (EditText) findViewById(R.id.edit_email);
        btn_forgotpsw = (Button) findViewById(R.id.btn_forgotpsw);
    }

    private void setClick() {
        btn_forgotpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = edit_email.getText().toString();

                if (text_email != null && text_email.length() > 0) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(text_email).matches()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Correcto" , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("txt_email", text_email);
                        setResult(201, intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.email_format_error), Toast.LENGTH_LONG).show();
                    }
                } else {
                    edit_email.setError("Error");
                }
            }
        });
    }

}
