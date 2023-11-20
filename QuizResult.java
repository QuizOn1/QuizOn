package com.example.informaticapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;


public class QuizResult extends AppCompatActivity {

    private List<QuestionsList> questionsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // getting widgets from activity_quiz_result.xml file
        final TextView scoreTV = findViewById(R.id.scoreTV);
        final TextView totalScoreTV = findViewById(R.id.totalScoreTV);
        final TextView correctTV = findViewById(R.id.correctTV);
        final TextView incorrectTV = findViewById(R.id.inCorrectTV);
        final AppCompatButton reTakeBtn = findViewById(R.id.reTakeQuizBtn);

        // getting questions list from MainActivity
        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("quetions");

        // setting data to TextViews
        totalScoreTV.setText("/"+questionsLists.size());
        scoreTV.setText(getCorrectAnswers() +"");
        correctTV.setText(getCorrectAnswers() + "");
        incorrectTV.setText(String.valueOf(questionsLists.size() - getCorrectAnswers()));


        reTakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // re start quiz go to MainActivity
                startActivity(new Intent(QuizResult.this, MenuQuiz.class));
                finish();
            }
        });
    }

    private int getCorrectAnswers(){

        int correctAnswer = 0;

        for(int i = 0; i < questionsLists.size(); i++){

            int getUserSelectedOption = questionsLists.get(i).getUserSelectedAnswer(); // get user selected option
            int getQuestionAnswer = questionsLists.get(i).getAnswer(); //  get correct answer for the question

            // check of user selected answer matches with correct answer
            if(getQuestionAnswer == getUserSelectedOption){
                correctAnswer++; //  increase correct answers
            }
        }

        return correctAnswer;
    }
}