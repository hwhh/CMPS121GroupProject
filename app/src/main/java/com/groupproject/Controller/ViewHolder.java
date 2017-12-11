package com.groupproject.Controller;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.groupproject.R;


public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView vName;
        public CardView cardView;
        public ImageButton interact;
        public CheckBox selected;

        public ViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            cardView = v.findViewById(R.id.card_view);
            interact = v.findViewById(R.id.interact);
            selected = v.findViewById(R.id.selected);
        }

}

