package com.groupproject.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupproject.Controller.MapActivites.MapsFragment;
import com.groupproject.Controller.SearchActivities.SearchFragment;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.SideBarActivities.SidebarFragment;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;


public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnCreateContextMenuListener,
        DataBaseCallBacks<User> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private InputMethodManager imm;

    FragmentManager fragmentManager = getSupportFragmentManager();
    SearchFragment searchFrag = new SearchFragment();
    SidebarFragment sidebarFragment = new SidebarFragment();
    Fragment mapsFrag = MapsFragment.newInstance();
    private SearchView searchView;
    private Fragment currentFragment;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    TextView userProfileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBaseAPI.getUser(dataBaseAPI.getCurrentUserID(), this, null);

        setContentView(R.layout.navigation_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imm = (InputMethodManager) getApplication().getSystemService(BaseActivity.INPUT_METHOD_SERVICE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.dashboard_content, mapsFrag, "maps");
        currentFragment = mapsFrag;
        ft.commit();
    }


    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        actionBarDrawerToggle.onConfigurationChanged(configuration);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment != mapsFrag) {
            fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
            currentFragment = mapsFrag;
            if (searchView != null) {
                searchView.setQuery("", true);
                searchView.clearFocus();
            }
        } else {
            super.onBackPressed();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.clearFocus();
//        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchText.setOnClickListener(listener);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);

        myActionMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                if(currentFragment == mapsFrag) {
                    fragmentManager.beginTransaction().replace(R.id.dashboard_content, searchFrag, "search").commit();
                    currentFragment = searchFrag;
                }
                searchView.findFocus();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
                currentFragment = mapsFrag;
                searchView.setIconified(true);
                searchView.clearFocus();
                currentFragment = mapsFrag;
                return true;
            }
        });


        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            searchView.setQuery("", true);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            fragmentManager.beginTransaction().replace(R.id.dashboard_content, mapsFrag, "maps").remove(searchFrag).commitAllowingStateLoss();
            currentFragment = mapsFrag;
            searchView.clearFocus();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //TODO Download list if filtering
                if(currentFragment == searchFrag)
                    searchFrag.setQ(s.toLowerCase());
                else if(currentFragment == sidebarFragment){
                    sidebarFragment.filterResults(s.toLowerCase());
                }
                return false;
            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        sidebarFragment = new SidebarFragment();
        if (id == R.id.friends) {
            bundle.putString("type", "friend");
        }else if (id == R.id.events) {
            bundle.putString("type", "events");
        } else if (id == R.id.groups) {
            bundle.putString("type", "groups");
        }else if (id == R.id.notifications) {
            bundle.putString("type", "notifications");
        } else if (id == R.id.sign_out) {
            dataBaseAPI.signOut(this);
            return true;
        }
        sidebarFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(currentFragment.getId(), sidebarFragment, "sidebar").remove(searchFrag).commitAllowingStateLoss();
        currentFragment = sidebarFragment;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onCreateContextMenu(ContextMenu var1, View var2, ContextMenu.ContextMenuInfo var3){
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void getUser(User user, ViewHolder holder) {
        userProfileName = findViewById(R.id.userProfile);
        userProfileName.setText(user.getName());
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<User> result, SearchType.Type type) {

    }
}
