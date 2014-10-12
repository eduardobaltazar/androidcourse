package com.unosquare.actionbarnavigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

public class ActionAndDrawerActivity extends Activity implements MyFragment.OnFragmentInteractionListener {

    private ListView list;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
   // private Intent intent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		initUI();
        setAdapter();
        setClicks();
        setToggle();
	}

    private void initUI() {
        list = (ListView) findViewById(R.id.left_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //intent = new Intent(ActionAndDrawerActivity.this, MyActivity.class);
	}

    private void setAdapter() {
        DrawerAdapter adapter = new DrawerAdapter(this);
        list.setAdapter(adapter);
    }

    private void setClicks() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ActionAndDrawerActivity.this, "Menu item:"+DrawerAdapter.menu[position],Toast.LENGTH_LONG).show();
                drawer.closeDrawers();
                String menustrvalue = ""+DrawerAdapter.menu[position];
                switch (position) {
                    case 0:
                        addFragment(R.color.lime, menustrvalue);
                        getActionBar().setTitle(menustrvalue);
                        break;
                    case 1:
                        addFragment(R.color.blue, menustrvalue);
                        getActionBar().setTitle(menustrvalue);
                        break;
                    case 2:
                        addFragment(R.color.yellow, menustrvalue);
                        getActionBar().setTitle(menustrvalue);
                        break;
                    default:
                        addFragment(R.color.gray, menustrvalue);
                        getActionBar().setTitle(menustrvalue);
                        break;
                }

            }
        });
    }

    private void addFragment(int color, String subt) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_frame, MyFragment.getInstance(color, subt)).commit();

    }

    private void setToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer, R.string.action_settings, R.string.action_settings);
        drawer.setDrawerListener(mDrawerToggle);
        getActionBar().setTitle("Principal");
        getActionBar().setSubtitle("SubPrin");
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.menu_help:
                Toast.makeText(this, "Click Help", Toast.LENGTH_LONG).show();
                //addFragment(R.color.maroon, "Help");
                //startActivity(intent);
                Intent intentH = new Intent(this, HelpActivity.class);
                startActivityForResult(intentH, 200);
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Click Settings", Toast.LENGTH_LONG).show();
                //addFragment(R.color.navy, "Settings");
                Intent intentS = new Intent(this, SettingsActivity.class);
                startActivityForResult(intentS, 300);
                break;
            case R.id.menu_othersettings:
                Toast.makeText(this, "Click Other Settings", Toast.LENGTH_LONG).show();
                addFragment(R.color.lime, "Other Settings");
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onFragmentInteraction(String texto) {
        getActionBar().setSubtitle(texto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode) {
            case 400:
                String subtitle = (String) data.getExtras().get("subtitle");
                getActionBar().setSubtitle(subtitle);
                break;
            case 500:
                String title = (String) data.getExtras().get("title");
                getActionBar().setTitle(title);
                break;

        }

        //Toast.makeText(this, "subtitle: "+subtitle, Toast.LENGTH_SHORT).show();
    }
}