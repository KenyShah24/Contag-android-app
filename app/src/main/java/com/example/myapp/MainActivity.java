package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.validation.Validator;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText n1;
    Button btn1,btn2;

    FirebaseAuth auth;
    DatabaseReference dbref;
    StorageReference sref;
    StorageTask stask;

    EditText us1,ps1,cp1,phno,email;
    ImageView eye,eye1;
    RadioButton m,f;
    TextView dob;
    Calendar cal;
    int day,month,year,pa=0,co=0;
    private static final int sel_img=1;
    private Button btn3;
    private TextView filename;
    private ImageView img_view;
    private Uri img_uri;
    Bitmap bitmap;
    CircleImageView c;
   // private static final String usr_pat="(?=.*[a-zA-Z])(?=.*[0-9])([^!@#$%^&*\",():;/]).{2,}";
   private static final String usr_pat="([a-zA-Z0-9_]*).{2,}";
    private static final String pas_pat="(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*[@!$#%]).{8,20}";
    private static final String usr_cons="(?=.*[!@#$%^&*\",():;/])";
   // StorageReference sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

       // dbref= FirebaseDatabase.getInstance().getReference().child("User");
      //  sref= FirebaseStorage.getInstance().getReference().child("User");



        auth=FirebaseAuth.getInstance();


        us1=findViewById(R.id.s_usn);
        ps1=findViewById(R.id.s_pas);
        cp1=findViewById(R.id.c_pas);
        email=findViewById(R.id.email);
        phno=findViewById(R.id.phno);
        m=findViewById(R.id.male);
        f=findViewById(R.id.female);
        eye=findViewById(R.id.hide);
        eye1=findViewById(R.id.hide1);


        dob=findViewById(R.id.dob);

        btn1=findViewById(R.id.signup);


        cal= Calendar.getInstance();
        day=cal.get(Calendar.DAY_OF_MONTH);
        month=cal.get(Calendar.MONTH);
        year=cal.get(Calendar.YEAR);

        btn3=findViewById(R.id.photo);
        filename=findViewById(R.id.filename);
       // img_view=findViewById(R.id.image_view);

        c=(CircleImageView)findViewById(R.id.signup_img);


        findViewById(R.id.cal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });


        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pa%2==0) {
                    eye.setImageResource(R.drawable.ic_visible);
                    ps1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pa++;
                }
                else
                {
                    eye.setImageResource(R.drawable.ic_hide);
                    ps1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pa++;
                }
            }
        });

        eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(co%2==0) {
                    eye1.setImageResource(R.drawable.ic_visible);
                    cp1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    co++;
                }
                else
                {
                    eye1.setImageResource(R.drawable.ic_hide);
                    cp1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    co++;
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x=0;
                String username=us1.getText().toString();
                String pass=ps1.getText().toString();
                String conpass=cp1.getText().toString();
                if(isEmpty(us1))
                {
                    us1.setError("Username is required!");
                    x=1;
                }
                if(!isEmpty(us1))
                {
                    if (username.length()<2)
                    {
                        us1.setError("Length must be atleast 2!");
                        x=1;
                    }
                    if (!username.matches(usr_pat))
                    {
                        us1.setError("Username must not contains any chars except [a-zA-Z],[0-9] and [_.]!");
                        x=1;
                    }
                    if (username.matches(usr_pat))
                    {
                        if(username.matches(".*[!@#$%^&*\",():;/].*")) {
                            us1.setError("Username must not contains any special characters!");
                            x = 1;
                        }
                    }

                }
                if(isEmpty(ps1))
                {
                    ps1.setError("Password is required!");
                    x=1;
                }
                if(!isEmpty(ps1))
                {
                    if(pass.length()>=8 && pass.length()<=20) {
                        if (!pass.matches(pas_pat)) {
                            ps1.setError("Must contain Atleast 1 Uppercase,Lowercase,Digit & Special Symbol [@!$#%]");
                            x = 1;
                        }
                    }
                    else
                    {
                        ps1.setError("Length must be between 8-20 characters");
                        x=1;
                    }

                }
                if(!pass.equals(conpass))
                {
                    cp1.setError("Must be same as Password!");
                    x=1;
                }
                if(isEmptymail(email)==false) {
                    email.setError("Email Id is required!");
                    x = 1;
                }
                if(isEmpty(phno))
                {
                    phno.setError("Phone no. is required!");
                    x=1;
                }
                if(isEmpty(dob))
                {
                    dob.setError("DOB is required!");
                    x=1;
                }
                if(!filename.getText().toString().equals("No File Choosen") && isEmpty(filename))
                {
                    filename.setError("Image is required!");
                    x=1;
                }
                if(x==0)
                {
                    if (stask != null && stask.isInProgress()) {
                        Toast.makeText(MainActivity.this, "Account Creation is in Progress..", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        signUp();
                    }
                }
               /* Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();*/
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


    }

    private void openFileChooser()
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,sel_img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == sel_img && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            img_uri=data.getData();
            filename.setText(data.getData().toString());
           // Toast.makeText(this, img_uri.toString(), Toast.LENGTH_SHORT).show();
            Picasso.with(this).load(img_uri).resize(250,250).into(c);
            filename.setText(data.getData().toString());
            //img_view.setImageURI(img_uri);
            //c.setImageURI(img_uri);

        }
        else
        {
            filename.setText("No File Choosen");
        }


    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void signUp()
    {
        final String us = us1.getText().toString();
        final String ps = ps1.getText().toString();
        final String mail=email.getText().toString();
        final String db=dob.getText().toString();
        final String ph=phno.getText().toString();
        final String gen;
        if(m.isChecked())
            gen = m.getText().toString();
        else
            gen = f.getText().toString();



        auth.createUserWithEmailAndPassword(mail,ps).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Email Id already exists!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dbref= FirebaseDatabase.getInstance().getReference("User").child(auth.getUid());
                    sref= FirebaseStorage.getInstance().getReference().child(auth.getUid()).child("Profile Pic");
                    final StorageReference stref=sref.child(img_uri.getLastPathSegment());
                    stask=stref.putFile(img_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    stref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            HashMap<String ,String> hashMap=new HashMap<>();
                                            hashMap.put("imageUrl",String.valueOf(uri));
                                            Data usr = new Data(us,gen,mail,ph,db,String.valueOf(uri).toString(),auth.getUid());
                                           // Toast.makeText(MainActivity.this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
                                          //  String id = dbref.push().getKey();
                                            dbref.setValue(usr);
                                            Toast.makeText(MainActivity.this," Account Created Successfully!", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent in=new Intent(MainActivity.this,Login.class);
                                            startActivity(in);

                                        }
                                    });


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed to create Account! Try Again..", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(MainActivity.this, "Creating Account..", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

        });

       /* String id = dbref.push().getKey();
        // Toast.makeText(MainActivity.this, name+" ", Toast.LENGTH_SHORT).show();

        Toast.makeText(MainActivity.this, usr.getUsr() + id, Toast.LENGTH_SHORT).show();
        dbref.child(id).setValue(usr);*/
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str=text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmpty(TextView text)
    {
        CharSequence str=text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmptymail(EditText text)
    {
        CharSequence str=text.getText().toString();
        return (!TextUtils.isEmpty(str) && Patterns.EMAIL_ADDRESS.matcher(str).matches());
    }
   /* public void checkmail(EditText e)
    {
        auth.fetchProvidersForEmail(e.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                boolean check=!task.getResult().getProviders().isEmpty();
                if(!check)
                    Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();

            }
        });
    }*/
    private void showdialog()
    {
        DatePickerDialog d=new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        d.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int day1, int month1, int year1) {
        month1=month1+1;
        String date=day1+"/"+month1+"/"+year1;
        dob.setText(date);
    }



}
