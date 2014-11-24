package com.example.eduardobaltazar.mypofin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PointsListActivity extends Activity {
    private SessionVariables sessionVariables;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_points);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Calling Application class
        sessionVariables = (SessionVariables) getApplicationContext();

        initUI();
    }

    private void initUI() {
        getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_points_list));

        if (sessionVariables.getPointsList()==null || sessionVariables.getPointsList().isEmpty()) {
            if (!sessionVariables.getUser_id().equals("")) {
                UserModel usmod = new UserModel();
                usmod.setId(sessionVariables.getUser_id());
                NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                    @Override
                    public void onTaskCompleted(String response) {
                        parseResponsePointsList(response);
                    }

                    @Override
                    public void onTaskError(String error) {
                        Toast.makeText(PointsListActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }, usmod, null, "");

                ntserv.execute(NetServices.WS_CALL_GET_POINTSLIST);
            }
        } else {
            setListAdapater();
        }


    }

    private void parseResponsePointsList(String response) {
        Gson gson = new Gson();
        try {
            ArrayList<PointModel> model = gson.fromJson(response, new TypeToken<List<PointModel>>(){}.getType());
            if (model!=null && !model.isEmpty()) {
                sessionVariables.setPointsList(model);
                setListAdapater();
            } else {
                MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
                Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }

    }

    private void setListAdapater() {
        if (sessionVariables.getPointsList()!=null) {
            final PointsListAdapter adapter = new PointsListAdapter(PointsListActivity.this, sessionVariables.getPointsList());
            list = (ListView) findViewById(R.id.listViewPoints);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    createDialog(adapter, position, parent.getContext());
                }

            });
        }
    }

    private void createDialog(PointsListAdapter adapter, int position, Context context){
        final PointModel pm = (PointModel)adapter.getItem(position);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_point);
        //dialog.setTitle(pm.getTitle());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView txt_title_value = (TextView) dialog.findViewById(R.id.dtl_txt_title);
        //TextView txt_latitude_value = (TextView) dialog.findViewById(R.id.dtl_txt_latitude);
        //TextView txt_longitude_value = (TextView) dialog.findViewById(R.id.dtl_txt_longitude);
        TextView txt_comment_value = (TextView) dialog.findViewById(R.id.dtl_txt_comment);
        txt_title_value.setText(pm.getTitle());
        //txt_latitude_value.setText(pm.getLatitude());
        //txt_longitude_value.setText(pm.getLongitude());
        txt_comment_value.setText(pm.getComment());

        Button DButton_cancel = (Button) dialog.findViewById(R.id.btn_point_cancel);
        Button DButton_edit = (Button) dialog.findViewById(R.id.btn_point_edit);
        Button DButton_delete = (Button) dialog.findViewById(R.id.btn_point_delete);

        DButton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        DButton_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPF = new Intent(PointsListActivity.this, PointFormActivity.class);
                getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_point_form));
                intentPF.putExtra("point_id", pm.getId());
                startActivity(intentPF);
                dialog.dismiss();
            }
        });

        DButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog_delete = new AlertDialog.Builder(PointsListActivity.this);
                alertDialog_delete.setTitle(R.string.title_dialog_delete);
                alertDialog_delete.setMessage(R.string.delete_question);
                alertDialog_delete.setIcon(R.drawable.ic_action_discard);

                /* delete point */
                alertDialog_delete.setPositiveButton(R.string.btn_yes_point_lbl,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogA, int which) {
                                NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                                    @Override
                                    public void onTaskCompleted(String response) {
                                        parseResponseDelete(response);
                                        sessionVariables.setPointsList(new ArrayList<PointModel>());
                                        dialog.dismiss();
                                        //NavUtils.navigateUpFromSameTask(PointsListActivity.this);
                                        recreate();
                                    }

                                    @Override
                                    public void onTaskError(String error) {
                                        Toast.makeText(PointsListActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    }
                                }, null, pm, NetServices.WS_CALL_PROCESS_DELETE);

                                ntserv.execute(NetServices.WS_CALL_POST_POINT);
                            }
                        });

                /* no delete point */
                alertDialog_delete.setNegativeButton(R.string.btn_no_point_lbl,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog_delete.show();
            }
        });

        dialog.show();
    }

    private void parseResponseDelete(String response) {
        Gson gson = new Gson();
        try {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
