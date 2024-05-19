package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.Model.Post;
import com.example.myapp.ViewHolder.PhotoHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Settings extends AppCompatActivity {

    RecyclerView recyclerView,recyclerView1;

    ImageView imgpro,options,setting;
    TextView username,follower,following,posts;
    ImageButton my_posts,saved_posts;
    Button edit_pro;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Post, PhotoHolder> adapter;
    FirebaseUser fuser;
    String userid;
    int k=0;
    DatabaseReference uref,uref1,uref2;
    Uri uri;
    View vi;
    List<String> followingList;
    List<String> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setting=findViewById(R.id.Setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Settings.this, Setting_page_design.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    }