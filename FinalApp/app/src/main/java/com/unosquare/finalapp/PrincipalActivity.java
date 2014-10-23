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
    private int addCount;

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
                //Toast.makeText(PrincipalActivity.this, "Menu item:"+DrawerAdapter.menu[position],Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                String menustrvalue = ""+DrawerAdapter.menu[position];
                getActionBar().setSubtitle(getResources().getString(R.string.principal_subtitle));

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
                getWebServicesResult(getResources().getString(R.string.menu_navbar_opc1), false);
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc1));
            break;

            case R.id.menu_help:
                manageAddCount(false);
                Intent intentH = new Intent(PrincipalActivity.this, AboutActivity.class);
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc2));
                startActivity(intentH);
                break;

            case R.id.menu_return:
                manageAddCount(false);
                Toast.makeText(PrincipalActivity.this, getResources().getString(R.string.menu_navbar_opc5), Toast.LENGTH_SHORT).show();
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc5));
                moveTaskToBack(true);
                PrincipalActivity.this.finish();
                break;

            case R.id.menu_replace:
                getWebServicesResult(getResources().getString(R.string.menu_navbar_opc3), false);
                getActionBar().setSubtitle(getResources().getString(R.string.menu_navbar_opc3));
                break;

            case R.id.menu_add:
                getWebServicesResult(getResources().getString(R.string.menu_navbar_opc4), true);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void getWebServicesResult(String section_name, boolean addCount) {
        //Toast.makeText(this, "Click "+section_name, Toast.LENGTH_SHORT).show();

        if (manager.findFragmentByTag("ListViewFragm") != null || manager.findFragmentByTag("GridViewFragm") != null || manager.findFragmentByTag("BothViesFragm") != null) {
            findViewById(R.id.listViewBoth_frg).setVisibility(View.GONE);
            findViewById(R.id.gridViewBoth_frg).setVisibility(View.GONE);

            manageAddCount(addCount); //if true, increment addCount else set it 0

            NetServices ntserv = new NetServices(new OnBackgroundTaskCallback() {
                @Override
                public void onTaskCompleted(String response) {
                    //Toast.makeText(MyActivity.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                    parseResponse(response);

                }

                @Override
                public void onTaskError(String error) {
                    Toast.makeText(PrincipalActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
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

    }

    private void parseResponse(String response) {
        Gson gson = new Gson();
        ModelGet model = gson.fromJson(response, ModelGet.class);

        ForecastAdapter adapter = new ForecastAdapter(PrincipalActivity.this, model.getQuery().getResults().getChannel().getItem().getForecast(), addCount);

        if (manager.findFragmentByTag("ListViewFragm") != null) {
            if (manager.findFragmentByTag("ListViewFragm").isVisible()) {
                list = (ListView) findViewById(R.id.listViewBoth_frg);
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);

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
                grid.setVisibility(View.VISIBLE);
            }
        }

        if (manager.findFragmentByTag("BothViesFragm") != null) {
            if (manager.findFragmentByTag("BothViesFragm").isVisible()) {
                list = (ListView) findViewById(R.id.listViewBoth_frg);
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);

                grid = (GridView) findViewById(R.id.gridViewBoth_frg);
                grid.setAdapter(adapter);
                grid.setVisibility(View.VISIBLE);
            }
        }

    }

    private void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void manageAddCount(boolean opc) {
        if (opc) {
            addCount++;
        } else {
            addCount = 0;
        }
    }
}
