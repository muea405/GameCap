package com.example.gamecap;

import android.provider.BaseColumns;

import java.util.Date;

public class Template {

    public static final class Entry implements BaseColumns {

        public static final String TABLE_NAME = "gameList";
        public static final String COLUMN_TITLE = "gameTitle";
        public static final String COLUMN_PRICENEW = "gamePriceNew";
        public static final String COLUMN_PRICEOLD = "gamePriceOld";
        public static final String COLUMN_PRICECUT = "gamePriceCut";
        public static final String COLUMN_GAMECOVER = "gameCover";
        public static final String COLUMN_GAMESUMMARY = "gameSummary";
        public static final String COLUMN_DATE = "gameDate";
    }
}
