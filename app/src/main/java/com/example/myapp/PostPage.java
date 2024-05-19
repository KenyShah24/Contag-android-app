package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostPage extends AppCompatActivity {


    private int STORAGE_PERMISSION_CODE = 1;
    Uri imguri,uri;
    Bitmap bmp;
        String myUrl="";
        StorageTask uploadTask;
        StorageReference stref;
        FirebaseAuth auth;
        private static final int sel_img=1;
        BottomNavigationView bnav;
        //NavigationView nv;
        ImageView close,post_img;
        TextView post;
        EditText caption;
        String savetime,savedate;
        private long countpost;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_post_page);


            try {
                this.getSupportActionBar().hide();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }

            close=findViewById(R.id.close);
            post_img=findViewById(R.id.post_img);
            post=findViewById(R.id.post);
            caption=findViewById(R.id.caption);

            bnav=findViewById(R.id.nav_view);
            bnav.setOnNavigationItemSelectedListener(navlist);

            auth=FirebaseAuth.getInstance();

            stref= FirebaseStorage.getInstance().getReference(auth.getUid()).child("Posts");

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Intent i=new Intent(PostPage.this,MyHome.class);
                    finish();
                   // startActivity(i);

                }
            });
            ActivityCompat.requestPermissions(PostPage.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImg();
                }
            });


        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
        private String getFileExtension(Uri uri)
        {
            ContentResolver cr=getContentResolver();
            MimeTypeMap mime=MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cr.getType(uri));
        }

        private void uploadImg()
        {
            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Posting");
            progressDialog.show();
            DatabaseReference db=FirebaseDatabase.getInstance().getReference("Posts");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        countpost = dataSnapshot.getChildrenCount();
                    }

                    //Toast.makeText(PostPage.this, String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            if(imguri!=null) {
                final StorageReference fileref = stref.child(System.currentTimeMillis() + ".jpg");
                uploadTask = fileref.putFile(imguri);
                //Toast.makeText(this, imguri.toString()+getFileExtension(imguri), Toast.LENGTH_SHORT).show();
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isComplete()) {
                            throw task.getException();
                        }
                        return fileref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri duri = task.getResult();
                            myUrl = duri.toString();

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Posts");

                            String postid = dbref.push().getKey();
                            Date todaysdate = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            savedate = format.format(todaysdate);
                            //Time t1=new Time();
                            Date t1 = new Date();
                            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                            savetime= time.format(t1);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("postid", postid);
                            hashMap.put("counter",countpost );
                            hashMap.put("postimage", myUrl);
                            hashMap.put("caption", caption.getText().toString());
                            hashMap.put("userid", auth.getUid());
                            hashMap.put("date",savedate);
                            hashMap.put("time",savetime);

                            dbref.child(postid).setValue(hashMap);
                            countpost++;
                            Intent in1 = new Intent(PostPage.this, MyHome.class);
                            finish();
                            startActivity(in1);
                        }



                        else {
                            Toast.makeText(PostPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostPage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else if(bmp!=null)
            {
                byte[] bytes=getBytes(bmp,100);
                final StorageReference st=stref.child(System.currentTimeMillis()+"."+"jpeg");
                uploadTask=st.putBytes(bytes);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return st.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri duri=task.getResult();
                            myUrl=duri.toString();

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Posts");

                            String postid = dbref.push().getKey();
                            Date todaysdate = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            savedate = format.format(todaysdate);
                            //Time t1=new Time();
                            Date t1 = new Date();
                            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                            savetime= time.format(t1);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("postid", postid);
                            hashMap.put("counter",countpost );
                            hashMap.put("postimage", myUrl);
                            hashMap.put("caption", caption.getText().toString());
                            hashMap.put("userid", auth.getUid());
                            hashMap.put("date",savedate);
                            hashMap.put("time",savetime);

                            dbref.child(postid).setValue(hashMap);
                            countpost++;

                            Intent in1 = new Intent(PostPage.this, MyHome.class);
                            finish();
                            startActivity(in1);

                        }
                        else
                        {
                            Toast.makeText(PostPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }



            else
            {
                Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
            }
        }

        private void openFileChooser()
        {
            Intent i=new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
           /* CropImage.activity()
                    .setAspectRatio(1,1)
                    .start(PostPage.this);*/
            startActivityForResult(i,sel_img);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                //Toast.makeText(this, String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE), Toast.LENGTH_SHORT).show();

                if(resultCode==RESULT_OK) {
                     imguri = result.getUri();
                    post_img.setImageURI(result.getUri());
                }
                else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                {
                    Exception e=result.getError();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
           /* if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK ) {
                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //imguri=result.getUri();  CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                   imguri=CropImage.getPickImageResultUri(this,data);
                  //CropImage.ActivityResult result = CropImage.getActivityResult(data);
               // CropImage.activity().setAspectRatio(1,1).start(PostPage.this);
                post_img.setImageURI(data.getData());
                    //startCrop(imguri);

            }*/
            else if(requestCode==102 && resultCode==RESULT_OK)
            {
                Bitmap bmp=data.getExtras().getParcelable("data");
                post_img.setImageBitmap(bmp);
               // post_img.setScaleType();
            }
            else
            {
                Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
            }
            /*if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK){
                    imguri=result.getUri();
                    post_img.setImageURI(imguri);
                }
            }
            else
            {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                Intent in=new Intent(PostPage.this,HomePage.class);
                startActivity(in);
                finish();
            }*/
        }


    private static byte[] getBytes(Bitmap b,int quality)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return stream.toByteArray();
    }

        private void startCrop(Uri uri)
        {
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        }
    private BottomNavigationView.OnNavigationItemSelectedListener navlist=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_image:
                            //openFileChooser();
                            CropImage.activity().start(PostPage.this);
                            //CropImage.startPickImageActivity(PostPage.this);
                            break;

                        case R.id.navigation_camera:
                            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String root= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"propic.jpg";
                            uri=Uri.parse(root);
                            startActivityForResult(i,102);
                            break;
                    }
                    return false;
                }
            };

    @Override
    public void onBackPressed() {
       // Intent i=new Intent(PostPage.this,HomePage.class);
        finish();
        //startActivity(i);
        //super.onBackPressed();

    }
}
