package com.unosquare.finalapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by admin on 19/10/2014.
 */
public class PrincipalActivity extends Activity {
    private ListView list;
    private GridView grid;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private BothViewsFragment bothviewsf;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        initUI();
        setAdapter();
        setFragments();
        setClicks();
        setToggle();
    }

    private void initUI() {
        list = (ListView) findViewById(R.id.left_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setFragments() {
        manager = getFragmentManager();
        bothviewsf = new BothViewsFragment();
    }

    private void setAdapter() {
        DrawerAdapter adapter = new DrawerAdapter(this);
        list.setAdapter(adapter);
    }

    private void setToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer, R.string.action_settings, R.string.action_settings);
        drawer.setDrawerListener(mDrawerToggle);
        getActionBar().setTitle(getResources().getString(R.string.principal_title));
        getActionBar().setSubtitle(getResources().getString(R.string.principal_subtitle));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setClicks() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(PrincipalActivity.this, "Menu item:"+DrawerAdapter.menu[position],Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                String menustrvalue = ""+DrawerAdapter.menu[position];
                switch (position) {
                    case 0:
                        manager.beginTransaction().replace(R.id.content_frame, bothviewsf.getInstance(R.color.white, menustrvalue), "ListViewFragm").commit();
                        getActionBar().setTitle(menustrvalue);
                        break;
                    case 1:
                        manager.beginTransaction().replace(R.id.content_frame, bothviewsf.getInstance(R.color.white, menustrvalue), "GridViewFragm").commit();
                        getActionBar().setTitle(menustrvalue);
                        break;
                    case 2:
                        manager.beginTransaction().replace(R.id.content_frame, bothviewsf.getInstance(R.color.white, menustrvalue), "BothViesFragm").commit();
                        getActionBar().setTitle(menustrvalue);
                        break;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerToggle.syncState();
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
            case R.id.menu_settings:
                getWebServicesResult(getResources().getString(R.string.menu_navbar_opc1));
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc1));
            break;

            case R.id.menu_help:
                Intent intentH = new Intent(PrincipalActivity.this, AboutActivity.class);
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc2));
                startActivity(intentH);
                break;

            case R.id.menu_return:
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc5));
                moveTaskToBack(true);
                PrincipalActivity.this.finish();
                break;

            case R.id.menu_replace:
                getWebServicesResult(getResources().getString(R.string.menu_navbar_opc3));
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc3));
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void getWebServicesResult(String section_name) {
        Toast.makeText(this, "Click "+section_name, Toast.LENGTH_SHORT).show();
        NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
            @Override
            public void onTaskCompleted(String response) {
                //Toast.makeText(MyActivity.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                parseResponse(response);

            }
            @Override
            public void onTaskError(String error) {
                Toast.makeText(PrincipalActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        ntserv.execute(NetServices.WS_CALL_GET);

    }

    private void parseResponse(String response) {
        Gson gson = new Gson();
        ModelGet model = gson.fromJson(response, ModelGet.class);

        ForecastAdapter adapter = new ForecastAdapter(PrincipalActivity.this, model.getQuery().getResults().getChannel().getItem().getForecast());

        if (manager.findFragmentByTag("ListViewFragm") != null) {
            if (manager.findFragmentByTag("ListViewFragm").isVisible()) {
                list = (ListView) findViewById(R.id.listViewBoth_frg);
                list.setAdapter(adapter);

                grid = (GridView) findViewById(R.id.gridViewBoth_frg);
                grid.setVisibility(View.GONE);
            }
        }

        if (manager.findFragmentByTag("GridViewFragm") != null) {
            if (manager.findFragmentByTag("GridViewFragm").isVisible()) {
                list = (ListView) findViewById(R.id.listViewBoth_frg);
                list.setVisibility(View.GONE);

                grid = (GridView) findViewById(R.id.gridViewBoth_frg);
                grid.setAdapter(adapter);
            }
        }

        if (manager.findFragmentByTag("BothViesFragm") != null) {
            if (manager.findFragmentByTag("BothViesFragm").isVisible()) {
                list = (ListView) findViewById(R.id.listViewBoth_frg);
                list.setAdapter(adapter);

                grid = (GridView) findViewById(R.id.gridViewBoth_frg);
                grid.setAdapter(adapter);
            }
        }

    }

}
