package com.example.modoowebtoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class webtoon_list_view extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;
    private String mJsonString;
    private Bitmap thumb_bitmap; // 썸네일 이미지
    private TextView inside_title;
    private TextView inside_author;
    private ImageView inside_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtoon_list_view);

        initData();
    }

    private void initData(){
        inside_title = findViewById(R.id.thumb_inside_title);
        inside_author = findViewById(R.id.thumb_inside_author);
        inside_image = findViewById(R.id.thumb_inside_image);

        Intent intent = getIntent();

        inside_title.setText(intent.getStringExtra("title"));
        inside_author.setText(intent.getStringExtra("author"));
        inside_image.setImageBitmap((Bitmap)intent.getParcelableExtra("image"));

        inside_author = findViewById(R.id.author_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.list_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();

        mAdapter = new ListAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        myDataset.clear();
        mAdapter.notifyDataSetChanged();

        GetData task = new GetData();
        task.execute( "http://" + MainActivity.IP_ADDRESS + "/list_getjson.php?title=" + intent.getStringExtra("title"), "");
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(webtoon_list_view.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null){
                //mTextViewesult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
            progressDialog.dismiss();
        }

        // DB에서 data 가져오기
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }
    }
    //JSON OBJECT에서 데이터 추출
    private void showResult(){
        String TAG_JSON="naver_serial_webtoon_inside";
        String TAG_TITLE = "title";
        String TAG_SEMI_TITLE = "semi_title";
        String TAG_AUTHOR ="author";
        String TAG_IMAGE ="image_data";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.d("foweijf", mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            myDataset.clear();

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                String semi_title = item.getString(TAG_SEMI_TITLE);
                final String image = item.getString(TAG_IMAGE);

                MyData Data = new MyData();

                Data.setTitle(title);
                Data.setSemi_title(semi_title);

                Thread mThread = new Thread(){
                    @Override
                    public void run() {
                        URL imgUrl = null;
                        HttpURLConnection connection = null;
                        InputStream is = null;
                        Bitmap retBitmap = null;
                        try{
                            imgUrl = new URL(image);
                            connection = (HttpURLConnection)imgUrl.openConnection();
                            connection.setDoInput(true);//url로 input받는 flag 허용
                            connection.connect(); //연결
                            is = connection.getInputStream(); // get inputstream
                            retBitmap = BitmapFactory.decodeStream(is);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }finally {
                            if(connection!=null) {
                                connection.disconnect();
                            }
                            thumb_bitmap =  retBitmap;
                        }
                    }
                };
                mThread.start();

                try{
                    mThread.join();
                    Data.setImage(thumb_bitmap);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                myDataset.add(Data);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d("main", "showResult : ", e);
        }
    }
}
