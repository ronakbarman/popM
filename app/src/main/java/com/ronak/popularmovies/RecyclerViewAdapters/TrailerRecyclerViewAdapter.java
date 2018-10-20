package com.ronak.popularmovies.RecyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ronak.popularmovies.R;
import com.ronak.popularmovies.Trailer;

import java.util.ArrayList;

public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.viewHolder>
{

    private ArrayList<Trailer> dataSource;
    private final BTap bTap;

   public void setDataSource(ArrayList<Trailer> dataSource1){
        dataSource = dataSource1;
        notifyDataSetChanged();
    }

    public interface BTap{
       void selectedTrailer(String key);
   }


    @Override
    public TrailerRecyclerViewAdapter.viewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_holder, viewGroup, false);
        return  new TrailerRecyclerViewAdapter.viewHolder(view);
    }

    public TrailerRecyclerViewAdapter(ArrayList<Trailer> dataSource1, BTap bTap1){
        dataSource = dataSource1;
        bTap = bTap1;
    }



    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        Trailer trailer = dataSource.get(i);
        String name = trailer.getName();
        viewHolder.trailerButton.setText(name);

    }

    class viewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener {

        Button trailerButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trailerButton = itemView.findViewById(R.id.trailerB);
            trailerButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          int position = getAdapterPosition();
            Trailer trailer = dataSource.get(position);
            bTap.selectedTrailer(trailer.getKey());
        }
    }


    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
