package com.example.jgz.jgzafvalapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jgz.jgzafvalapp.models.Answer;
import com.example.jgz.jgzafvalapp.models.ChallengeItem;
import com.example.jgz.jgzafvalapp.models.Question;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;

public class QuizFragment extends Fragment implements Callback<List<Question>> {

    private View rootView;

    private View startQuizLayout;
    private View questionLayout;
    private View resultLayout;

    private QuizAdapter adapter;
    private RecyclerView mRecyclerView;
    private ApiServices mService;
    private ProgressBar mProgress;

    private TableLayout scoreList;
    private Button startQuiz;
    private Button nextQuestion;
    private int selectedQuestion;
    private int currentQuestion = 0;
    private TextView questionTitle;
    private ArrayList<Question> questions;
    private ArrayList<Answer> myAnswers;
    private TextView myScore;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance() {
        QuizFragment fragment = new QuizFragment();
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
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        startQuizLayout = rootView.findViewById(R.id.start_quiz_layout);
        questionLayout = rootView.findViewById(R.id.question_layout);
        resultLayout = rootView.findViewById(R.id.result_layout);

        scoreList = (TableLayout) rootView.findViewById(R.id.score_list);
        fillTable();

        questionTitle = (TextView) rootView.findViewById(R.id.question_title);

        startQuiz = (Button) rootView.findViewById(R.id.start_quiz_button);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizLayout.setVisibility(View.GONE);
                questionLayout.setVisibility(View.VISIBLE);
                nextQuestion();
            }
        });

        myScore = (TextView) rootView.findViewById(R.id.result_score);

        nextQuestion = (Button) rootView.findViewById(R.id.next_question_button);
        nextQuestion.setEnabled(false);
        nextQuestion.setBackgroundColor(Color.parseColor("#8000A3E0"));
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAnswers.add(adapter.getItem(selectedQuestion));
                if(currentQuestion == questions.size()) {
                    resultLayout.setVisibility(View.VISIBLE);
                    questionLayout.setVisibility(View.GONE);
                    myScore.setText(String.valueOf(getScore()));
                    getActivity().setTitle(getString(R.string.result));
                } else {
                    nextQuestion();
                }
            }
        });

        questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Appel", false));
        answers.add(new Answer("Banaan", false));
        answers.add(new Answer("Citroen", false));
        answers.add(new Answer("Schoenen", true));
        questions.add(new Question("Iets", answers));
        questions.add(new Question("Waar smaken marshmallows naar?", answers));
        myAnswers = new ArrayList<>();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.answers_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);

        adapter = new QuizAdapter(getContext(), new ArrayList<Answer>(), new ClickableListener() {
            @Override
            public void onClick(int position) {
                Answer selected = adapter.getItem(position);

                if (selected.selected) {
                    selectedQuestion = position;
                    nextQuestion.setEnabled(true);
                    nextQuestion.setBackgroundResource(R.color.colorLightBlue);
                } else {
                    nextQuestion.setEnabled(false);
                    nextQuestion.setBackgroundColor(Color.parseColor("#8000A3E0"));
                }
            }
        });

        mRecyclerView.setAdapter(adapter);

        mProgress = (ProgressBar) rootView.findViewById(R.id.activity_progress);
        hideSpinner();

        return rootView;
    }

    private void fillTable() {
        for (int i = 0; i <2; i++) {
            TableRow row = (TableRow) getLayoutInflater(null).inflate(R.layout.tablerow_layout, null);
            TextView week = (TextView) row.findViewById(R.id.week_content);
            TextView score = (TextView) row.findViewById(R.id.score_content);
            week.setText("1");
            score.setText("100");
            scoreList.addView(row,i + 1);
        }
    }

    @Override
    public void onResponse(Response<List<Question>> response) {
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

//        mService.getLog(1);
    }

    private int getScore() {
        int score = 0;
        for (Answer answer:myAnswers) {
            if(answer.rightAnswer) score = score + 10;
        }
        return score;
    }

    private void nextQuestion() {
        adapter.clearList();
        questionTitle.setText(questions.get(currentQuestion).question);
        adapter.addItems(questions.get(currentQuestion).answers);
        currentQuestion++;
        getActivity().setTitle(String.format(getString(R.string.question_number), currentQuestion));
    }
}
