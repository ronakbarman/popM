package com.ronak.popularmovies.RecyclerViewAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ronak.popularmovies.Movie;
import com.ronak.popularmovies.NetworkUtilities;
import com.ronak.popularmovies.R;

import java.util.ArrayList;

public class MovieRecyclerViewAdapter extends  RecyclerView.Adapter<MovieRecyclerViewAdapter.mViewHolder>{

private ArrayList<Movie> dataSource;
private final ItemClickListener itemClickListener;
private Context context;

public void setDataSource(ArrayList<Movie> dataSource1){
    dataSource = dataSource1;
    notifyDataSetChanged();
}

public MovieRecyclerViewAdapter(ArrayList<Movie> dataSource, Context context1, ItemClickListener itemClickListener) {
    this.dataSource = dataSource;
    this.context =  context1;
    this.itemClickListener = itemClickListener;
}


    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_holder, viewGroup, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder viewHolder, int i) {
   Movie details = dataSource.get(i);
   String posterPath = details.getMovieposterpath();
        NetworkUtilities.setPoster(context,posterPath,viewHolder.PosterView);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }


    public interface ItemClickListener {
    void onItemClick(Movie movie);
    }

    class mViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

    ImageView PosterView;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            PosterView = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            itemClickListener.onItemClick(dataSource.get(pos));
        }
    }
}
