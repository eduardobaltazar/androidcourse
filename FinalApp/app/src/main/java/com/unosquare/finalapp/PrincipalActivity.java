package com.unosquare.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

/**
 * Created by admin on 19/10/2014.
 */
public class PrincipalActivity extends Activity implements PrincipalFragment.OnFragmentInteractionListener {
    private ListView list;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        initUI();
        setAdapter();
        //setClicks();
        setToggle();
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
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.ic_drawer, R.string.action_settings, R.string.action_settings);
        drawer.setDrawerListener(mDrawerToggle);
        getActionBar().setTitle(getResources().getString(R.string.principal_title));
        getActionBar().setSubtitle(getResources().getString(R.string.principal_subtitle));
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

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onFragmentInteraction(String texto) {
        getActionBar().setSubtitle(texto);
    }
}
