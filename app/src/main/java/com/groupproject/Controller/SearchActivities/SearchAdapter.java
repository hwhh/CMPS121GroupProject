package com.groupproject.Controller.SearchActivities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;
import java.util.List;

import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;


public class SearchAdapter extends FirebaseRecyclerAdapter<DataBaseItem, ViewHolder> implements DataBaseCallBacks<String> {


    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private final Fragment fragment;

    public SearchAdapter(Query query, Fragment fragment) {
        super(new FirebaseRecyclerOptions.Builder<DataBaseItem>().setQuery(query, DataBaseItem.class).build());
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DataBaseItem model) {
//        holder.cardView.setOnClickListener(v -> {
//            if (fragment.getSearchType() == SearchFragment.SearchType.USERS)
//            if (fragment.getSearchType() == SearchFragment.SearchType.EVENTS){}
//            if (fragment.getSearchType() == SearchFragment.SearchType.GROUPS){}
//        });


        holder.vName.setText(model.getName());
        if (((SearchFragment) fragment).getSearchType() == SearchType.Type.USERS) {
            dataBaseAPI.getUser(model.getId(), this, holder);
        }if (((SearchFragment) fragment).getSearchType() == SearchType.Type.EVENTS) {
//            createEvent(model, holder);
        }if (((SearchFragment) fragment).getSearchType() == SearchType.Type.GROUPS) {


        }
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        DataBaseAPI.UserRelationship userRelationship = dataBaseAPI.getRelationShip(user);
        if(userRelationship == ME) {
            holder.interact.setImageDrawable(null);
        } else if(userRelationship == FRIENDS) {
            holder.interact.setImageDrawable(null); //TODO go to profile to remove
        } else if(userRelationship == REQUESTED) {
            holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.cancel_button));
        }else if(userRelationship == NONE) {
            holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.add_button));
        }
        holder.interact.setOnClickListener(view -> {
            if(userRelationship == FRIENDS) {
                dataBaseAPI.removeFriend(user); //TODO go to profile to remove
            } else if(userRelationship == REQUESTED) {
                dataBaseAPI.cancelRequest(user);
            }else if(userRelationship == NONE) {
                dataBaseAPI.sendFriendRequest(user);
            }
        });
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {

    }

    @Override
    public void createUserList(List<User> userList) {

    }


}
