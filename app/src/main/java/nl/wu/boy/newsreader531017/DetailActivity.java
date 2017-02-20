package nl.wu.boy.newsreader531017;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boy.newsreader531017.R;
import com.squareup.okhttp.ResponseBody;

import java.net.URL;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailActivity extends AppCompatActivity {

    private static final String ITEM = "item";
    private static final String POSITION = "position";

    private ImageView image;
    private TextView title;
    private TextView summary;
    private ApiServices mService;
    private FloatingActionButton fab;
    FloatingActionButton likeButton;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        resources = getResources();

        Intent intent = getIntent();

        image = (ImageView) findViewById(R.id.detail_image);
        title = (TextView) findViewById(R.id.detail_title);
        summary = (TextView) findViewById(R.id.detail_summary);

        final Article data = intent.getParcelableExtra(ITEM);

        image.setImageBitmap(urlToBMP(data.Image));
        title.setText(data.Title);
        summary.setText(data.Summary);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(ApiServices.class);

        fab = (FloatingActionButton) findViewById(R.id.detail_read_more);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(data.Url != null)
                {
                    goToUrl(data.Url);
                }
                else {
                    Snackbar.make(view, "There is no source given", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        likeButton = (FloatingActionButton) findViewById(R.id.detail_like);

        if(data.IsLiked == false)
            likeButton.setImageResource(R.drawable.ic_star_border_white_24dp);
        else
            likeButton.setImageResource(R.drawable.ic_grade_yellow_24dp);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.IsLiked == false)
                {
                    likeButton.setImageResource(R.drawable.ic_grade_yellow_24dp);
                    Globals.ArticleLike = "Like";
                    data.IsLiked = true;
                    like(data.Id);
                }
                else {
                    likeButton.setImageResource(R.drawable.ic_star_border_white_24dp);
                    Globals.ArticleLike = "Dislike";
                    data.IsLiked = false;
                    dislike(data.Id);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap urlToBMP(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(src);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    protected void like(int id) {
        retrofit.Callback<ResponseBody> likeCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(findViewById(R.id.content_detail), R.string.like, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(findViewById(R.id.content_detail), R.string.failure, Snackbar.LENGTH_LONG).show();
            }
        };

        mService.likeArticle(Globals.authToken, id).enqueue(likeCallback);
    }

    protected void dislike(int id) {
        retrofit.Callback<ResponseBody> dislikeCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(findViewById(R.id.content_detail), R.string.dislike, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(findViewById(R.id.content_detail), R.string.failure, Snackbar.LENGTH_LONG).show();
            }
        };

        mService.dislikeArticle(Globals.authToken, id).enqueue(dislikeCallback);
    }
}