package com.nazran.filtering;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText nameOrNumber;
    private ListView contactListView;
    private ContactListAdapter contactListAdapter;
    private Context context = MainActivity.this;
    private ArrayList<ModelContact> modelContacts = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        checkContactPermission();

        startService(new Intent(MainActivity.this, MyService.class));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void checkContactPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_CONTACTS
                }, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        nameOrNumber = findViewById(R.id.nameOrNumber);
        contactListView = findViewById(R.id.contactListView);

        contactListAdapter = new ContactListAdapter(context, modelContacts);
        contactListView.setAdapter(contactListAdapter);
        //dataReady();

        nameOrNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListAdapter.getFilter().filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        Log.e("onFilterComplete", "" + count);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your Read Contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void dataReady() {
        modelContacts.add(new ModelContact("Isa vai", "111133311221"));
        modelContacts.add(new ModelContact("Saad vai", "2222222222222"));
        modelContacts.add(new ModelContact("Dip vai", "33322223113"));
        modelContacts.add(new ModelContact("Sourav vai", "444111443334"));
        modelContacts.add(new ModelContact("Sad Dark Vai", "55225555"));
        modelContacts.add(new ModelContact("Nazran", "01714516763"));
        modelContacts.add(new ModelContact("Alamgir vai", "777337744"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("name"));
            Log.e(TAG, intent.getStringExtra("phone"));

            modelContacts.add(new ModelContact(
                    intent.getStringExtra("name"),
                    intent.getStringExtra("phone")
            ));
            contactListAdapter.notifyDataSetChanged();
        }
    };
}
