package com.example.modoowebtoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static String IP_ADDRESS = "192.168.56.1";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;
    private String mJsonString;
    private Bitmap thumb_bitmap; // 썸네일 이미지
    private String current_week;
    private TextView textView_mon;
    private TextView textView_tue;
    private TextView textView_wed;
    private TextView textView_thu;
    private TextView textView_fri;
    private TextView textView_sat;
    private TextView textView_sun;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }

    @Override
    public void onClick(View view) {
        GetData task = new GetData();
        switch (view.getId()){
            case R.id.label_mon:
                changTextColor(current_week);
                textView_mon.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/mon_getjson.php", "");
                break;
            case R.id.label_tue:
                changTextColor(current_week);
                textView_tue.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/tue_getjson.php", "");
                break;
            case R.id.label_wed:
                changTextColor(current_week);
                textView_wed.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/wed_getjson.php", "");
                break;
            case R.id.label_thu:
                changTextColor(current_week);
                textView_thu.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/thu_getjson.php", "");
                break;
            case R.id.label_fri:
                changTextColor(current_week);
                textView_fri.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/fri_getjson.php", "");
                break;
            case R.id.label_sat:
                changTextColor(current_week);
                textView_sat.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/sat_getjson.php", "");
                break;
            case R.id.label_sun:
                changTextColor(current_week);
                textView_sun.setTextColor(getResources().getColor(R.color.black));
                task.execute("http://" + IP_ADDRESS + "/sun_getjson.php", "");
                break;
        }
    }
    private void changTextColor(String week){
        if(!week.equals("mon"))
            textView_mon.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("tue"))
            textView_tue.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("wed"))
            textView_wed.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("thu"))
            textView_thu.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("fri"))
            textView_fri.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("sat"))
            textView_sat.setTextColor(getResources().getColor(R.color.gray));
        if(!week.equals("sun"))
            textView_sun.setTextColor(getResources().getColor(R.color.gray));
    }
    private void initData(){
        textView_mon = (TextView)findViewById(R.id.label_mon);
        textView_tue = (TextView)findViewById(R.id.label_tue);
        textView_wed = (TextView)findViewById(R.id.label_wed);
        textView_thu = (TextView)findViewById(R.id.label_thu);
        textView_fri = (TextView)findViewById(R.id.label_fri);
        textView_sat = (TextView)findViewById(R.id.label_sat);
        textView_sun = (TextView)findViewById(R.id.label_sun);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        myDataset.clear();
        mAdapter.notifyDataSetChanged();

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/" + getDayOfWeek() + "_getjson.php", "");
    }

    private String getDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        String strWeek = "";

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (nWeek){
            case 0:
                strWeek = "mon";
                textView_mon.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                strWeek = "tue";
                textView_tue.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                strWeek = "wed";
                textView_wed.setTextColor(getResources().getColor(R.color.black));
                break;
            case 3:
                strWeek = "thu";
                textView_thu.setTextColor(getResources().getColor(R.color.black));
                break;
            case 4:
                strWeek = "fri";
                textView_fri.setTextColor(getResources().getColor(R.color.black));
                break;
            case 5:
                strWeek = "sat";
                textView_sat.setTextColor(getResources().getColor(R.color.black));
                break;
            case 6:
                strWeek = "sun";
                textView_sun.setTextColor(getResources().getColor(R.color.black));
                break;
        }
        current_week = strWeek;
        return strWeek;
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == null){
                //mTextViewesult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
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
        String TAG_JSON="naver_serial_webtoon_thumb";
        String TAG_TITLE = "title";
        String TAG_SEMI_TITLE = "semi_title";
        String TAG_AUTHOR ="author";
        String TAG_IMAGE ="image";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.d("foweijf", mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                String semi_title = item.getString(TAG_SEMI_TITLE);
                String author = item.getString(TAG_AUTHOR);
                final String image = item.getString(TAG_IMAGE);

                MyData Data = new MyData();

                Data.setTitle(title);
                Data.setSemi_title(semi_title);
                Data.setAuthor(author);


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
