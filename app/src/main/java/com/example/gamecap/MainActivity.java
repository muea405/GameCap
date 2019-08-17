package com.example.gamecap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final AppCompatActivity activity = MainActivity.this;

    private TextView gameTitleMain, gameTitle, gameOldPrice, gameOld, gameNewPrice, gameNew, gameCutPrice, gameCut, summary, frontTitle;
    private ImageView cover, frontCover;
    private EditText gameSearch;
    private Button addButton;

    public String gameID = "";

    public String ITADPriceUrl1 = "https://api.isthereanydeal.com/v01/game/prices/?key=a55b69a9ec840c6d1d7cd948e56f71b631074686&plains=";
    public String ITADPriceUrl2 = "&region=us&country=US&shops=steam"; //Only does steam US for now
    public String IDGBInfoUrl1 = "https://api-v3.igdb.com/games/?search=";
    public String IDGBInfoUrl2 = "&fields=id,summary,cover.url&limit=1";

    public String title, priceOld, priceNew, priceCut, coverJSON, gameCover, gameSummary, gameCoverL;

    private DatabaseHelper databaseHelper;
    private Game game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        gameSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        gameID = gameSearch.getText().toString();
                        Log.d("Gamenamehere:", gameID);
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        run();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addGame(game);

                Intent accountsIntent = new Intent(activity, ListActivity.class);
                accountsIntent.putExtra("TITLE", title);
                accountsIntent.putExtra("GAMENEW", priceNew);
                accountsIntent.putExtra("GAMEOLD", priceOld);
                accountsIntent.putExtra("GAMECUT", priceCut);
                accountsIntent.putExtra("GAMECOVER", gameCover);
                accountsIntent.putExtra("GAMESUMMARY", gameSummary);
                startActivity(accountsIntent);
            }
        });
    }

    private void init() {

        gameTitle = (TextView) findViewById(R.id.gameTitle);
        gameOld = (TextView) findViewById(R.id.gameOld);
        gameNew = (TextView) findViewById(R.id.gameNew);
        gameCut = (TextView) findViewById(R.id.gameCut);
        gameSearch = (EditText) findViewById(R.id.gameSearch);
        cover = (ImageView) findViewById(R.id.cover);
        summary = (TextView) findViewById(R.id.summary);
        addButton = (Button) findViewById(R.id.addButton);
        frontTitle = (TextView) findViewById(R.id.frontTitle);
        frontCover = (ImageView) findViewById(R.id.frontCover);

        databaseHelper = new DatabaseHelper(activity);
        game = new Game();
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        //ITAD
        String url = ITADPriceUrl1 + gameID + ITADPriceUrl2;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Whosefault","Amanda");
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string(); //Straight JSON

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("JSON:", myResponse);

                        try {
                            JSONObject priceObject = new JSONObject(myResponse);
                            title = gameID.substring(0, 1).toUpperCase() + gameID.substring(1);
                            priceOld = priceObject.getJSONObject("data").getJSONObject(gameID).getJSONArray("list").getJSONObject(0).getString("price_old");
                            priceNew = priceObject.getJSONObject("data").getJSONObject(gameID).getJSONArray("list").getJSONObject(0).getString("price_new");
                            priceCut = priceObject.getJSONObject("data").getJSONObject(gameID).getJSONArray("list").getJSONObject(0).getString("price_cut");

                            Log.d("Title:", title);
                            Log.d("Old Price:", priceOld);
                            Log.d("New Price:", priceNew);
                            Log.d("Price Cut:", priceCut);

                            game.setTitle(title);
                            game.setPriceOld(priceOld);
                            game.setPriceNew(priceNew);
                            game.setPriceCut(priceCut);

                            frontTitle.setText(title);
                            addButton.setText("Add and view list");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            frontTitle.setText("Game not found");
                            addButton.setText("View List");
                        }
                    }
                });

            }
        });

        //IGDB

        String url2 = IDGBInfoUrl1 + gameID + IDGBInfoUrl2;

        Request request2 = new Request.Builder()
                .url(url2)
                .get()
                .addHeader("user-key", "a247364390f4967303c27fe259e17b31")
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Whosefault","Jesse");
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse2 = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("JSON", myResponse2);
                            JSONObject json2 = (new JSONArray(myResponse2)).getJSONObject(0);
                            coverJSON = "https:" + json2.getJSONObject("cover").getString("url");
                            gameSummary = json2.getString("summary");
                            gameCover = coverJSON.substring(0, 44) + "cover_big" + coverJSON.substring(49);
                            gameCoverL = coverJSON.substring(0, 44) + "cover_big_2x" + coverJSON.substring(49);

                            Log.d("Cover URL:", gameCover);
                            Log.d("Summary:", gameSummary);

                            game.setGameCover(gameCover);
                            game.setGameSummary(gameSummary);

                            Picasso.get().load(gameCoverL).into(frontCover);
                        } catch (JSONException e) {
                            frontCover.setImageDrawable(null);
                            e.printStackTrace();
                            frontTitle.setText("Game not found");
                            addButton.setText("View List");
                        }
                    }
                });

            }
        });
    }
}