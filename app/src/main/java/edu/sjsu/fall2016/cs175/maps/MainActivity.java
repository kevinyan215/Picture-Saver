package edu.sjsu.fall2016.cs175.maps;


import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View;


import com.squareup.picasso.Picasso;


import edu.sjsu.fall2016.cs175.maps.model.APIService;
import edu.sjsu.fall2016.cs175.maps.model.PicResponse;
import edu.sjsu.fall2016.cs175.maps.model.RetrofitHelper;
import edu.sjsu.fall2016.cs175.maps.model.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private APIService picAPIService;
    //private Context context;
    private LinearLayout layout;
    private static final int numberOfPictures = 10;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //context = getApplicationContext();

        layout = (LinearLayout) findViewById(R.id.imageHolder);
        RetrofitHelper.init();
        picAPIService = RetrofitHelper.getService();

        db = openOrCreateDatabase("picDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pictures(url VARCHAR);");
        //db.execSQL("DELETE FROM pictures");

    }


    public void searchPictures(View view){
        layout.removeAllViews();

        EditText searchText = (EditText) findViewById(R.id.searchText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        String searchTerms = searchText.getText().toString();
        //Log.d("SEarch", searchTerms);

        String headerURL = "https://api.unsplash.com/search/photos?client_id=67ac3222bfd4ba972349eee7f37387067105a795d63f186bbd1db774820deaa9&query=";
        String url = headerURL + searchTerms;


        Call<SearchResponse> searchResponse = picAPIService.getSearchPic(url);
        searchResponse.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                //Log.d("response", response + "");
                //Log.d("response2", response.body() + "");
                //Log.d("response2", response.body().getPicResponse() + "");

                if(response.isSuccessful()) {
                    PicResponse[] arrayUrl = response.body().getPicResponse();

                    int max = 0;
                    if(arrayUrl.length > 10){
                        max = 10;
                    }
                    else{
                        max = arrayUrl.length;
                    }
                    for(int i = 0; i < max; i++){
                        String url = arrayUrl[i].getPicURL();
                        loadURL(url, true);
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"API Cap Limit Exceeded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API Failure", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void showSavedPictures(View view){
        layout.removeAllViews();
        Cursor c = db.rawQuery("SELECT * FROM pictures", null);
        while(c.moveToNext()){
            StringBuffer buffer = new StringBuffer();
            buffer.append(c.getString(0));
            String url = buffer.toString();
            Log.d("db", url);

            loadURL(url,false);
        }
    }

    public void showRandomPictures(View view) {

        layout.removeAllViews();
        for(int i = 0; i < numberOfPictures; i++){
            getRandomPic();
        }


    }

    public void getRandomPic(){
        Call<PicResponse> picResponse = picAPIService.getPic();
        picResponse.enqueue(new Callback<PicResponse>() {
            @Override
            public void onResponse(Call<PicResponse> call, Response<PicResponse> response) {
                //Log.d("response", response + "");
                //Log.d("response2", response.body() + "");
                if(response.isSuccessful()) {
                    String url = response.body().getPicURL();
                    //Log.d("pic", url);
                    loadURL(url, true);
                }
                else{
                    Toast.makeText(MainActivity.this,"API Cap Limit Exceeded", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PicResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadURL(final String url, boolean insert){

        ImageButton imageButton = new ImageButton(this);
        if(insert) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    db.execSQL("INSERT INTO pictures VALUES('" + url + "');");
                }
            });
        }
        layout.addView(imageButton);

        Picasso.with(this)
                .load(url)
                .resize(700,500)
                .into(imageButton);
    }


}
