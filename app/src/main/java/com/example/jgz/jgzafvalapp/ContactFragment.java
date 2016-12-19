package com.example.jgz.jgzafvalapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ContactFragment extends Fragment {

    private View rootView;

    private Spinner spinnerSubject;
    private EditText text;
    private Button buttonSend, buttonCancel;
    private MenuItem home;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        spinnerSubject = (Spinner) rootView.findViewById(R.id.spinner_subject);
        text = (EditText) rootView.findViewById(R.id.content_text);
        buttonSend = (Button) rootView.findViewById(R.id.button_send);
        buttonCancel = (Button) rootView.findViewById(R.id.button_cancel);
        home = (MenuItem) rootView.findViewById(R.id.nav_home);
        Log.d("Item", String.valueOf(home));

        String[] items = new String[]{getString(R.string.select_subject), "Beweging", "Voeding", "Slapen", "Anders"};
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerSubject.setAdapter(adapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!spinnerSubject.getSelectedItem().toString().equals(getString(R.string.select_subject)) &&
                        !text.getText().toString().trim().equals(""))
                    sendMail();
                else
                    Toast.makeText(getContext(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerSubject.setSelection(0,true);
                text.setText("");
            }
        });

        return rootView;
    }

    public void sendMail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.reciever)});
        i.putExtra(Intent.EXTRA_SUBJECT, spinnerSubject.getSelectedItem().toString());
        i.putExtra(Intent.EXTRA_TEXT   , text.getText().toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), getString(R.string.no_clients), Toast.LENGTH_SHORT).show();
        }
    }
}
