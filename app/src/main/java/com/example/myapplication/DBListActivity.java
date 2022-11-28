package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.DBAdapter;
import com.example.myapplication.listadapter.DBListDetailAdapter;
import com.example.myapplication.model.ListModel;

import java.util.ArrayList;

public class DBListActivity extends AppCompatActivity {

    private Context context;
    private DBAdapter dbAdapter;
    private RecyclerView recyclerView;
    private DBListDetailAdapter mAdapter;

    private ArrayList<ListModel> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        init();
    }

    private void init() {
        context = this;
        dbAdapter = new DBAdapter(context);
        dbAdapter.open();

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new DBListDetailAdapter(this, allUsers);

        //set the layout of list
        mAdapter.setLayout(R.layout.list_items);

        //initialize layout manager as per need, (linear, grid etc)
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        setListValues();

    }

    private void setListValues() {
        Cursor cursor = dbAdapter.getAllUser();

        int count = cursor.getCount();
        cursor.moveToFirst();

        if (count > 0) {

            allUsers.clear();
            for (int i = 0; i < count; i++) {

                int user_id = cursor.getInt(1);
                String user_name = cursor.getString(2);


                allUsers.add(new ListModel(user_id, user_name));
                cursor.moveToNext();
            }

            //set the latest array list
            mAdapter.setData(allUsers);

        } else {
            Toast.makeText(DBListActivity.this, "No Data Found !", Toast.LENGTH_SHORT).show();
        }
    }
}

