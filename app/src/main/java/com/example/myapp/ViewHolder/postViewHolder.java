package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class postViewHolder extends RecyclerView.ViewHolder {

        public TextView caption;
        public TextView username;
        public ImageView postimg,dots;
        public TextView publisher;
        public CircleImageView proimg;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public LinearLayout layout;
        public TextView com_line;
        public TextView likes;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            caption= itemView.findViewById(R.id.description);
            username=itemView.findViewById(R.id.username);
            dots=itemView.findViewById(R.id.more_opt);
            publisher=itemView.findViewById(R.id.publisher);
            proimg=itemView.findViewById(R.id.image_profile);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            save=itemView.findViewById(R.id.save);
            likes=itemView.findViewById(R.id.likes);
            layout=itemView.findViewById(R.id.post_layout);
            com_line=itemView.findViewById(R.id.comments);
            //ematxt=itemView.findViewById(R.id.email_txt);
            // dobtxt=itemView.findViewById(R.id.dob_txt);
            //photxt=itemView.findViewById(R.id.phno_txt);
            //gentxt=itemView.findViewById(R.id.gen_txt);
            postimg = itemView.findViewById(R.id.post_image);
        }

}
