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

import com.example.bookreview.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class SignUpActivity extends AppCompatActivity {

    TextView reLogin;
    EditText name, email, password;
    Button signUp;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        reLogin = findViewById(R.id.login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp);

        progressDialog = new ProgressDialog(this);

        reLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ename = name.getText().toString();
                String Eemail = email.getText().toString().trim();
                String Epassword = password.getText().toString();

                progressDialog.setMessage("Signing Up...");
                progressDialog.show();
                auth.createUserWithEmailAndPassword(Eemail, Epassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                        progressDialog.cancel();

                        firestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(new Users(Ename, Eemail, Epassword));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
            }
        });
    }
}