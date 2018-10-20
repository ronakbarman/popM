package com.ronak.popularmovies.RecyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ronak.popularmovies.R;
import com.ronak.popularmovies.Review;

import java.util.ArrayList;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.viewHolder> {

  private ArrayList<Review> dataSource;


 public void setDataSource(ArrayList<Review> dataSource1){
      dataSource = dataSource1;
      notifyDataSetChanged();
  }

 public ReviewRecyclerViewAdapter(ArrayList<Review> dataSource1){
      this.dataSource = dataSource1;
  }

  class  viewHolder extends RecyclerView.ViewHolder {

      TextView author, content;


      public viewHolder(@NonNull View itemView) {
          super(itemView);
          author = itemView.findViewById(R.id.review_author);
          content = itemView.findViewById(R.id.review_content);
      }
  }

    @NonNull
    @Override
    public ReviewRecyclerViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_holder, viewGroup, false);
       return new ReviewRecyclerViewAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
      Review review = dataSource.get(i);
      String author = review.getmAuthor();
      String content = review.getmContent();
      viewHolder.author.setText(author);
      viewHolder.content.setText(content);
    }



    @Override
    public int getItemCount() {
        return dataSource.size();
    }


}
