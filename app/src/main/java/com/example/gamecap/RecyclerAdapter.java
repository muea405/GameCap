package com.example.gamecap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    private ArrayList<Game> listGames;
    public ImageView overflow;
    private Context mContext;
    private ArrayList<Game> mFilteredList = new ArrayList<Game>();

    public RecyclerAdapter(ArrayList<Game> listGames, Context mContext) {
        this.listGames = listGames;
        this.mContext = mContext;
        this.mFilteredList = listGames;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gameTitle;
        public TextView gameOld;
        public TextView gameNew;
        public TextView gameCut;
        public ImageView cover;
        public TextView summary;

        public ViewHolder(View v) {
            super(v);
            gameTitle = (TextView) v.findViewById(R.id.gameTitle);
            gameOld = (TextView) v.findViewById(R.id.gameOld);
            gameNew = (TextView) v.findViewById(R.id.gameNew);
            gameCut = (TextView) v.findViewById(R.id.gameCut);
            cover = (ImageView) v.findViewById(R.id.cover);
            summary = (TextView) v.findViewById(R.id.summary);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.gameTitle.setText(listGames.get(position).getTitle());
        holder.gameOld.setText(listGames.get(position).getPriceNew());
        holder.gameNew.setText(listGames.get(position).getPriceOld());
        holder.gameCut.setText(listGames.get(position).getPriceCut());
        Picasso.get().load(listGames.get(position).getGameCover()).into(holder.cover);
        holder.summary.setText(listGames.get(position).getGameSummary());
    }

    @Override
    public int getItemCount() {
        try{
            return mFilteredList.size();
        }
        catch (NullPointerException e)
        {
            return '0';
        }
    }



}