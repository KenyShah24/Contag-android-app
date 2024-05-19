package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPass extends AppCompatActivity {

    EditText mail;
    Button rpas;
    FirebaseAuth auth_pas;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        mail=findViewById(R.id.email);
        rpas=findViewById(R.id.respas);


        auth_pas=FirebaseAuth.getInstance();

        rpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ema=mail.getText().toString().trim();
                if(isEmpty(mail))
                {
                    mail.setError("Email Id is required!");
                }
                else
                {
                    auth_pas.sendPasswordResetEmail(ema).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(ForgotPass.this, "Error in sending password reset mail!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ForgotPass.this, "Password reset mail is sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPass.this,Login.class));
                            }
                        }
                    });
                }

            }
        });

        

    }

    boolean isEmpty(EditText text)
    {
        CharSequence str=text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
