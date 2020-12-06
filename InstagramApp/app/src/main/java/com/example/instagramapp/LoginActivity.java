package com.example.instagramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField,mPasswordField;
    private Button mLoginBtn;
    private TextView mRegisterUser;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = (EditText)findViewById(R.id.emailField);
        mPasswordField = (EditText)findViewById(R.id.passwordField);
        mLoginBtn = (Button)findViewById(R.id.loginButton);
        mRegisterUser = (TextView)findViewById(R.id.registerUser);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = mEmailField.getText().toString();
                String txt_password = mPasswordField.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this,"Error! Fields Are Empty",Toast.LENGTH_SHORT).show();
                    mPasswordField.setText("");
                }
                else{
                    mProgressDialog.setMessage("Signing In, Please Wait");
                    mProgressDialog.show();
                    loginUser(txt_email,txt_password);
                }
            }
        });
    }

    private void loginUser(String txt_email, String txt_password) {
        mAuth.signInWithEmailAndPassword(txt_email,txt_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Email and Password Do Not Match! Try Again",Toast.LENGTH_LONG).show();
                mPasswordField.setText("");
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,StartActivity.class));
        finish();
        super.onBackPressed();
    }
}
