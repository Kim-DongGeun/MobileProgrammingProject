package com.example.modoowebtoon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WebtoonAdapter extends RecyclerView.Adapter<WebtoonAdapter.ViewHolder> {
    static private Context mContext;
    static private ArrayList<MyData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView webtoon_mImageView;

        public ViewHolder(View view) {
            super(view);
            webtoon_mImageView = (ImageView)view.findViewById(R.id.webtoon_image_view);
            //mTitleView = (TextView)view.findViewById(R.id.title_view);
            //inside_mSemiTitleView = (TextView)view.findViewById(R.id.inside_list_semi_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WebtoonAdapter(ArrayList<MyData> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WebtoonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        // layout에 넣을 view를 정한다.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.webtoon, parent, false);
        // set the view's size, margins, paddings and layout parameters
        WebtoonAdapter.ViewHolder vh = new WebtoonAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WebtoonAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTitleView.setText(mDataset.get(position).title);
        //holder.inside_mSemiTitleView.setText(mDataset.get(position).semi_title);
        holder.webtoon_mImageView.setImageBitmap(mDataset.get(position).image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
