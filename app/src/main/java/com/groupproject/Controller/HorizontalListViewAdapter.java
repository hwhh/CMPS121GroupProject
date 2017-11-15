package com.groupproject.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.groupproject.R;

/**
 * Created by haileypun on 12/11/2017.
 */

public class HorizontalListViewAdapter extends RecyclerView.Adapter<HorizontalListViewAdapter.ViewHolder> {

    Context context;

    public HorizontalListViewAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        viewHolder.tvSpecies.setText(alName.get(i));
//        viewHolder.imgThumbnail.setImageResource(alImage.get(i));

//        viewHolder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if (isLongClick) {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
//                } else {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageButton imageButton;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = (ImageButton)itemView.findViewById(R.id.imageButton);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

}
