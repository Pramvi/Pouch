package com.pramvi.pouch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pramvi.pouch.Model.ContactModel;
import com.pramvi.pouch.Utils.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String id;
    RealmConfiguration realmConfig;
    Realm realm;

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


        realmConfig = new RealmConfiguration.Builder(getActivity()).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        final RealmResults<ContactModel> contacts=realm.where(ContactModel.class).equalTo("id",1).findAll();

        contacts.size();

        return view;
    }
}

