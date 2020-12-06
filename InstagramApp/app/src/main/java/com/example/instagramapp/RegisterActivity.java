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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsernameField,mNameField,mPasswordField,mEmailField;
    private Button mRegisterBtn;
    private TextView mLoginUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameField = (EditText)findViewById(R.id.usernameField);
        mEmailField = (EditText)findViewById(R.id.emailField);
        mPasswordField = (EditText)findViewById(R.id.passwordField);
        mNameField = (EditText)findViewById(R.id.nameField);
        mRegisterBtn = (Button) findViewById(R.id.registerButton2);
        mLoginUser = (TextView)findViewById(R.id.loginUser);
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        mLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = mUsernameField.getText().toString();
                String txt_name = mNameField.getText().toString();
                String txt_password = mPasswordField.getText().toString();
                String txt_email = mEmailField.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)){
                    Toast.makeText(RegisterActivity.this,"Credential Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password Too Short!",Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressDialog.setMessage("Registering User");
                    mProgressDialog.show();
                    registerUser(txt_username,txt_email,txt_name,txt_password);
                }

            }
        });
    }

    private void registerUser(final String txt_username, final String txt_email, final String txt_name, String txt_password) {
        mAuth.createUserWithEmailAndPassword(txt_email,txt_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this,"User Successfully Registered!",Toast.LENGTH_LONG).show();
                Map<String,Object> mMap = new HashMap<>();
                mMap.put("name",txt_name);
                mMap.put("username",txt_username);
                mMap.put("email",txt_email);
                mMap.put("id",mAuth.getCurrentUser().getUid());
                mMap.put("bio",null);
                mMap.put("imageUrl","default");

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"User Successfully Registered!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error Occured While Registering, please try again",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this,StartActivity.class));
        finish();
        super.onBackPressed();
    }
}
