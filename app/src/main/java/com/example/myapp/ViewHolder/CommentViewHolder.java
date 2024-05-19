package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgpro1;
    public TextView username,comment1;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        imgpro1=itemView.findViewById(R.id.imgpro1);
        username=itemView.findViewById(R.id.username);
        comment1=itemView.findViewById(R.id.comment1);

    }
}
