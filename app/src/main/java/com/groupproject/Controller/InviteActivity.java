package com.groupproject.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.SideBarActivities.SidebarAdapter;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;
import java.util.Set;

public class InviteActivity extends AppCompatActivity implements SearchType, DataBaseCallBacks<String> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private SidebarAdapter mSearchAdapter;
    private InputMethodManager imm;
    private String id;
    private String type;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        type = b.getString("type");
        id = b.getString("id");

        setContentView(R.layout.activity_invite);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        imm = (InputMethodManager) getApplication().getSystemService(BaseActivity.INPUT_METHOD_SERVICE);

        Query query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("friendsIDs");
        dataBaseAPI.executeQuery(query, this, Type.USERS);
        mSearchAdapter = new SidebarAdapter(null, Type.INVITE, id);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView mRecyclerView = findViewById(R.id.friends_invite);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.swapAdapter(mSearchAdapter, true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invite_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSearchAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                Set<String> invited = mSearchAdapter.getInvited();
                Set<String> uninvited = mSearchAdapter.getUnivited();
                for (String userID : invited) {
                    if(type.equals("group")){
                        dataBaseAPI.sendGroupInvite(userID, id);
                    }else if(type.equals("event")){
                        dataBaseAPI.sendEventInvite(userID, id);
                    }
                }
                for (String userID : uninvited) {
                    if(type.equals("event")){
                        dataBaseAPI.cancelEventInvite(userID, id);
                    }else if(type.equals("group")){
                        dataBaseAPI.cancelGroupInvite(userID, id);
                    }
                }

                Toast.makeText(this, "Invites sent", Toast.LENGTH_LONG).show();
                super.onBackPressed();
                return true;
            case android.R.id.home:
                super.onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        mSearchAdapter.getItems().add(user);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {


    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {
    }

    @Override
    public void executeQuery(List<String> result, Type type) {
        for (String s : result) {
            if(type == Type.USERS)
                dataBaseAPI.getUser(s, this, null);
            else if(type == Type.EVENTS)
                dataBaseAPI.getEvent(s, this, null);
            else if(type == Type.GROUPS)
                dataBaseAPI.getGroup(s, this, null);
        }
    }
}
