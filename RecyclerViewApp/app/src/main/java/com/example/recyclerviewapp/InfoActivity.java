package com.example.recyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTitle,mDescription;


    private String data1,data2;
    private int mImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mImageView = (ImageView)findViewById(R.id.mainImageView);
        mTitle = (TextView)findViewById(R.id.mainTitle);
        mDescription = (TextView)findViewById(R.id.mainDescription);

        getData();
        setData();
    }

    private void getData(){
        if(getIntent().hasExtra("data1") && getIntent().hasExtra("data2") && getIntent().hasExtra("myImage")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            mImage = getIntent().getIntExtra("myImage",1);
        }
        else{
            Toast.makeText(InfoActivity.this,"No Data Found!",Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        mTitle.setText(data1);
        mDescription.setText(data2);
        mImageView.setImageResource(mImage);
    }
}
