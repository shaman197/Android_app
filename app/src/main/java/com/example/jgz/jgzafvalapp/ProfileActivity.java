package com.example.jgz.jgzafvalapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class ProfileActivity extends AppCompatActivity {

    private final String NOTIFICATIONS = "Notifications";
    SharedPreferences sharedPref;

    AlarmManager alarmManager;
    private TextView myName, myBirthDate, myLength, myGender, myWeight;
    private Switch breakfastTitle, lunchTitle, dinnerTitle, challengeTitle;
    private TextView breakfastSubtitle, lunchSubtitle, dinnerSubtitle, challengeSubtitle;
    private TimePickerDialog timePickerDialog;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.profile));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent loginIntent;
        loginIntent = getIntent();

        Account myAccount = new Account(loginIntent.getStringExtra(LoginActivity.EMAIL), LoginActivity.ACCOUNT_TYPE);
        myName = (TextView) findViewById(R.id.my_name);
        myBirthDate = (TextView) findViewById(R.id.my_birth_date);
        myLength = (TextView) findViewById(R.id.my_length);
        myGender = (TextView) findViewById(R.id.my_gender);
        myWeight = (TextView) findViewById(R.id.my_weight);

        String name = AccountManager.get(this).getUserData(myAccount, LoginActivity.NAME);
        String birthDate = AccountManager.get(this).getUserData(myAccount, LoginActivity.BIRTHDATE);
        String length = AccountManager.get(this).getUserData(myAccount, LoginActivity.LENGTH);
        String gender = AccountManager.get(this).getUserData(myAccount, LoginActivity.GENDER);
        String weight = AccountManager.get(this).getUserData(myAccount, LoginActivity.WEIGHT);

        myName.setText(getString(R.string.my_name, name));
        myBirthDate.setText(getString(R.string.my_birth_date, birthDate));
        myLength.setText(getString(R.string.my_length, length));
        myGender.setText(getString(R.string.my_gender, gender));
        myWeight.setText(getString(R.string.my_weight, weight));

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        logoutButton = (Button) findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ProfileActivity.this.finish();
            }
        });

        sharedPref = getSharedPreferences(NOTIFICATIONS, MODE_PRIVATE);

        breakfastTitle = (Switch) findViewById(R.id.breakfast_title);
        lunchTitle = (Switch) findViewById(R.id.lunch_title);
        dinnerTitle =(Switch) findViewById(R.id.dinner_title);
        challengeTitle = (Switch) findViewById(R.id.challenges_title);

        breakfastSubtitle = (TextView) findViewById(R.id.breakfast_subtitle);
        lunchSubtitle = (TextView) findViewById(R.id.lunch_subtitle);
        dinnerSubtitle =(TextView) findViewById(R.id.dinner_subtitle);
        challengeSubtitle = (TextView) findViewById(R.id.challenges_subtitle);

        this.setData();

        breakfastTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((Switch) view).isChecked()) {
                    breakfastSubtitle.setVisibility(View.VISIBLE);
                    createTimePickerDialog(8, 00, breakfastSubtitle, getString(R.string.breakfast),
                            getString(R.string.breakfast_notification), 1);
                }
                else {
                    breakfastSubtitle.setVisibility(View.INVISIBLE);
                    cancelNotification(1);
                }
            }
        });

        lunchTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((Switch) view).isChecked()) {
                    lunchSubtitle.setVisibility(View.VISIBLE);
                    createTimePickerDialog(12, 00, lunchSubtitle, getString(R.string.lunch),
                            getString(R.string.lunch_notification), 2);
                }
                else {
                    lunchSubtitle.setVisibility(View.INVISIBLE);
                    cancelNotification(2);
                }
            }
        });

        dinnerTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((Switch) view).isChecked()) {
                    dinnerSubtitle.setVisibility(View.VISIBLE);
                    createTimePickerDialog(18, 00, dinnerSubtitle, getString(R.string.dinner),
                            getString(R.string.dinner_notification), 3);
                }
                else {
                    dinnerSubtitle.setVisibility(View.INVISIBLE);
                    cancelNotification(3);
                }
            }
        });

        challengeTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((Switch) view).isChecked()) {
                    challengeSubtitle.setVisibility(View.VISIBLE);
                    createTimePickerDialog(10, 00, challengeSubtitle, getString(R.string.challenges),
                            getString(R.string.challenge_notification), 4);
                }
                else {
                    challengeSubtitle.setVisibility(View.INVISIBLE);
                    cancelNotification(4);
                }
            }
        });
    }

    private void createTimePickerDialog(int startHour, int startMinute, final TextView subTitle,
                                        final String title, final String text, final int id) {
        timePickerDialog = new TimePickerDialog(ProfileActivity.this, R.style.OverlayPrimaryBlue,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        subTitle.setText(String.format(getString(R.string.daily_around), hourOfDay + ":" +
                                new DecimalFormat("00").format(minute)));

                        // notification
                        scheduleNotification(getNotification(title, text), hourOfDay, minute, id);
                    }
                }, startHour, startMinute, true);
        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(NOTIFICATIONS, MODE_PRIVATE);
        settings.edit().clear().commit();
        SharedPreferences.Editor editor = settings.edit();

        if(breakfastTitle.isChecked())
            editor.putString("breakfastNotification", breakfastSubtitle.getText().toString());
        if(lunchTitle.isChecked())
            editor.putString("lunchNotification", lunchSubtitle.getText().toString());
        if(dinnerTitle.isChecked())
            editor.putString("dinnerNotification", dinnerSubtitle.getText().toString());
        if(challengeTitle.isChecked())
            editor.putString("challengeNotification", challengeSubtitle.getText().toString());
        editor.commit();
    }

    private void setData() {
        if(sharedPref.contains("breakfastNotification"))
        {
            breakfastTitle.setChecked(true);
            breakfastSubtitle.setText(sharedPref.getString("breakfastNotification", null));
        }

        if(sharedPref.contains("lunchNotification"))
        {
            lunchTitle.setChecked(true);
            lunchSubtitle.setText(sharedPref.getString("lunchNotification", null));
        }

        if(sharedPref.contains("dinnerNotification"))
        {
            dinnerTitle.setChecked(true);
            dinnerSubtitle.setText(sharedPref.getString("dinnerNotification", null));
        }

        if(sharedPref.contains("challengeNotification"))
        {
            challengeTitle.setChecked(true);
            challengeSubtitle.setText(sharedPref.getString("challengeNotification", null));
        }
    }

    private void scheduleNotification(Notification notification, int hour, int minutes, int id) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar triggerTime = Calendar.getInstance(TimeZone.getDefault());
        triggerTime.setTimeInMillis(System.currentTimeMillis());
        triggerTime.set(Calendar.HOUR_OF_DAY, hour); // maybe an hour longer cause timezone
        triggerTime.set(Calendar.MINUTE, minutes);
        triggerTime.set(Calendar.SECOND, 0);

//        Use this for testing
//        triggerTime.setTimeInMillis(System.currentTimeMillis());
//        Log.e("triggertime", String.valueOf(triggerTime.getTimeInMillis()));

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private Notification getNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this)
                //            .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher);

        return builder.build();
    }

    private void cancelNotification(int id) {
        if (alarmManager != null) {
            Intent notificationIntent = new Intent(this, NotificationPublisher.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
    }
}
