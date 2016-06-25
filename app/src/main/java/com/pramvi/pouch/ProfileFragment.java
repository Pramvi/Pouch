package com.pramvi.pouch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pramvi.pouch.Utils.Constants;


public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String id;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
        id=sharedPreferences.getString(Constants.USERNAME,"9");

        return view;
    }
}

