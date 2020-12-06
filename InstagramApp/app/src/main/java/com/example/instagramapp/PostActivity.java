package com.example.instagramapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private ImageView mClose,mImageAdded;
    private TextView mPost;
    private SocialAutoCompleteTextView mDescription;
    private Uri mImageUri;
    private String mDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mClose = (ImageView)findViewById(R.id.closeImage);
        mImageAdded = (ImageView)findViewById(R.id.imageAdded);
        mPost = (TextView)findViewById(R.id.postText);
        mDescription = (SocialAutoCompleteTextView)findViewById(R.id.description);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,MainActivity.class));
                finish();
            }
        });

        mImageAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(PostActivity.this);

            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });



    }

    private void uploadImage() {
        if(mImageUri!=null){
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+".jpeg");
            StorageTask uploadTask = filepath.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    mDownloadUrl = downloadUri.toString();



                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = mDatabaseReference.push().getKey();

                    Map<String,Object> mMap = new HashMap<>();
                    mMap.put("postid",postId);
                    mMap.put("imageUri",mDownloadUrl);
                    mMap.put("description",mDescription.getText().toString());
                    mMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    mDatabaseReference.child(postId).setValue(mMap);

                    DatabaseReference mHashtagRef = FirebaseDatabase.getInstance().getReference().child("Hashtags");
                    List<String> mHashtags = mDescription.getHashtags();
                    if(!mHashtags.isEmpty()){
                        for(String tag : mHashtags){
                            mMap.clear();
                            mMap.put("tag",tag.toLowerCase());
                            mMap.put("postid",postId);

                            mHashtagRef.child(tag.toLowerCase()).setValue(mMap);
                        }
                    }
                    mProgressDialog.dismiss();
                    Toast.makeText(PostActivity.this,"Upload Successful!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                    finish();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(PostActivity.this,"No Image was Selected",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)t{
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            mImageAdded.setImageURI(mImageUri);

        }
        else {
            Toast.makeText(PostActivity.this,"Some Error Occurred,Try Again!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }
}
