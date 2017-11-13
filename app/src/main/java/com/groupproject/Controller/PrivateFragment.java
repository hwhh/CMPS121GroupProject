package com.groupproject.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupproject.R;

/**
 * Created by haileypun on 12/11/2017.
 */

public class PrivateFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.private_fragment, container, false);

        return rootView;
    }

}
