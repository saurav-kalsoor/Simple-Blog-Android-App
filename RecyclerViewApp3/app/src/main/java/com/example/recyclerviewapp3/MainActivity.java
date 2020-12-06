package com.example.recyclerviewapp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mArrayList;

    private EditText mInsertNumber,mRemoveNumber;
    private Button mInsertBtn,mRemoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInsertNumber = (EditText)findViewById(R.id.insertNumber);
        mRemoveNumber = (EditText)findViewById(R.id.removeNumber);
        mInsertBtn = (Button)findViewById(R.id.insertButton);
        mRemoveBtn = (Button)findViewById(R.id.removeButton);


        mInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mInsertNumber.getText().toString())){
                    Toast.makeText(MainActivity.this,"Field is Empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    int position = Integer.parseInt(mInsertNumber.getText().toString());
                    insertItem(position);
                }
            }
        });

        mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mRemoveNumber.getText().toString())){
                    Toast.makeText(MainActivity.this,"Field is Empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    int position = Integer.parseInt(mRemoveNumber.getText().toString());
                    removeItem(position);
                }
            }
        });

        createList();
        buildRecyclerView();

    }

    public void insertItem(int position){
        mArrayList.add(position,new ExampleItem(R.drawable.ic_android,"New Item Inserted At Position "+position,"This is Line 2"));
        mAdapter.notifyItemInserted(position);
        Toast.makeText(MainActivity.this,"Item Added Successfully",Toast.LENGTH_SHORT).show();
        mInsertNumber.setText("");
    }
    public void removeItem(int position){
        mArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);
        Toast.makeText(MainActivity.this,"Item Removed Successfully",Toast.LENGTH_SHORT).show();
        mRemoveNumber.setText("");
    }

    public void changeItem(int position,String text){
        mArrayList.get(position).changeText1("Clicked");
        mAdapter.notifyItemChanged(position);
    }

    public void createList(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 1","Line 2"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 3","Line 4"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 5","Line 6"));
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 1","Line 2"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 3","Line 4"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 5","Line 6"));
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 7","Line 8"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 9","Line 10"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 11","Line 12"));
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 13","Line 14"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 15","Line 16"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 17","Line 18"));
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 19","Line 20"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 21","Line 22"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 23","Line 24"));
        mArrayList.add(new ExampleItem(R.drawable.ic_android,"Line 25","Line 26"));
        mArrayList.add(new ExampleItem(R.drawable.ic_bluetooth,"Line 27","Line 28"));
        mArrayList.add(new ExampleItem(R.drawable.ic_call,"Line 29","Line 6"));
    }

    public void buildRecyclerView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this,mArrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position,"Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }

        });


    }

}
