package com.example.gamecap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Game {

    private String title;
    private String priceNew;
    private String priceOld;
    private String priceCut;
    private String gameCover;
    private String gameSummary;
    private String gameDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(String priceNew) {
        this.priceNew = priceNew;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(String priceOld) {
        this.priceOld = priceOld;
    }

    public String getPriceCut() {
        return priceCut;
    }

    public void setPriceCut(String priceCut) {
        this.priceCut = priceCut;
    }

    public String getGameCover() {
        return gameCover;
    }

    public void setGameCover(String gameCover) {
        this.gameCover = gameCover;
    }

    public String getGameSummary() {
        return gameSummary;
    }

    public void setGameSummary(String gameSummary) {
        this.gameSummary = gameSummary;
    }

    public String getGameDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        gameDate = sdf.format(new Date());
        /*java.util.Date utilDate = new java.util.Date(); //Was trying to make these Date for consistency
        gameDate = new java.sql.Date(utilDate.getTime());*/
        return gameDate; }

    public void setGameDate(String gameDate) { this.gameDate = gameDate; }
}