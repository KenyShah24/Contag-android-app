package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.AvailableNetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

public class Login extends AppCompatActivity {
    Button btn1,btn2;
    EditText us,pas;
    String usr,pass;
    FirebaseAuth auth1;
    DatabaseReference dbref;
    String uid;
    String username;

    TextView forpas;
   public static final String EXTRA_TEXT="com.example.Myapp";
    private FirebaseAuth.AuthStateListener authlis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        auth1=FirebaseAuth.getInstance();

        btn1=findViewById(R.id.signup);
        btn2=findViewById(R.id.login);
        us=findViewById(R.id.s_usn2);
        pas=findViewById(R.id.pas);
        forpas=findViewById(R.id.fpas);

       /* dbref= FirebaseDatabase.getInstance().getReference().child("User");
        authlis=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {*/

                FirebaseUser fuser=auth1.getCurrentUser();
                if(fuser!=null)
                {
                    OneSignal.startInit(this).init();
                    OneSignal.setSubscription(true);
                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String userId, String registrationId) {
                            FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notification_key").setValue(userId);

                        }
                    });
                    OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
                    //Toast.makeText(Login.this, "You are Logged in!!", Toast.LENGTH_SHORT).show();
                    Intent in1=new Intent(Login.this,MyHome.class);
                    startActivity(in1);
                    finish();
                }

      /*      }
        };*/

       forpas.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent in3=new Intent(Login.this,ForgotPass.class);
               startActivity(in3);
           }
       });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x=0;
                String mail=us.getText().toString();
                String pass=pas.getText().toString();
                if(isEmpty(us))
                {
                    Toast.makeText(Login.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
                    us.setError("Email is required!");
                    x=1;
                }
                if(isEmpty(pas))
                {
                    pas.setError("Password is required!");
                    x=1;
                }
                if(x==0)
                {
                    GoToHome(mail,pass);

                }

            }
        });

        ((View) btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensignup();
            }
        });


    }
    public void opensignup()
    {
        Intent in=new Intent(this,MainActivity.class);
        startActivity(in);
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str=text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void GoToHome(String mail1,String pass1)
    {
        //Toast.makeText(this, "enter", Toast.LENGTH_SHORT).show();
        auth1.signInWithEmailAndPassword(mail1,pass1).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                   // showMessage(view, "Error: ${task.exception?.message}")
                    Toast.makeText(Login.this, "Login Unsuccessful!! Try Again..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, "Welcome User!", Toast.LENGTH_SHORT).show();
                   /* dbref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseUser user=auth1.getCurrentUser();
                            uid=user.getUid();
                            Toast.makeText(Login.this, "uid:"+uid, Toast.LENGTH_SHORT).show();
                           for(DataSnapshot ds:dataSnapshot.getChildren())
                            {
                                Data d1=new Data();
                              //  username=ds.child();
                                Toast.makeText(Login.this, "name : "+username, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                    finish();
                    Intent in2=new Intent(Login.this,MyHome.class);
                    //in2.putExtra(EXTRA_TEXT,us.getText().toString());
                  //  Toast.makeText(Login.this, "Logged", Toast.LENGTH_LONG).show();
                    startActivity(in2);

                }
            }
        });
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        auth1.addAuthStateListener(authlis);
    }*/

}

