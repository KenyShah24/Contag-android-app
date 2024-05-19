package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.PhotoHolder;
import com.example.myapp.ViewHolder.UserAdapter;
import com.example.myapp.ViewHolder.dataViewHolder;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class blank extends AppCompatActivity {
    RecyclerView recyclerView,recyclerView1;

    ImageView imgpro;
    Button choose;
    Uri iuri,uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        imgpro=findViewById(R.id.pic);
        choose=findViewById(R.id.choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CropImage.startPickImageActivity(blank.this);
               // CropImage.activity().start(blank.this);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            Uri imuri=CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imuri)){
                uri=imuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                , 0);
            }else
            {
                startCrop(imuri);
            }
        }

        //Toast.makeText(this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            //Toast.makeText(this, String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE), Toast.LENGTH_SHORT).show();

            if(resultCode==RESULT_OK) {
               // iuri = result.getUri();
                imgpro.setImageURI(result.getUri());
            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e=result.getError();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    }
    private void startCrop(Uri imuri)
    {
        CropImage.activity(imuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
}
