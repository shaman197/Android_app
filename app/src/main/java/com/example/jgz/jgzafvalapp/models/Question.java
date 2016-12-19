package com.example.jgz.jgzafvalapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Boy on 11-12-2016.
 */

public class Question {
    public String question;
    public List<Answer> answers;

    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }
}
