package com.example.gamecap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "gameList.db";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + Template.Entry.TABLE_NAME + " (" +
                Template.Entry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_PRICENEW + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_PRICEOLD + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_PRICECUT + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_GAMECOVER + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_GAMESUMMARY + " TEXT NOT NULL, " +
                Template.Entry.COLUMN_DATE + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }
    //drop beneficiary table
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + Template.Entry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //---opens the database---
    public DatabaseHelper open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {

        //Drop User Table if exist

        db1.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(db1);

    }


    public void addGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Template.Entry.COLUMN_TITLE, game.getTitle());
        values.put(Template.Entry.COLUMN_PRICENEW, game.getPriceNew());
        values.put(Template.Entry.COLUMN_PRICEOLD, game.getPriceOld());
        values.put(Template.Entry.COLUMN_PRICECUT, game.getPriceCut());
        values.put(Template.Entry.COLUMN_GAMECOVER, game.getGameCover());
        values.put(Template.Entry.COLUMN_GAMESUMMARY, game.getGameSummary());
        values.put(Template.Entry.COLUMN_DATE, String.valueOf(game.getGameDate()));

        db.insert(Template.Entry.TABLE_NAME, null, values);
        db.close();
    }

    public List<Game> getAllGames() {
        // array of columns to fetch
        String[] columns = {
                Template.Entry.COLUMN_TITLE,
                Template.Entry.COLUMN_PRICENEW,
                Template.Entry.COLUMN_PRICEOLD,
                Template.Entry.COLUMN_PRICECUT,
                Template.Entry.COLUMN_GAMECOVER,
                Template.Entry.COLUMN_GAMESUMMARY,
                Template.Entry.COLUMN_DATE
        };
        List<Game> gameList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(Template.Entry.TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                Template.Entry.COLUMN_DATE); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Game game = new Game();
                game.setTitle(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_TITLE)));
                game.setPriceNew(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_PRICENEW)));
                game.setPriceOld(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_PRICEOLD)));
                game.setPriceCut(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_PRICECUT)));
                game.setGameCover(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_GAMECOVER)));
                game.setGameSummary(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_GAMESUMMARY)));
                game.setGameDate(cursor.getString(cursor.getColumnIndex(Template.Entry.COLUMN_DATE)));
                gameList.add(game);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return gameList;
    }

}