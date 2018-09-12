package com.nazran.filtering;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText nameOrNumber;
    private ListView contactListView;
    private ContactListAdapter contactListAdapter;
    private Context context = MainActivity.this;
    private ArrayList<ModelContact> modelContacts = new ArrayList<>();

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
        dataReady();

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

    private void dataReady() {
        modelContacts.add(new ModelContact("Isa vai", "111133311221"));
        modelContacts.add(new ModelContact("Saad vai", "2222222222222"));
        modelContacts.add(new ModelContact("Dip vai", "33322223113"));
        modelContacts.add(new ModelContact("Sourav vai", "444111443334"));
        modelContacts.add(new ModelContact("Sad Dark Vai", "55225555"));
        modelContacts.add(new ModelContact("Nazran", "01714516763"));
        modelContacts.add(new ModelContact("Alamgir vai", "777337744"));
    }
}
