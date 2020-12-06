package com.example.simpleblogapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    ImageButton mSelectImage;
    EditText mTitleField,mDescField;
    Button mSubmitBtn;
    Uri mImageUri = null;
    StorageReference mStorage;
    ProgressDialog mProgressDialog;
    DatabaseReference mDatabase;
    private static final int GALLERY_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mSelectImage = (ImageButton) findViewById(R.id.selectImage);
        mTitleField = (EditText) findViewById(R.id.imageTitleField);
        mDescField = (EditText) findViewById(R.id.imageDescriptionField);
        mSubmitBtn = (Button) findViewById(R.id.postButton);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blogs");
        mProgressDialog = new ProgressDialog(this);


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

    
    public void startUploading() {
        final String title_val = mTitleField.getText().toString().trim();
        final String desc_val = mDescField.getText().toString().trim();

        if(!title_val.equals("") && !desc_val.equals("") && mImageUri!=null)
        {
            mProgressDialog.setMessage("Posting To Blog...");
            mProgressDialog.show();
            final StorageReference filepath = mStorage.child("Blog_image").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri)
                    .continueWithTask(task -> {
                        if (!task.isSuccessful())
                        {
                            throw Objects.requireNonNull(task.getException());
                        }

                        return filepath.getDownloadUrl();
                    })
                    .addOnCompleteListener(task-> {
                        if (task.isSuccessful())
                        {

                            Uri downloadUri = task.getResult();
                            String downloadURL = null;
                            if (downloadUri != null) {
                                downloadURL = downloadUri.toString();

                            }
                            DatabaseReference newPost = mDatabase.push();
                            newPost.child("Title").setValue(title_val);
                            newPost.child("Description").setValue(desc_val);
                            newPost.child("DownloadUrl").setValue(downloadURL);
                            mProgressDialog.dismiss();
                            Toast.makeText(getBaseContext(),"Post Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this,MainActivity.class));
                        }
                    });



        }
    }



}
