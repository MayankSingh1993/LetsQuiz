package com.example.letsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // array that can user type
    private String[] user = {"Teacher", "Student"};

    // Fields initialisation
    private TextView loginUser, loginUser2;
    private TextInputEditText username, email, password, confirmPassword;
    private ProgressBar bar;
    private AppCompatButton signUpButton;
    private Spinner userSelection;

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
        confirmPassword = findViewById(R.id.confirm_pwd);


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
        Intent goToLogin = new Intent(RegisterActivity.this, MainActivity.class);
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
        ArrayAdapter userArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, user);


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
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPwd = password.getText().toString();
        String userConfirmPwd = confirmPassword.getText().toString();
        // String userType = userSelection.getTransitionName().toString();
        if (TextUtils.isEmpty(userName)) {
            username.setError("Username cannot be empty");
            username.requestFocus();
        } else if (TextUtils.isEmpty(userEmail)) {

            email.setError("Email cannot be empty");
        } else if (TextUtils.isEmpty(userPwd)) {

            password.setError("Password cannot be empty");
        } else if (TextUtils.isEmpty(userConfirmPwd)) {

            confirmPassword.setError("Confirm password cannot be empty");
        } else {
            mAuth.createUserWithEmailAndPassword(userEmail, userPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
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
        Toast.makeText(getApplicationContext(), user[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}