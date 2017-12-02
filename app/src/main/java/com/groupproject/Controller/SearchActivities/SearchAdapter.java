package com.groupproject.Controller.SearchActivities;




import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Controller.SideBarActivities.GroupsFragment;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.User;
import com.groupproject.R;

import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;


public class SearchAdapter extends FirebaseRecyclerAdapter<DataBaseItem, SearchAdapter.ViewHolder> {


    private final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private final Fragment fragment;

    public SearchAdapter(Query query, Fragment fragment) {
        super(new FirebaseRecyclerOptions.Builder<DataBaseItem>().setQuery(query, DataBaseItem.class).build());
        this.fragment = fragment;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.search_result, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DataBaseItem model) {


//        holder.cardView.setOnClickListener(v -> {
//            if (fragment.getSearchType() == SearchFragment.SearchType.USERS)
//            if (fragment.getSearchType() == SearchFragment.SearchType.EVENTS){}
//            if (fragment.getSearchType() == SearchFragment.SearchType.GROUPS){}
//        });

        if(fragment instanceof SearchFragment) {
            holder.vName.setText(model.getName());
            if (((SearchFragment) fragment).getSearchType() == SearchType.Type.USERS)
                createUserDrawable(model, holder);
            if (((SearchFragment) fragment).getSearchType() == SearchType.Type.EVENTS) {

            }
        }else if(fragment instanceof GroupsFragment){
            holder.vName.setText(model.getName());
        }


    }


    private void createUserDrawable(DataBaseItem model, final ViewHolder holder){
        Query getUser = DataBaseAPI.getDataBase().getmUserRef().child(model.getId());
        getUser.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       User user = dataSnapshot.getValue(com.groupproject.Model.User.class);
                       DataBaseAPI.UserRelationship userRelationship = dataBaseAPI.getRelationShip(user);
                       if(userRelationship == ME) {
                           holder.interact.setVisibility(View.GONE);
                       } else if(userRelationship == FRIENDS) {
                           holder.interact.setVisibility(View.GONE);//TODO Add remove button to profile
                           holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.com_facebook_button_icon));
                       } else if(userRelationship == REQUESTED) {
                           holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.common_google_signin_btn_icon_dark_focused));
                       }else if(userRelationship == NONE) {
                           holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.deleteaccount));
                       }
                       holder.interact.setOnClickListener(view -> {
                           if(userRelationship == FRIENDS) {
                                dataBaseAPI.removeFriend(user);
                           } else if(userRelationship == REQUESTED) {
                               dataBaseAPI.cancelRequest(user);
                           }else if(userRelationship == NONE) {
                               dataBaseAPI.sendFriendRequest(user);
                           }
                       });
                   }
               }
               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView vName;
        CardView cardView;
        FloatingActionButton interact;

        ViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            cardView = v.findViewById(R.id.card_view);
            interact = v.findViewById(R.id.interactFloatingButton);
        }

    }

}
