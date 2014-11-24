package com.example.eduardobaltazar.mypofin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by eduardo.baltazar on 12/11/2014.
 */
public class RegisterActivity extends Activity {
    EditText edit_email;
    EditText edit_passw;
    EditText edit_name;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();
        setClick();
    }

    private void initUI() {
        hideProgressBar();

        edit_email = (EditText) findViewById(R.id.edit_email_form);
        edit_passw = (EditText) findViewById(R.id.edit_password_form);
        edit_name = (EditText) findViewById(R.id.edit_name_form);
        btnRegister = (Button) findViewById(R.id.btn_register_form);
        getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_register));
    }

    private void setClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean errorflag = false;
                String text_email = edit_email.getText().toString();
                String text_password = edit_passw.getText().toString();
                String text_name = edit_name.getText().toString();

                if (text_name != null && text_name.length() > 0) {
                } else {
                    edit_name.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (text_email != null && text_email.length() > 0) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(text_email).matches()) {
                    } else {
                        edit_email.setError(getResources().getString(R.string.email_format_error));
                        errorflag = true;
                    }
                } else {
                    edit_email.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (text_password != null && text_password.length() > 0) {
                } else {
                    edit_passw.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (!errorflag) {
                    UserModel usmod = new UserModel();
                    usmod.setEmail(text_email);
                    usmod.setPassword(text_password);
                    usmod.setName(text_name);

                    NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                        @Override
                        public void onTaskCompleted(String response) {
                            parseResponseRegister(response);
                            if (response.indexOf("Error") <= -1) {
                                NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                            }
                        }
                        @Override
                        public void onTaskError(String error) {
                            Toast.makeText(RegisterActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
                        }
                    }, usmod, null, "");

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

                    ntserv.execute(NetServices.WS_CALL_POST_REGISTER);
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void parseResponseRegister(String response) {
        Gson gson = new Gson();
        try {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar_Register).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar_Register).setVisibility(View.GONE);
    }
}
