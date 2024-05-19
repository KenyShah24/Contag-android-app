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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 1;

    ImageView close,img_profile;
    TextView save,tv_change;
    EditText fullname,user_name;
    Bitmap bmp;

    FirebaseUser fbuser;
    String myUrl;
    int x=0;

    private Uri muri,uri;
    BottomNavigationView bnav;
    private StorageTask uploadTask;
    StorageReference stref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

      /*  try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }*/

        close=findViewById(R.id.close);
        img_profile=findViewById(R.id.img_profile);
        save=findViewById(R.id.save);
        tv_change=findViewById(R.id.tv_change);
        fullname=findViewById(R.id.fullname);
        user_name=findViewById(R.id.user_name);

        bnav=findViewById(R.id.nav_view);
        bnav.setOnNavigationItemSelectedListener(navlist);

        fbuser= FirebaseAuth.getInstance().getCurrentUser();
        stref= FirebaseStorage.getInstance().getReference(fbuser.getUid()).child("Profile Pic");

        ActivityCompat.requestPermissions(EditProfile.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bnav.setVisibility(View.VISIBLE);
               // uploadImage();
            }
        });

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("User").child(fbuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usr=dataSnapshot.child("usr").getValue().toString();
                String imgurl=dataSnapshot.child("imageUrl").getValue().toString();
                user_name.setText(usr);
                fullname.setText(usr);
                Glide.with(getApplicationContext()).load(imgurl).into(img_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(fullname.getText().toString(),
                        user_name.getText().toString());
               // uploadImage();

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

    private void updateProfile(String fullname,String username)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("User").child(fbuser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("usr",username);

        ref.updateChildren(hashMap);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void uploadImage()
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(muri!=null)
        {
            final StorageReference st=stref.child(System.currentTimeMillis()+"."+getFileExtension(muri));
            uploadTask=st.putFile(muri);
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

                        DatabaseReference refe=FirebaseDatabase.getInstance().getReference("User").child(fbuser.getUid());

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageUrl",""+myUrl);

                        refe.updateChildren(hashMap);
                       // Toast.makeText(EditProfile.this, "done", Toast.LENGTH_SHORT).show();

                        Intent in1 = new Intent(EditProfile.this, ProfilePage.class);
                        finish();
                        startActivity(in1);

                    }
                    else
                    {
                        Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

                        DatabaseReference refe=FirebaseDatabase.getInstance().getReference("User").child(fbuser.getUid());

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageUrl",""+myUrl);

                        refe.updateChildren(hashMap);
                        Toast.makeText(EditProfile.this, "done", Toast.LENGTH_SHORT).show();

                        Intent in1 = new Intent(EditProfile.this, ProfilePage.class);
                        finish();
                        startActivity(in1);

                    }
                    else
                    {
                        Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            //Toast.makeText(this, String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE), Toast.LENGTH_SHORT).show();

            if(resultCode==RESULT_OK) {
                muri = result.getUri();
                img_profile.setImageURI(result.getUri());
                x=1;
                uploadImage();
            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception e=result.getError();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }

       /* if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK ) {
            muri=CropImage.getPickImageResultUri(this,data);

            img_profile.setImageURI(data.getData());
            uploadImage();

        }*/
        else if(requestCode==102 && resultCode==RESULT_OK)
        {
            bmp=data.getExtras().getParcelable("data");
            x=1;
            //muri=data.getData();
            img_profile.setImageBitmap(bmp);
            uploadImage();
            // post_img.setScaleType();
        }
        else if(x==0)
        {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }

        /*if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
           // CropImage.ActivityResult result=CropImage.getActivityResult(data);
            //muri=result.getUri();
            muri=data.getData();
            uploadImage();
        }
        else
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();*/
    }

    private static byte[] getBytes(Bitmap b,int quality)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return stream.toByteArray();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlist=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_image:
                            //openFileChooser();
                           // CropImage.startPickImageActivity(EditProfile.this);
                            CropImage.activity().start(EditProfile.this);
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
}
