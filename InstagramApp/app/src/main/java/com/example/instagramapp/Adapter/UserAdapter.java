package com.example.instagramapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramapp.Model.Users;
import com.example.instagramapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<Users> mUsers;
    private boolean isFragment;
    private FirebaseUser mFirebaseUser;

    public UserAdapter(Context context, List<Users> Users, boolean b) {
        mContext = context;
        mUsers = Users;
        isFragment = b;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Users user =mUsers.get(position);
        holder.mFollowBtn.setVisibility(View.VISIBLE);

        holder.mUsernameField.setText(user.getUsername());
        holder.mNameField.setText(user.getName());

        Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.mProfileImage);
        isFollowed(user.getId(),holder.mFollowBtn);

        if(user.getId().equals(mFirebaseUser.getUid())){
            holder.mFollowBtn.setVisibility(View.GONE);
        }
    }

    private void isFollowed(final String id, final Button mFollowBtn) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Follow").child(mFirebaseUser.getUid()).child("following");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    mFollowBtn.setText("following");
                }
                else{
                    mFollowBtn.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {

        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public CircleImageView mProfileImage;
        public TextView mUsernameField,mNameField;
        public Button mFollowBtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileImage = itemView.findViewById(R.id.imageProfile);
            mUsernameField = itemView.findViewById(R.id.usernameField);
            mNameField = itemView.findViewById(R.id.nameField);
            mFollowBtn = itemView.findViewById(R.id.followButton);
        }

    }
}
