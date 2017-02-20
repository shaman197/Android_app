package nl.wu.boy.newsreader531017;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.boy.newsreader531017.R;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<Result> {

    private static final String ITEM = "item";

    private ListViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private ApiServices mService;
    private ProgressBar mProgress;

    private int nextId = 0;
    private int currentItemPosition, LastVisible, totalItemCount;
    private Boolean loading = true;
    Menu menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgress = (ProgressBar) findViewById(R.id.activity_progress);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    if(loading == true) {
                        LastVisible = adapter.getCurrentPosition();
                        totalItemCount = adapter.getItemCount();

                        if (LastVisible >= (totalItemCount - 3)) {
                            loading = false;
                            fetchContent();
                        }
                    }
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(ApiServices.class);
        fetchContent();
    }

    private void fetchContent() {
        mRecyclerView.animate().alpha(0);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.animate().alpha(1);

        if (Globals.authToken != null || (Globals.authToken != null && Globals.resetList == true))
            mService.getArticles(Globals.authToken).enqueue(this);
        else if(adapter == null || Globals.resetList == true)
            mService.getArticles().enqueue(this);
        else if (Globals.authToken != null && nextId != 0)
            mService.getNextArticles(Globals.authToken, nextId).enqueue(this);
        else if (nextId != 0) {
            mService.getNextArticles(nextId).enqueue(this);
        }
    }

    @Override
    public void onResponse(Response<Result> response) {
        hideSpinner();
        if (response.isSuccess() && response.body() != null) {
            if(adapter == null) {
                nextId = response.body().NextId;
                adapter = new ListViewAdapter(this, response.body().Results, new ListEventListener() {

                    @Override
                    public void goToDetail(int position) {
                        currentItemPosition = position;
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(ITEM, adapter.getItem(position));
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }

            else {
                loading = true;
                nextId = response.body().NextId;
                adapter.addItems(response.body().Results);
            }

            mRecyclerView.animate().alpha(1);
        } else {
            Snackbar.make(findViewById(R.id.activity_main), R.string.error, Snackbar.LENGTH_LONG).setAction(
                    R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchContent();
                        }
                    }).show();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        //something went wrong
        Log.e("NewsReader", "Could net fetch data", t);
        Snackbar.make(findViewById(R.id.activity_main), R.string.failure, Snackbar.LENGTH_LONG).setAction(
                R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchContent();
                    }
                }).show();
    }

    private void hideSpinner() {
        mProgress.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menuList = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                adapter = null;
                fetchContent();
                return true;
            case R.id.action_login:
                if(Globals.authToken == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Globals.authToken = null;
                    Snackbar.make(findViewById(R.id.activity_main), R.string.logout, Snackbar.LENGTH_LONG).show();
                    adapter = null;
                    ChangeMenu();
                    fetchContent();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ChangeMenu() {
        MenuItem userItem = menuList.findItem(R.id.action_login);
        if (Globals.authToken != null) {
            userItem.setIcon(R.drawable.ic_delete_white_24dp);
        } else {
            userItem.setIcon(R.drawable.ic_account_box_white_24dp);
        }
    }

    @Override
    protected void onResume() {
        super.onRestart();
        if(Globals.resetList == true) {
            Globals.resetList = false;
            adapter = null;
            nextId = 0;
            ChangeMenu();
            fetchContent();
        }

        if(Globals.ArticleLike == "Like")
        {
            Globals.ArticleLike = "Nothing";
            Article currentArticle = adapter.getItem(currentItemPosition);
            currentArticle.IsLiked = true;
            adapter.editItem(currentItemPosition, currentArticle);
        }

        else if(Globals.ArticleLike == "Dislike")
        {
            Globals.ArticleLike = "Nothing";
            Article currentArticle = adapter.getItem(currentItemPosition);
            currentArticle.IsLiked = false;
            adapter.editItem(currentItemPosition, currentArticle);
        }
    }
}
