package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.DBAdapter;
import com.example.myapplication.model.ListModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button btnProceedDBList;

    private DBAdapter db;

    private ArrayList<ListModel> addUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        context = this;

        db = new DBAdapter(context);
        db.open();

        btnProceedDBList = findViewById(R.id.btnDBList);

        setListeners();
    }

    private void setListeners() {

        db.deleteAllData();

        addUsers.clear();
        addUsers.add(new ListModel(1, "Android"));
        addUsers.add(new ListModel(2, "Java"));
        addUsers.add(new ListModel(3, "Kotlin"));
        addUsers.add(new ListModel(4, "MVVM"));
        addUsers.add(new ListModel(5, "Retrofit"));

        db = new DBAdapter(context);
        db.open();

        db.addListItem(addUsers);

        btnProceedDBList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(context, DBListActivity.class));
            }
        });
    }

}

