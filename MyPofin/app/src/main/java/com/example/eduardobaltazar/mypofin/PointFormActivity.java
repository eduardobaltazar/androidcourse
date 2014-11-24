package com.example.eduardobaltazar.mypofin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by eduardo.baltazar on 12/11/2014.
 */
public class PointFormActivity extends Activity {
    EditText edit_title;
    EditText edit_latitude;
    EditText edit_longitude;
    EditText edit_comment;
    Button btnSave;
    Button btnCancel;
    private String point_id;
    private String latitude;
    private String longitude;
    private PointModel pmEdit;
    private SessionVariables sessionVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_point);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();
        getInfo();
        setClick();
    }

    private void initUI() {
        hideProgressBar();

        edit_title = (EditText) findViewById(R.id.edit_title_point_form);
        edit_latitude = (EditText) findViewById(R.id.edit_latitude_point_form);
        edit_longitude = (EditText) findViewById(R.id.edit_longitude_point_form);
        edit_comment = (EditText) findViewById(R.id.edit_comment_point_form);
        btnSave = (Button) findViewById(R.id.btn_save_point_form);
        btnCancel = (Button) findViewById(R.id.btn_cancel_point_form);
        getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_point_form));
        edit_latitude.setEnabled(false);
        edit_longitude.setEnabled(false);
        pmEdit = new PointModel();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            point_id = extras.getString("point_id");
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");

            edit_latitude.setText(latitude);
            edit_longitude.setText(longitude);
        }

        // Calling Application class
        sessionVariables = (SessionVariables) getApplicationContext();
    }

    private void getInfo() {
        if (point_id!=null && !point_id.isEmpty()) {
            PointModel pm = new PointModel();
            pm.setId(point_id);
            NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                @Override
                public void onTaskCompleted(String response) {
                    parseResponsePoint(response);
                }

                @Override
                public void onTaskError(String error) {
                    Toast.makeText(PointFormActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }, null, pm, "");

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

            ntserv.execute(NetServices.WS_CALL_GET_POINT);
        }
    }

    private void setClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavUtils.navigateUpFromSameTask(PointFormActivity.this);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorflag = false;
                String text_title = edit_title.getText().toString();
                String text_latitude = edit_latitude.getText().toString();
                String text_longitude = edit_longitude.getText().toString();
                String text_comment = edit_comment.getText().toString();

                if (text_title != null && text_title.length() > 0) {
                } else {
                    edit_title.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (text_latitude != null && text_latitude.length() > 0) {
                } else {
                    edit_latitude.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (text_longitude != null && text_longitude.length() > 0) {
                } else {
                    edit_longitude.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (text_comment != null && text_comment.length() > 0) {
                } else {
                    edit_comment.setError(getResources().getString(R.string.gral_error_msg));
                    errorflag = true;
                }

                if (!errorflag) {
                    pmEdit.setUser_id(sessionVariables.getUser_id());
                    pmEdit.setTitle(text_title);
                    pmEdit.setLatitude(text_latitude);
                    pmEdit.setLongitude(text_longitude);
                    pmEdit.setComment(text_comment);
                    final String process;
                    if (pmEdit.getId()!=null && !pmEdit.getId().isEmpty()) {
                        process = NetServices.WS_CALL_PROCESS_UPDATE;
                    } else {
                        process = NetServices.WS_CALL_PROCESS_INSERT;
                    }
                    NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                        @Override
                        public void onTaskCompleted(String response) {
                            parseResponseUpPoint(response);
                            sessionVariables.setPointsList(new ArrayList<PointModel>());
                            if (process == NetServices.WS_CALL_PROCESS_UPDATE) {
                                NavUtils.navigateUpFromSameTask(PointFormActivity.this);
                            } else {
                                NavUtils.navigateUpTo(PointFormActivity.this, new Intent(PointFormActivity.this, PrincipalActivity.class));
                            }
                        }

                        @Override
                        public void onTaskError(String error) {
                            Toast.makeText(PointFormActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }, null, pmEdit, process);

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

                    ntserv.execute(NetServices.WS_CALL_POST_POINT);
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

    private void parseResponsePoint(String response) {
        Gson gson = new Gson();
        try {
            pmEdit = gson.fromJson(response, PointModel.class);
            if (pmEdit!=null && !pmEdit.getTitle().isEmpty()) {
                setInfoForm();
            } else {
                MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
                Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void parseResponseUpPoint(String response) {
        Gson gson = new Gson();
        try {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void setInfoForm() {
        edit_title.setText(pmEdit.getTitle());
        edit_latitude.setText(pmEdit.getLatitude());
        edit_longitude.setText(pmEdit.getLongitude());
        edit_comment.setText(pmEdit.getComment());
    }

    private void showProgressBar() {
        findViewById(R.id.progressBar_Form).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar_Form).setVisibility(View.GONE);
    }
}
