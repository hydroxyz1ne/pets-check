package com.example.petscheck;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;

    private EditText name;
    private Button saveprofileBtn;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile_activity);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailReset);
        resetPasswordButton = findViewById(R.id.resetPass);
        name = findViewById(R.id.name);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        saveprofileBtn = findViewById(R.id.saveprofileBtn);

        saveprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = name.getText().toString().trim();
                if (!newUsername.isEmpty()) {
                    updateUserName(newUsername);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(EditProfileActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserName(String newUsername) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child(userId);

        userRef.child("login").setValue(newUsername)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Optionally finish the activity
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to update username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
