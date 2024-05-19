package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

public class notifyHolder extends RecyclerView.ViewHolder {

    public ImageView image_profile,post_image1;
    public TextView user1,txt;



    public notifyHolder(@NonNull View itemView) {
        super(itemView);

        image_profile=itemView.findViewById(R.id.image_profile);
        post_image1=itemView.findViewById(R.id.post_image);
        user1=itemView.findViewById(R.id.user);
        txt=itemView.findViewById(R.id.comm);
    }
}
