package com.example.gamecap;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {

    private AppCompatActivity activity = ListActivity.this;
    Context context = ListActivity.this;
    private RecyclerView recyclerview;
    private ArrayList<Game> listGames;
    private RecyclerAdapter recyclerAdapter;
    private DatabaseHelper databaseHelper;
    SearchView searchBox;
    private ArrayList<Game> filteredList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_list);
        init();
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("TITLE")) {

            String priceNew = getIntent().getExtras().getString("PRICENEW");
            String priceOld = getIntent().getExtras().getString("PRICEOLD");
            String priceCut = getIntent().getExtras().getString("PRICECUT");
            String gameCover = getIntent().getExtras().getString("GAMECOVER");
            String gameSummary = getIntent().getExtras().getString("GAMESUMMARY");
        }

    }

    private void init() {
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        listGames = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(listGames, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(recyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);

        getDataFromSQLite();

    }

    private void getDataFromSQLite() {
        // Fetches entries
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listGames.clear();
                listGames.addAll(databaseHelper. getAllGames());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}