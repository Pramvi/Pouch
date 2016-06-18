package com.pramvi.pouch;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pramvi.pouch.Model.ContactModel;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ContactActivity extends AppCompatActivity {


    private Realm realm;
    private RealmConfiguration realmConfig;
    Button saveButton;
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
        webSiteView=(EditText) findViewById(R.id.website);
        companyNameView=(EditText) findViewById(R.id.company);
        saveButton=(Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = firstNameView.getText().toString();
                lastName=lastNameView.getText().toString();
                address=addressView.getText().toString();
                emailId=emailIdView.getText().toString();
                mobileNo=mobileNoView.getText().toString();
                companyName=companyNameView.getText().toString();
                webSite=webSiteView.getText().toString();

                ContactModel contactModel = new ContactModel();
                contactModel.setFirstName(firstName);
                contactModel.setLastName(lastName);
                contactModel.setAddress(address);
                contactModel.setEmailId(emailId);
                contactModel.setMobileNo(mobileNo);
                contactModel.setCompanyName(companyName);
                contactModel.setWebsite(webSite);
                save(contactModel);
            }
        });
    }

    void save(final ContactModel contactModel)
    {

        realmConfig = new RealmConfiguration.Builder(this).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // realm.copyToRealm(obj)
                realm.copyToRealmOrUpdate(contactModel);
            }
        });
    }
}


