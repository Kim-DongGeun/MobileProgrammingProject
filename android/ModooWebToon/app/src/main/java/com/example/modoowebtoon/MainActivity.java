package com.example.modoowebtoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }

    private void initData(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();

        myDataset.add(new MyData("img1", R.drawable.tower_of_god_3_36_1));
        myDataset.add(new MyData("img2", R.drawable.tower_of_god_3_36_10));
        myDataset.add(new MyData("img3", R.drawable.tower_of_god_3_36_11));
        myDataset.add(new MyData("img4", R.drawable.tower_of_god_3_36_12));
        myDataset.add(new MyData("img5", R.drawable.tower_of_god_3_36_13));
        myDataset.add(new MyData("img6", R.drawable.tower_of_god_3_36_14));


        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
}
