package com.example.bookreview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView reSign, forgotPass;
    EditText email, password;
    Button login;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        reSign = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.Login);
        forgotPass = findViewById(R.id.forgotPass);

        reSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Eemail = email.getText().toString().trim();
                String Epassword = password.getText().toString().trim();
                progressDialog.setMessage("Logging in...");
                progressDialog.show();

                auth.signInWithEmailAndPassword(Eemail, Epassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.cancel();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(LoginActivity.this, "e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                progressDialog.setMessage("Sending Email...");
                progressDialog.show();

                auth.sendPasswordResetEmail(Email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); // Show error message
                            }
                        });
            }
        });

    }
}