package com.example.modoowebtoon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    static private ArrayList<MyData> mDataset;
    static private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTitleView;
        public TextView mSemiTitleView;
        public TextView mAuthorView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.thumb_image);
            mTitleView = (TextView)view.findViewById(R.id.title_view);
            mSemiTitleView = (TextView)view.findViewById(R.id.semi_title_view);
            mAuthorView = (TextView)view.findViewById(R.id.author_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition(); // 선택된 뷰 위치
                    if(pos != RecyclerView.NO_POSITION){
                        MyData getData = mDataset.get(pos);
                        //클릭됐으면 제목 가져와서 웹툰 리스트 띄우기
                        String title = getData.getTitle();
                        String author = getData.getAuthor();
                        Bitmap thumb = getData.getImage();

                        Intent intent = new Intent(MainActivity.mContext,webtoon_list_view.class);
                        intent.putExtra("title", title);
                        intent.putExtra("author", author);
                        intent.putExtra("thumb", thumb);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<MyData> myDataset, Context context) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        // layout에 넣을 view를 정한다.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumb_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitleView.setText(mDataset.get(position).title);
        holder.mSemiTitleView.setText(mDataset.get(position).semi_title);
        holder.mAuthorView.setText(mDataset.get(position).author);
        holder.mImageView.setImageBitmap(mDataset.get(position).image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
// view에 저장될 데이터 목록
class MyData{
    String title;
    String semi_title;
    String author;
    Bitmap image;

    public MyData() {
        this.title = "";
        this.semi_title = "";
        this.author = "";
        this.image = null;
    }


    public MyData(String title, String semi_title, String author, Bitmap image) {
        this.title = title;
        this.semi_title = semi_title;
        this.author = author;
        this.image = image;
    }


    public String getSemi_title() {
        return semi_title;
    }

    public void setSemi_title(String semi_title) {
        this.semi_title = semi_title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
