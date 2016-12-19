package com.example.jgz.jgzafvalapp.models;

/**
 * Created by Boy on 11-12-2016.
 */
public class Answer {
    public String answer;
    public Boolean rightAnswer;
    public Boolean selected;

    public Answer(String answer, Boolean rightAnswer) {
        this.answer = answer;
        this.rightAnswer = rightAnswer;
        this.selected = false;
    }
}
