package com.example.jgz.jgzafvalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jgz.jgzafvalapp.models.ChallengeItem;
import com.example.jgz.jgzafvalapp.models.LogItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ChallengeFragment extends Fragment implements Callback<List<ChallengeItem>> {

    private final String MY_CHALLANGES = "Challanges";
    private final String MY_ITEMS = "Items";
    SharedPreferences sharedPref;

    private View rootView;

    private ChallengeAdapter adapter;
    private RecyclerView mRecyclerView;
    private ApiServices mService;
    private ProgressBar mProgress;

    private Snackbar description;

    Menu menu;
    MenuItem confirmButton;
    private List<ChallengeItem> myChallenges;

    TextView title1;

    public ChallengeFragment() {
        // Required empty public constructor
    }

    public static ChallengeFragment newInstance() {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_challenge, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.challenge_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);

        title1 = (TextView) rootView.findViewById(R.id.list_header);

        myChallenges = new ArrayList<>();

        sharedPref = this.getActivity().getSharedPreferences(MY_CHALLANGES, MODE_PRIVATE);
        if(sharedPref.contains(MY_ITEMS))
        {
            Set<String> currentList = sharedPref.getStringSet(MY_ITEMS, null);

            for (String title:currentList) {
                myChallenges.add(new ChallengeItem(title));
            }

            adapter = new ChallengeAdapter(getContext(), myChallenges, false, new ClickableListener() {
                @Override
                public void onClick(int position) { }
            });

            description = Snackbar.make(rootView, String.format(getString(R.string.days_left), adapter.getRemainingDays()), Snackbar.LENGTH_INDEFINITE);
            description.show();
        }

        else {

            description = Snackbar.make(rootView, R.string.challenge_description, Snackbar.LENGTH_INDEFINITE);
            description.show();

            List<ChallengeItem> test = new ArrayList();
            test.add(new ChallengeItem("Doe 10 push-ups per dag"));
            test.add(new ChallengeItem("Doe 20 sit-ups per dag"));
            test.add(new ChallengeItem("Beweeg 2 uur per dag"));

            adapter = new ChallengeAdapter(getContext(), test, true, new ClickableListener() {
                @Override
                public void onClick(int position) {
                    ChallengeItem selected = adapter.getItem(position);

                    if (selected.selected) {
                        myChallenges.add(selected);
                    } else {
                        myChallenges.remove(selected);
                    }

                    if (myChallenges.size() == 2) {
                        confirmButton.setVisible(true);
                        confirmButton.setIcon(R.drawable.ic_right_check);
                    } else {
                        confirmButton.setVisible(false);
                    }
                }
            });
        }

        mRecyclerView.setAdapter(adapter);

        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);
        hideSpinner();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(ApiServices.class);
//        fetchContent();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        this.confirmButton = menu.findItem(R.id.action_right);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_right:
                title1.setText(R.string.my_challenges);
                confirmButton.setVisible(false);
                description.dismiss();
                adapter.clearList();
                adapter.switchClickable();
                adapter.addItems(myChallenges);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResponse(Response<List<ChallengeItem>> response) {
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

//        mService.getLog("12").enqueue(this);
    }

    @Override
    public void onStop(){
        super.onStop();

        SharedPreferences settings = this.getActivity().getSharedPreferences(MY_CHALLANGES, MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences.Editor editor = settings.edit();

        Set<String> items = adapter.getAllItemTitles();

        if(items != null)
            editor.putStringSet(MY_ITEMS, items);

        editor.commit();
    }
}