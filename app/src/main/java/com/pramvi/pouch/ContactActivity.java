package com.pramvi.pouch;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.pramvi.pouch.Model.ContactModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ContactActivity extends AppCompatActivity {


    private Realm realm;
    private RealmConfiguration realmConfig;
    String firstName,lastName,emailId,address,mobileNo,webSite,companyName;
    EditText firstNameView,lastNameView,mobileNoView,addressView,webSiteView,companyNameView,emailIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstNameView=(EditText) findViewById(R.id.firstname);
        lastNameView=(EditText) findViewById(R.id.LastName);
        addressView=(EditText) findViewById(R.id.address);
        mobileNoView=(EditText) findViewById(R.id.mobile);
        emailIdView=(EditText) findViewById(R.id.emailid);
        //webSiteView=(EditText) findViewById(R.id.we);
        //companyNameView=(EditText) findViewById(R.id.firstname);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    void save()
    {

        realmConfig = new RealmConfiguration.Builder(this).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                ContactModel contactModel = realm.createObject(ContactModel.class);
                contactModel.setFirstName(firstName);
                contactModel.setLastName(lastName);
                contactModel.setAddress(address);
                contactModel.setEmailId(emailId);
                contactModel.setMobileNo(mobileNo);
                contactModel.setCompanyName(companyName);
                contactModel.setWebsite(webSite);


            }
        });
    }
}


