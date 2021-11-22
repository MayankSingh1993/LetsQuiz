package com.example.letsquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText username, password;
    private ProgressBar progressBar;
    private Object TextInputLayout;
    private FirebaseUser currentUser;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    private TextView register;
    ArrayList<User> list = new ArrayList<>();
    private AppCompatButton signInButton;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//    private void updateUI(FirebaseUser currentUser) {
//    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //Get reference of all the required views
        username = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        progressBar = findViewById(R.id.progress_bar);
        signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });




        /*setting intent on register textview 1 to go to
          register activity */
        register = findViewById(R.id.register_user);
        register.setText(Utils.htmlFormat(Utils.getColoredSpanned(getString(R.string.register), "#FF000000") + " " + Utils.getColoredSpanned(getString(R.string.Sign_up), "#3CF436")));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();

            }
        });


    }

    private void userLogin() {
        String email = username.getText().toString().trim();
        String pass_word = password.getText().toString().trim();

        if (email.isEmpty()) {
            username.setError("Email is required!");
            username.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            username.setError("Please enter a valid email!");
            username.requestFocus();
            return;
        }
        if (pass_word.isEmpty()) {
            password.setError("Please enter a valid email!");
            password.requestFocus();
            return;
        }
        if (pass_word.length() < 6) {
            password.setError("Minimum password length should be");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    firebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User users = null;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                users = dataSnapshot.getValue(User.class);

                                // get the user data using realtime database userid
                                users.getUserId(dataSnapshot.getKey());
                                list.add(users);
                            }
                            String userType = users.getUser();
                            if (userType.equals("Teacher")) {
                                Intent goToTeacher = new Intent(MainActivity.this, TeacherActivity.class);
                                goToTeacher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToTeacher);
                                finish();
                            } else if (userType.equals("Student")) {
                                Intent goToStudent = new Intent(MainActivity.this, StudentActivity.class);
                                goToStudent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(goToStudent);
                                finish();

                            } else {
                                Toast.makeText(MainActivity.this, "Failed Login. Please Try Again", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}