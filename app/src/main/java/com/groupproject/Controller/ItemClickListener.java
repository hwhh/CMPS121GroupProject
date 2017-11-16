package com.groupproject.Controller;

/**
 * Created by haileypun on 12/11/2017.
 */
import android.view.View;

public interface ItemClickListener {

    void onClick(View view, int position, boolean isLongClick);
}