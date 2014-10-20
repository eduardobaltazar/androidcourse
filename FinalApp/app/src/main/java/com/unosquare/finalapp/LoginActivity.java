package com.unosquare.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 19/10/2014.
 */
public class LoginActivity extends Activity {
    EditText edit_username;
    EditText edit_passw;
    Button btnLogin;
    TextView linkForgotP;
    TextView lblemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setClick();

    }

    private void initUI() {
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_passw = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        linkForgotP = (TextView) findViewById(R.id.link_forgotpsw);
        lblemail = (TextView) findViewById(R.id.lbl_email);
    }

    private void setClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_username = edit_username.getText().toString();
                String text_password = edit_passw.getText().toString();

                if (text_username != null && text_username.length() > 0) {
                    if (text_password != null && text_password.length() > 0) {
                        if (text_username.equals(getResources().getString(R.string.username_value)) && text_password.equals(getResources().getString(R.string.password_value))) {
                            Toast.makeText(LoginActivity.this, "Access", Toast.LENGTH_LONG).show();
                            Intent intentP = new Intent(LoginActivity.this, PrincipalActivity.class);
                            startActivity(intentP);
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_match_error), Toast.LENGTH_LONG).show();
                            edit_username.setText("");
                            edit_passw.setText("");
                        }
                    } else {
                        edit_passw.setError(getResources().getString(R.string.gral_error_msg));
                    }
                } else {
                    edit_username.setError(getResources().getString(R.string.gral_error_msg));
                }

            }
        });

        linkForgotP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentH = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivityForResult(intentH, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode) {
            case 201:
                lblemail.setText((String) data.getExtras().get("txt_email"));
                lblemail.setTextColor(getResources().getColor(R.color.blue));
                break;
        }

        //Toast.makeText(this, "subtitle: "+subtitle, Toast.LENGTH_SHORT).show();
    }
}
