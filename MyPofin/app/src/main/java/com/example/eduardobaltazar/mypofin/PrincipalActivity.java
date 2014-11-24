package com.example.eduardobaltazar.mypofin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
    private ListView list;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private GoogleMap mMap;
    double latitude = 20.7027285;
    double longitude = -103.3757878;
    int zoom = 13;
    //String user_id = "-1";
    private AppLocationService appLocationService;
    private SessionVariables sessionVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Calling Application class
        sessionVariables = (SessionVariables) getApplicationContext();

        initUI();
        setAdapter();
        setToggle();
        getCurrentLocation();
        setUpMapIfNeeded();
        getPointsList();
        setClicks();
    }

    private void initUI() {
        list = (ListView) findViewById(R.id.left_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setAdapter() {
        DrawerAdapter adapter = new DrawerAdapter(this);
        list.setAdapter(adapter);
    }

    private void setToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.dw_menu_open, R.string.dw_menu_close);
        drawer.setDrawerListener(mDrawerToggle);
        getActionBar().setTitle(getResources().getString(R.string.app_title));
        getActionBar().setSubtitle(getResources().getString(R.string.app_subtitle));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getParameters() {
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");
        }*/
        //Toast.makeText(this, "user_id: "+user_id, Toast.LENGTH_SHORT).show();

        /* get the points list to store in session variable */
        //getPointsList();

    }

    private void getPointsList() {
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
                        Toast.makeText(PrincipalActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }, usmod, null, "");

                ntserv.execute(NetServices.WS_CALL_GET_POINTSLIST);
            }
        } else {
            setListMarkers();
        }

    }

    private void parseResponsePointsList(String response) {
        Gson gson = new Gson();
        try {
            ArrayList<PointModel> model = gson.fromJson(response, new TypeToken<List<PointModel>>(){}.getType());
            if (model!=null && !model.isEmpty()) {
                sessionVariables.setPointsList(model);
                setListMarkers();
            } else {
                MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
                Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            MsgModel modelmsg = gson.fromJson(response, MsgModel.class);
            Toast.makeText(this, "Msg: "+modelmsg.getMsg(), Toast.LENGTH_LONG).show();
        }

    }

    private void setListMarkers() {
        ArrayList<PointModel> pointList = sessionVariables.getPointsList();
        if (pointList!=null && !pointList.isEmpty()) {
            LatLng latlng;
            for (PointModel tempMk : pointList) {
                latlng = new LatLng(Double.parseDouble(tempMk.getLatitude()), Double.parseDouble(tempMk.getLongitude()));
                mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(tempMk.getTitle())
                                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_marker_list_b)
                        ));
            }
        }

    }

    private void setClicks() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                drawer.closeDrawers();
                switch (position) {
                    case 0:
                        Intent intentPL = new Intent(PrincipalActivity.this, PointsListActivity.class);
                        getActionBar().setSubtitle(getResources().getString(R.string.subtitle_activity_points_list));
                        startActivity(intentPL);
                        break;
                    case 1:
                        newMarkerPoint();
                        break;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerToggle.syncState();
        getCurrentLocation();
        setUpMapIfNeeded();
        getPointsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId()) {
            case R.id.menu_about:
                Intent intentH = new Intent(PrincipalActivity.this, AboutActivity.class);
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc1));
                startActivity(intentH);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void newMarkerPoint() {
        LatLng latlng = new LatLng(latitude-0.0004900, longitude-0.0000300);
        mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("New Marker")
                .snippet("Drag the marker to the new position")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_new_marker)
                ));
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(latlng);

        mMap.moveCamera(center);
    }

    @Override
    public void onMapClick(LatLng arg0) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
    }



    @Override
    public void onMarkerDrag(Marker arg0) {
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        //create new marker when user long clicks
        mMap.addMarker(new MarkerOptions()
                .position(arg0)
                .title("New Marker")
                .snippet("Drag the marker to the new position")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_new_marker)
                ));
    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        /*LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        Toast.makeText(this, "on drag end :" + dragLat + " dragLong :" + dragLong, Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        if(arg0.getTitle().equals("New Marker")) {
            LatLng posNew = arg0.getPosition();
            final double coordLat = posNew.latitude;
            final double coordLong = posNew.longitude;
            //Toast.makeText(this, "on click :" + coordLat + " dragLong :" + coordLong, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.txt_dialog_save);
            alertDialogBuilder.setPositiveButton(R.string.btn_ok_point_lbl,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentPF = new Intent(PrincipalActivity.this, PointFormActivity.class);
                            intentPF.putExtra("latitude", Double.toString(coordLat));
                            intentPF.putExtra("longitude", Double.toString(coordLong));
                            startActivity(intentPF);
                            dialog.dismiss();

                        }
                    });
            alertDialogBuilder.setNegativeButton(R.string.btn_cancel_point_lbl,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {
            arg0.showInfoWindow();
        }
        return true;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng latlng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("I am here!")
                .snippet("We are here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_marker_b)
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                ));
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(latlng);
        CameraUpdate czoom= CameraUpdateFactory.zoomTo(zoom);

        mMap.moveCamera(center);
        mMap.animateCamera(czoom);
    }

    private void getCurrentLocation() {
        appLocationService = new AppLocationService(this);
        Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        if (nwLocation!=null) {
            latitude = nwLocation.getLatitude();
            longitude = nwLocation.getLongitude();
        } else if (gpsLocation != null) {
            latitude = gpsLocation.getLatitude();
            longitude = gpsLocation.getLongitude();
        } else {
            showSettingsAlert("GPS");
        }

    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        PrincipalActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


}
