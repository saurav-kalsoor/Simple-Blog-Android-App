package com.example.recyclerviewapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private String data1[],data2[];
    private int images[];



    public MyAdapter(Context ct,String s1[],String s2[],int images[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        this.images = images;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View mView = mLayoutInflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mText1.setText(data1[position]);
        holder.mText2.setText(data2[position]);
        holder.mImage.setImageResource(images[position]);


        holder.mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context,InfoActivity.class);
                mIntent.putExtra("data1",data1[position]);
                mIntent.putExtra("data2",data2[position]);
                mIntent.putExtra("myImage",images[position]);

                context.startActivity(mIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mText1,mText2;
        private ImageView mImage;
        private RelativeLayout mMainLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mText1 = itemView.findViewById(R.id.text1);
            mText2 = itemView.findViewById(R.id.text2);
            mImage = itemView.findViewById(R.id.image);
            mMainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
