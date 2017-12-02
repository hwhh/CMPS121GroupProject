package com.groupproject.Controller;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.groupproject.Controller.MapActivites.MapsFragment;
import com.groupproject.Controller.SearchActivities.SearchFragment;
import com.groupproject.R;


public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.SwitchFragment {

    private InputMethodManager imm;

    FragmentManager fragmentManager = getSupportFragmentManager();
    SearchFragment searchFrag = SearchFragment.newInstance();
    Fragment mapsFrag = MapsFragment.newInstance();
    private SearchView searchView;

    boolean exited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imm = (InputMethodManager) getApplication().getSystemService(BaseActivity.INPUT_METHOD_SERVICE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.dashboard_content, mapsFrag);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.findFragmentByTag("maps") == null) {
            fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
            if (searchView != null) {
                searchView.setQuery("", true);
                searchView.clearFocus();
            }
            exited = true;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();



        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b && !exited)  //check if just closed
                    fragmentManager.beginTransaction().replace(R.id.dashboard_content, searchFrag, "search").commit();
                else if(!b && exited)
                    fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
                else if(b && exited){
                    fragmentManager.beginTransaction().replace(R.id.dashboard_content, searchFrag, "search").commit();

                }
                else
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                exited = false;
            }
        });


        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", true);
                fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchFrag.setQ(s.toLowerCase());
                return false;
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.friends) {


        } else if (id == R.id.groups) {

        } else if (id == R.id.events) {

        } else if (id == R.id.sign_out) {

        } else if (id == R.id.delete_accout) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void switchFragment(Fragment frag, Bundle args) {
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        frag.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.dashboard_content, frag, "profile")
                .commit();
        exited = true;

    }
}
