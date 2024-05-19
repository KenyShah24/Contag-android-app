package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class PhotoHolder extends RecyclerView.ViewHolder {

    public ImageView post_image;

    public PhotoHolder(@NonNull View itemView) {
        super(itemView);
        this.post_image = itemView.findViewById(R.id.post_img);
    }

}
