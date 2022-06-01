package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText msignupemail,msignuppassword;
    private RelativeLayout msignup;
    private TextView mgotologin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);

        firebaseAuth=FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Wszystkie pola są wymagane",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7)
                {
                    Toast.makeText(getApplicationContext(),"Hasło musi mieć więcej niż 7 znaków",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Rejestracja przebiegła pomyślnie", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Rejestracja zakończona niepowodzeniem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification()
        {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Email weryfikacyjny został wysłany, zweryfikuj go, a następnie zaloguj się",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
            });
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Nie udało się wysłać emaila weryfikacyjnego",Toast.LENGTH_SHORT).show();
        }
    }
}