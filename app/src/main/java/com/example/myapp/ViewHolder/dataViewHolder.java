package com.example.myapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;

//import com.example.work.Model.SearchUser;

public class dataViewHolder extends RecyclerView.ViewHolder {

    public TextView usrtxt;
    public TextView ematxt;
    public TextView dobtxt;
    public TextView photxt;
    public TextView gentxt;
    public ImageView imgtxt;
    public RelativeLayout rlay;
    public Button follow;

    public dataViewHolder(@NonNull View itemView) {
        super(itemView);

        usrtxt= itemView.findViewById(R.id.name_txt);
        follow=itemView.findViewById(R.id.follow);
        rlay=itemView.findViewById(R.id.user_lay);
        //ematxt=itemView.findViewById(R.id.email_txt);
       // dobtxt=itemView.findViewById(R.id.dob_txt);
        //photxt=itemView.findViewById(R.id.phno_txt);
        //gentxt=itemView.findViewById(R.id.gen_txt);
        imgtxt = itemView.findViewById(R.id.pro_img);
    }
}
