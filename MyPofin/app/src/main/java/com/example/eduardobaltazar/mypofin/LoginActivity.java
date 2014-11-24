package com.example.eduardobaltazar.mypofin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by admin on 19/10/2014.
 */
public class LoginActivity extends Activity {
    EditText edit_email;
    EditText edit_passw;
    Button btnLogin;
    Button btnRegister;
    CheckBox chbxInfoLogin;
    private SessionVariables sessionVariables;
    private LoginOperations loginOperations;
    UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Calling Application class
        sessionVariables = (SessionVariables) getApplicationContext();
        sessionVariables.setPointsList(new ArrayList<PointModel>());
        sessionVariables.setUser_id("");
        sessionVariables.setName("");

        //Open database
        loginOperations = new LoginOperations(this);
        loginOperations.open();

        initUI();
        setClick();
        getInfoLogin();
    }

    private void initUI() {
        hideProgressBar();

        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_passw = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        chbxInfoLogin = (CheckBox) findViewById(R.id.checkBox_remember);
        getActionBar().setSubtitle(getResources().getString(R.string.title_activity_login));

    }

    private void getInfoLogin() {
        currentUser = new UserModel();
        if (currentUser!=null) {
            currentUser = loginOperations.getUser();
            if (currentUser.getEmail()!=null) {
                edit_email.setText(currentUser.getEmail());
                edit_passw.setText(currentUser.getPassword());
                chbxInfoLogin.setChecked(true);
            }

        }
    }

    private void setClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = edit_email.getText().toString();
                String text_password = edit_passw.getText().toString();

                if (text_email != null && text_email.length() > 0) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(text_email).matches()) {
                        if (text_password != null && text_password.length() > 0) {
                            UserModel usmod = new UserModel();
                            usmod.setEmail(text_email);
                            usmod.setPassword(text_password);

                            NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                                @Override
                                public void onTaskCompleted(String response) {
                                    //Toast.makeText(MyActivity.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                                    parseResponseLogin(response);
                                }
                                @Override
                                public void onTaskError(String error) {
                                    Toast.makeText(LoginActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
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

                            ntserv.execute(NetServices.WS_CALL_GET_LOGIN);

                        } else {
                            edit_passw.setError(getResources().getString(R.string.gral_error_msg));
                        }
                    } else {
                        edit_email.setError(getResources().getString(R.string.email_format_error));
                    }
                } else {
                    edit_email.setError(getResources().getString(R.string.gral_error_msg));
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_register));
                startActivity(intent);
            }
        });

    }

    private void parseResponseLogin(String response) {
        Gson gson = new Gson();
        UserModel model = gson.fromJson(response, UserModel.class);
        if (model.getEmail()!=null && model.getPassword()!=null) {
            //put info in session variables
            sessionVariables.setUser_id(model.getId());
            sessionVariables.setName(model.getName());

            //if checkbox is checked then save the info
            if (chbxInfoLogin.isChecked()) {
                loginOperations.deleteUser();
                loginOperations.insertUser(model.getEmail(), model.getPassword());
            } else {
                loginOperations.deleteUser();
            }

            Toast.makeText(LoginActivity.this, "Welcome: "+sessionVariables.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PrincipalActivity.class);
            //intent.putExtra("user_id", model.getId());

            startActivity(intent);
            finish();
        } else {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(LoginActivity.this, "Name: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar_login).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar_login).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        loginOperations.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        loginOperations.close();
        super.onPause();
    }
}
