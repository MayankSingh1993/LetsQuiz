package com.example.letsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // array that can user type
    private final String[] userType = {"Teacher", "Student"};

    // Fields initialisation
    private TextView loginUser;
    private TextInputEditText username, email, password;
    private ProgressBar progressBar;
    private AppCompatButton signUpButton;
    private Spinner userSelection;
    private Intent goToLogin;

    //firebase auth initialisation
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        progressBar =findViewById(R.id.progress_bar);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        loginUser = findViewById(R.id.loginUser);
        loginUser.setText(
                Utils.htmlFormat(
                        Utils.getColoredSpanned(getString(R.string.sign_in_text), "#FF000000") + "" +
                                Utils.getColoredSpanned(getString(R.string.Sign_in), "#3CF436")
                )
        );

        goToLogin = new Intent(RegisterActivity.this, MainActivity.class);
        loginUser.setOnClickListener(v -> {
            startActivity(goToLogin);

        });


        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked

        userSelection = findViewById(R.id.spinner);
        userSelection.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter userArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userType);


        // set simple layout resource file
        // for each item of spinner
        userArrayAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        userSelection.setAdapter(userArrayAdapter);
    }

    private void createUser() {
        String userName = Objects.requireNonNull(username.getText()).toString();
        String userEmail = Objects.requireNonNull(email.getText()).toString();
        String userPwd = Objects.requireNonNull(password.getText()).toString();

        if (TextUtils.isEmpty(userName)) {
            username.setError("Username cannot be empty");
            username.requestFocus();
            return;
        } else if (TextUtils.isEmpty(userEmail)) {

            email.setError("Email cannot be empty");
            email.requestFocus();
            return;
        } else if (TextUtils.isEmpty(userPwd)) {

            password.setError("Password cannot be empty");
            password.requestFocus();
            return;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Please provide valid email");
            email.requestFocus();
            return;
        } else if(userPwd.length()<6){
            password.setError("Password length should be greater than 6");
            password.requestFocus();
            return;

        }else
         {
            mAuth.createUserWithEmailAndPassword(userEmail, userPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        User user = new User(userSelection.getSelectedItem().toString(),userName,userEmail);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                    startActivity(goToLogin);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });


                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration ERROR!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void updateUI(FirebaseUser user) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // make toast of name of course
        // which is selected in spinner
        Toast.makeText(getApplicationContext(), userType[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}