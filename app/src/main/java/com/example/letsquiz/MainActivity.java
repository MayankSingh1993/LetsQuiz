package com.example.letsquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText username, password;
    private ProgressBar bar;
    private Object TextInputLayout;
    private FirebaseAuth mAuth;
    private TextView register, register1;
    private AppCompatButton signInButton;


    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            reload();

        }
    }

    private void reload() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //Get reference of all the required views
        username =  findViewById(R.id.email);
        password =  findViewById(R.id.pwd);
        bar =  findViewById(R.id.progress_bar);
        signInButton= findViewById(R.id.sign_in_button);




        /*setting intent on register textview 1 to go to
          register activity */
        register = findViewById(R.id.register_user);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTo = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goTo);

            }
        });

         /*setting intent on register textview 2 to go to
          register activity */
        register1 = findViewById(R.id.register_user2);
        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTo = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goTo);

            }
        });


    }

}