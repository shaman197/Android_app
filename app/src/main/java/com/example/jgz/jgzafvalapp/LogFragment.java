package com.example.jgz.jgzafvalapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgz.jgzafvalapp.models.LogItem;
import com.example.jgz.jgzafvalapp.models.Logbook;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LogFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private View logbookView;

    private String userId;
    private String logbookId;
    private TextView date;
    private DatePickerDialog datePickerDialog;
    private int year, month, day;
    private String today;
    private String pickedDate;

    // Breakfast, Lunch, Dinner, Snacks, Workout
    private LogAdapter adapter1, adapter2, adapter3, adapter4, adapter5;
    private RecyclerView mRecyclerView1, mRecyclerView2, mRecyclerView3, mRecyclerView4, mRecyclerView5;
    private ApiServices mService;
    private ProgressBar mProgress;

    private TextView listFooter1, listFooter2, listFooter3, listFooter4, listFooter5;
    private LinearLayout addView1, addView2, addView3, addView4, addView5;
    private AutoCompleteTextView addName1, addName2, addName3, addName4, addName5;
    private EditText addAmount1, addAmount2, addAmount3, addAmount4, addAmount5;
    private Button submitItem1, submitItem2, submitItem3, submitItem4, submitItem5;

    private Button buttonSad;
    private Button buttonNeutral;
    private Button buttonHappy;

    private static final String breakfast = "Breakfast";
    private static final String lunch = "Lunch";
    private static final String dinner = "Dinner";
    private static final String snacks = "Snacks";
    private static final String workout = "workout";

    private static final String sad = "Sad";
    private static final String neutral = "Neutral";
    private static final String happy = "Happy";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_log, container, false);

        logbookView = rootView.findViewById(R.id.logbook_view);

        addListViews();
        addRecyclerViews();

        buttonSad = (Button) rootView.findViewById(R.id.mood_sad);
        buttonNeutral = (Button) rootView.findViewById(R.id.mood_neutral);
        buttonHappy = (Button) rootView.findViewById(R.id.mood_happy);

        buttonSad.setOnClickListener(this);
        buttonNeutral.setOnClickListener(this);
        buttonHappy.setOnClickListener(this);

        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(ApiServices.class);

        Account myAccount = new Account(getActivity().getIntent().getStringExtra(LoginActivity.EMAIL), LoginActivity.ACCOUNT_TYPE);
        userId =  AccountManager.get(getActivity()).getUserData(myAccount, LoginActivity.USERID);

        // Calender
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        today = pickedDate = day + "-" + (month + 1) + "-" + year;

        date = (TextView) rootView.findViewById(R.id.date);
        date.setText(day + "-" + month + "-" + year);
        rootView.findViewById(R.id.date).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                datePickerDialog = new DatePickerDialog(getActivity(), R.style.OverlayPrimaryGreen,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                pickedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                date.setText(pickedDate);
                                fetchContent();
                            }
                        }
                        , year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        fetchContent();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_footer1:
                listFooter1.setVisibility(View.GONE);
                addView1.setVisibility(View.VISIBLE);
                break;
            case R.id.add_item_submit1:
                if(!addAmount1.getText().toString().equals("") && !addName1.getText().toString().equals("")) {
                    listFooter1.setVisibility(View.VISIBLE);
                    addView1.setVisibility(View.GONE);
                    LogItem item =  new LogItem(logbookId, addName1.getText().toString(), breakfast, Integer.parseInt(addAmount1.getText().toString()));
                    mService.postLogItem(item).enqueue(itemSuccess);
                    adapter1.addItem(item);

                    addAmount1.setText("");
                    addName1.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.item_required, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.list_footer2:
                listFooter2.setVisibility(View.GONE);
                addView2.setVisibility(View.VISIBLE);
                break;
            case R.id.add_item_submit2:
                if(!addAmount2.getText().toString().equals("") && !addName2.getText().toString().equals("")) {
                    listFooter2.setVisibility(View.VISIBLE);
                    addView2.setVisibility(View.GONE);
                    LogItem item =  new LogItem(logbookId, addName2.getText().toString(), lunch, Integer.parseInt(addAmount2.getText().toString()));
                    mService.postLogItem(item).enqueue(itemSuccess);
                    adapter2.addItem(item);

                            addAmount2.setText("");
                    addName2.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.item_required, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.list_footer3:
                listFooter3.setVisibility(View.GONE);
                addView3.setVisibility(View.VISIBLE);
                break;
            case R.id.add_item_submit3:
                if(!addAmount3.getText().toString().equals("") && !addName3.getText().toString().equals("")) {
                    listFooter3.setVisibility(View.VISIBLE);
                    addView3.setVisibility(View.GONE);
                    LogItem item = new LogItem(logbookId, addName3.getText().toString(), dinner, Integer.parseInt(addAmount3.getText().toString()));
                    mService.postLogItem(item).enqueue(itemSuccess);
                    adapter3.addItem(item);

                    addAmount3.setText("");
                    addName3.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.item_required, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.list_footer4:
                listFooter4.setVisibility(View.GONE);
                addView4.setVisibility(View.VISIBLE);
                break;
            case R.id.add_item_submit4:
                if(!addAmount4.getText().toString().equals("") && !addName4.getText().toString().equals("")) {
                    listFooter4.setVisibility(View.VISIBLE);
                    addView4.setVisibility(View.GONE);
                    LogItem item = new LogItem(logbookId, addName4.getText().toString(), snacks, Integer.parseInt(addAmount4.getText().toString()));
                    mService.postLogItem(item).enqueue(itemSuccess);
                    adapter4.addItem(item);

                    addAmount4.setText("");
                    addName4.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.item_required, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.list_footer5:
                listFooter5.setVisibility(View.GONE);
                addView5.setVisibility(View.VISIBLE);
                break;
            case R.id.add_item_submit5:
                if(!addAmount5.getText().toString().equals("") && !addName5.getText().toString().equals("")) {
                    listFooter5.setVisibility(View.VISIBLE);
                    addView5.setVisibility(View.GONE);
                    LogItem item = new LogItem(logbookId, addName5.getText().toString(), workout, Integer.parseInt(addAmount5.getText().toString()));
                    mService.postLogItem(item).enqueue(itemSuccess);
                    adapter5.addItem(item);

                    addAmount5.setText("");
                    addName5.setText("");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.item_required, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mood_sad:
                mService.changeMood(logbookId, sad).enqueue(moodSuccess);
                Toast.makeText(getContext(), R.string.response_sad, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mood_neutral:
                mService.changeMood(logbookId, neutral).enqueue(moodSuccess);
                Toast.makeText(getContext(), R.string.response_neutral, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mood_happy:
                mService.changeMood(logbookId, happy).enqueue(moodSuccess);
                Toast.makeText(getContext(), R.string.response_happy, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_right:
                adapter1.switchDeleteMode();
                adapter2.switchDeleteMode();
                adapter3.switchDeleteMode();
                adapter4.switchDeleteMode();
                adapter5.switchDeleteMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mRecyclerView1.animate().alpha(0);
        mRecyclerView2.animate().alpha(0);
        mRecyclerView3.animate().alpha(0);
        mRecyclerView4.animate().alpha(0);
        mRecyclerView5.animate().alpha(0);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.animate().alpha(1);

        mService.getLog(userId, pickedDate).enqueue(getLogbook);
    }

    private void addListViews() {
        // add view 1
        listFooter1 = (TextView) rootView.findViewById(R.id.list_footer1);
        listFooter1.setOnClickListener(this);

        addView1 = (LinearLayout) rootView.findViewById(R.id.add_view1);

        addName1 = (AutoCompleteTextView) rootView.findViewById(R.id.add_item_name1);
        addAmount1 = (EditText) rootView.findViewById(R.id.add_item_amount1);
        submitItem1 = (Button) rootView.findViewById(R.id.add_item_submit1);
        submitItem1.setOnClickListener(this);

        // add view 2
        listFooter2 = (TextView) rootView.findViewById(R.id.list_footer2);
        listFooter2.setOnClickListener(this);

        addView2 = (LinearLayout) rootView.findViewById(R.id.add_view2);

        addName2 = (AutoCompleteTextView) rootView.findViewById(R.id.add_item_name2);
        addAmount2 = (EditText) rootView.findViewById(R.id.add_item_amount2);
        submitItem2 = (Button) rootView.findViewById(R.id.add_item_submit2);
        submitItem2.setOnClickListener(this);

        buttonSad = (Button) rootView.findViewById(R.id.mood_sad);
        buttonNeutral = (Button) rootView.findViewById(R.id.mood_neutral);
        buttonHappy = (Button) rootView.findViewById(R.id.mood_happy);

        // add view 3
        listFooter3 = (TextView) rootView.findViewById(R.id.list_footer3);
        listFooter3.setOnClickListener(this);

        addView3 = (LinearLayout) rootView.findViewById(R.id.add_view3);

        addName3 = (AutoCompleteTextView) rootView.findViewById(R.id.add_item_name3);
        addAmount3 = (EditText) rootView.findViewById(R.id.add_item_amount3);
        submitItem3 = (Button) rootView.findViewById(R.id.add_item_submit3);
        submitItem3.setOnClickListener(this);

        // add view 4
        listFooter4 = (TextView) rootView.findViewById(R.id.list_footer4);
        listFooter4.setOnClickListener(this);

        addView4 = (LinearLayout) rootView.findViewById(R.id.add_view4);

        addName4 = (AutoCompleteTextView) rootView.findViewById(R.id.add_item_name4);
        addAmount4 = (EditText) rootView.findViewById(R.id.add_item_amount4);
        submitItem4 = (Button) rootView.findViewById(R.id.add_item_submit4);
        submitItem4.setOnClickListener(this);

        // add view 5
        listFooter5 = (TextView) rootView.findViewById(R.id.list_footer5);
        listFooter5.setOnClickListener(this);

        addView5 = (LinearLayout) rootView.findViewById(R.id.add_view5);

        addName5 = (AutoCompleteTextView) rootView.findViewById(R.id.add_item_name5);
        addAmount5 = (EditText) rootView.findViewById(R.id.add_item_amount5);
        submitItem5 = (Button) rootView.findViewById(R.id.add_item_submit5);
        submitItem5.setOnClickListener(this);
    }

    private void addRecyclerViews() {
        // Recycler view 1
        mRecyclerView1 = (RecyclerView) rootView.findViewById(R.id.log_view1);
        adapter1 = new LogAdapter(getContext(), new ArrayList<LogItem>(), false, new ClickableListener() {
            @Override
            public void onClick(int position) {
                mService.deleteLogItem(adapter1.getItem(position).logbookId);
            }
        });
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView1.setAdapter(adapter1);

        // Recycler view 2
        mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.log_view2);
        adapter2 = new LogAdapter(getContext(), new ArrayList<LogItem>(), false, new ClickableListener() {
            @Override
            public void onClick(int position) {
                mService.deleteLogItem(adapter2.getItem(position).logbookId);
            }
        });
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView2.setAdapter(adapter2);

        // Recycler view 3
        mRecyclerView3 = (RecyclerView) rootView.findViewById(R.id.log_view3);
        adapter3 = new LogAdapter(getContext(), new ArrayList<LogItem>(), false, new ClickableListener() {
            @Override
            public void onClick(int position) {
                mService.deleteLogItem(adapter3.getItem(position).logbookId);
            }
        });
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView3.setAdapter(adapter3);

        // Recycler view 4
        mRecyclerView4 = (RecyclerView) rootView.findViewById(R.id.log_view4);
        adapter4 = new LogAdapter(getContext(), new ArrayList<LogItem>(), false, new ClickableListener() {
            @Override
            public void onClick(int position) {
                mService.deleteLogItem(adapter4.getItem(position).logbookId);
            }
        });
        mRecyclerView4.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView4.setAdapter(adapter4);

        // Recycler view 5
        mRecyclerView5 = (RecyclerView) rootView.findViewById(R.id.log_view5);
        adapter5 = new LogAdapter(getContext(), new ArrayList<LogItem>(), true, new ClickableListener() {
            @Override
            public void onClick(int position) {
                mService.deleteLogItem(adapter5.getItem(position).logbookId);
            }
        });
        mRecyclerView5.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView5.setAdapter(adapter5);
    }

    // All the callbacks
    Callback<Logbook> getLogbook = new Callback<Logbook>() {
        @Override
        public void onResponse(Response<Logbook> response) {
            hideSpinner();
            if (response.isSuccess() && response.body() != null) {
                logbookView.setVisibility(View.VISIBLE);
                logbookId = response.body().id;
                Log.e("Items", logbookId);
                mService.getAllLogbookItems(logbookId).enqueue(getAllItems);
            } else if (today.equals(pickedDate)) {
                mService.postLog(userId).enqueue(getLogbook);
            } else {
                logbookView.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.can_not_create_logbook, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            //something went wrong
            hideSpinner();
            logbookView.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_LONG).show();
        }

    };

    Callback<List<LogItem>> getAllItems = new Callback<List<LogItem>>() {

        @Override
        public void onResponse(final Response<List<LogItem>> response) {
            if (response.isSuccess() && response.body().size() > 0) {
                for(LogItem item : response.body()) {
                    switch (item.category) {
                        case breakfast:
                            adapter1.addItem(item);
                            break;
                        case lunch:
                            adapter2.addItem(item);
                            break;
                        case dinner:
                            adapter3.addItem(item);
                            break;
                        case snacks:
                            adapter4.addItem(item);
                            break;
                        case workout:
                            adapter5.addItem(item);
                            break;
                    }
                }

                mRecyclerView1.animate().alpha(1);
                mRecyclerView2.animate().alpha(1);
                mRecyclerView3.animate().alpha(1);
                mRecyclerView4.animate().alpha(1);
                mRecyclerView5.animate().alpha(1);
            } else {
                Log.e("Items", "No content");
            }
        }

        @Override
        public void onFailure(Throwable t) {
            //something went wrong
            hideSpinner();
            Toast.makeText(getContext(), R.string.failure, Toast.LENGTH_LONG).show();
        }
    };

    Callback<LogItem> itemSuccess = new Callback<LogItem>() {
        @Override
        public void onResponse(Response<LogItem> response) {
            if (response.isSuccess() && response.body() != null) {
                Toast.makeText(getContext(), R.string.log_item_success, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
        }
    };

    Callback<Logbook> moodSuccess = new Callback<Logbook>() {
        @Override
        public void onResponse(Response<Logbook> response) {
//            if (response.isSuccess() && response.body() != null) {}
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_LONG).show();
        }
    };
}
