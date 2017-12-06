package com.groupproject.Controller.SearchActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.groupproject.Controller.EventActivities.EventInfoActivity;
import com.groupproject.Controller.GroupActivities.ViewGroupActivity;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;
import com.groupproject.Controller.ViewProfileActivity;

import java.util.List;

import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.HIDDEN;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.INVITED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.JOINED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.PUBLIC;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;


public class SearchAdapter extends FirebaseRecyclerAdapter<DataBaseItem, ViewHolder> implements DataBaseCallBacks<String> {


    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private final Fragment fragment;

    SearchAdapter(Query query, Fragment fragment) {
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
        holder.selected.setVisibility(View.GONE);
        if (((SearchFragment) fragment).getSearchType() == SearchType.Type.USERS) {
            dataBaseAPI.getUser(model.getId(), this, holder);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), ViewProfileActivity.class);
                intent.putExtra("key", model.getId());
                fragment.getActivity().startActivity(intent);
            });
        }else if (((SearchFragment) fragment).getSearchType() == SearchType.Type.EVENTS) {
            dataBaseAPI.getEvent(model.getId(), this, holder);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), EventInfoActivity.class);
                intent.putExtra("key", model.getId());
                fragment.getActivity().startActivity(intent);
            });
        }else if (((SearchFragment) fragment).getSearchType() == SearchType.Type.GROUPS) {
            dataBaseAPI.getGroup(model.getId(), this, holder);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), ViewGroupActivity.class);
                intent.putExtra("key", model.getId());
                fragment.getActivity().startActivity(intent);
            });
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
                dataBaseAPI.cancelFriendRequest(user);
            }else if(userRelationship == NONE) {
                dataBaseAPI.sendFriendRequest(user);
            }
        });
        holder.vName.setText(user.getName());
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        DataBaseAPI.STATUS status = dataBaseAPI.getEventRelationShip(event);
        if(status == HIDDEN) {
            holder.cardView.setVisibility(View.GONE);
        } else if(status == JOINED) {
            holder.interact.setImageDrawable(null);
        } else if(status == INVITED) {
            holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.button_add));
        }else if(status == PUBLIC) {
            holder.interact.setImageDrawable(null);
        }
        holder.interact.setOnClickListener(view -> {
            if(status == INVITED) {
                dataBaseAPI.acceptEventInvite(event);
            }
        });
        holder.vName.setText(event.getName());
    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {
        DataBaseAPI.STATUS status = dataBaseAPI.getGroupRelationShip(group);
        if(status == HIDDEN) {
            holder.cardView.setVisibility(View.GONE);
        } else if(status == JOINED) {
            holder.interact.setImageDrawable(null);
        } else if(status == INVITED) {
            holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.button_add));
        }else if(status == PUBLIC) {
            holder.interact.setImageDrawable(null);
        }
        holder.interact.setOnClickListener(view -> {
            if(status == INVITED) {
                dataBaseAPI.acceptGroupInvite(group);
            }
        });
        holder.vName.setText(group.getName());
    }



    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {

    }




}
