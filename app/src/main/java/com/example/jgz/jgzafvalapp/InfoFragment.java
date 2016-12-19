package com.example.jgz.jgzafvalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.jgz.jgzafvalapp.models.InfoItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;

public class InfoFragment extends Fragment implements Callback<List<InfoItem>> {

    private static final String ARG_CATEGORY = "category";

    private View rootView;
    private InfoAdapter adapter;
    private RecyclerView mRecyclerView;
    private ApiServices mService;
    private ProgressBar mProgress;

    private String category;
    private long nextDate;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(String category) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);

        // Recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.info_view );
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<InfoItem> start = new ArrayList();

        for(int x=0; x < 20; x++) {
            start.add(new InfoItem("XLR-8", category + " " + (x + 1), "Cupcake ipsum dolor sit amet sweet chocolate sweet sesame snaps. Marshmallow sesame snaps jelly donut jelly pie. Gummi bears sweet pastry I love biscuit marshmallow. Lollipop danish tiramisu cotton candy biscuit. Soufflé tart apple pie tiramisu.", "http://www.jqueryscript.net/images/Simplest-Responsive-jQuery-Image-Lightbox-Plugin-simple-lightbox.jpg", "http://www.google.com/"));
        }

        adapter = new InfoAdapter(getContext(), start, new ClickableListener() {
            @Override
            public void onClick(int position) {
                InfoItem item = adapter.getItem(position);
                goToUrl(item.url);
            }
        });
        mRecyclerView.setAdapter(adapter);

        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);
        hideSpinner();

//        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Globals.apiURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        mService = retrofit.create(ApiServices.class);
//        fetchContent();

        return rootView;
    }

    @Override
    public void onResponse(Response<List<InfoItem>> response) {
//        hideSpinner();
//        if (response.isSuccess() && response.body() != null) {
//            if(adapter == null) {
//                adapter = new ChallengeAdapter(rootView, response.body());
//                mRecyclerView.setAdapter(adapter);
//            }
//
//            else {
//                adapter.addItems(response.body().logItems);
//            }
//
//            mRecyclerView.animate().alpha(1);
//        } else {
//            Snackbar.make(rootView, R.string.error, Snackbar.LENGTH_LONG).setAction(
//                    R.string.retry, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            fetchContent();
//                        }
//                    }).show();
//        }
    }

    @Override
    public void onFailure(Throwable t) {
        //something went wrong
        hideSpinner();
        Snackbar.make(rootView, R.string.failure, Snackbar.LENGTH_LONG).setAction(
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

    private void fetchContent() {
        mRecyclerView.animate().alpha(0);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.animate().alpha(1);

        switch (category){
            case "Voeding":
                if(nextDate != 0) mService.getArticleCategory("voeding");
                else mService.getMoreArticleCategory(nextDate, "voeding");
                break;
            case "Beweging":
                if(nextDate != 0) mService.getArticleCategory("beweging");
                else mService.getMoreArticleCategory(nextDate, "beweging");
                break;
            default:
                if(nextDate != 0) mService.getArticleCategory("voeding");
                else mService.getMoreArticleCategory(nextDate, "voeding");
                break;
        }
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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
}
