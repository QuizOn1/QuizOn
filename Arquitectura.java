package com.example.informaticapp;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Arquitectura extends AppCompatActivity {

    // questionsLists to store questions data
    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView quizTimer;

    private RelativeLayout option1Layout, option2Layout, option3Layout, option4Layout;
    private TextView option1TV, option2TV, option3TV, option4TV;
    private ImageView option1Icon, option2Icon, option3Icon, option4Icon;

    private TextView questionTV;

    private TextView totalQuestionTV;
    private TextView currentQuestion;

    // creating Database Reference from URL
    private DatabaseReference databaseReference;

    // CountDown Timer for Quiz
    private CountDownTimer countDownTimer;

    // current question position. By default 0 (First Question)
    private int currentQuestionPosition = 0;

    // selected option number. Value must be between 1-4 (We have 4 options). 0 means no option is selected
    private int selectedOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquitectura);

        // getting widgets from activity_main.xml file
        quizTimer = findViewById(R.id.quizTimer);
        option1Layout = findViewById(R.id.option1Layout);
        option2Layout = findViewById(R.id.option2Layout);
        option3Layout = findViewById(R.id.option3Layout);
        option4Layout = findViewById(R.id.option4Layout);

        option1TV = findViewById(R.id.option1TV);
        option2TV = findViewById(R.id.option2TV);
        option3TV = findViewById(R.id.option3TV);
        option4TV = findViewById(R.id.option4TV);

        option1Icon = findViewById(R.id.option1Icon);
        option2Icon = findViewById(R.id.option2Icon);
        option3Icon = findViewById(R.id.option3Icon);
        option4Icon = findViewById(R.id.option4Icon);

        questionTV = findViewById(R.id.questionTV);

        totalQuestionTV = findViewById(R.id.totalQuestionsTV);
        currentQuestion = findViewById(R.id.currentQuestionTV);

        final AppCompatButton nextBtn = findViewById(R.id.nextQuestionBtn);

        // getting firebase realtime database URL from strings
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://baseinformaticapp-default-rtdb.firebaseio.com/");

        // show interstitial ad



        // show instructions dialog

        // getting data(questions and quiz time) from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // getting quiz time in seconds
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                // getting questions data from firebase realtime database
                for (DataSnapshot questions : snapshot.child("questions").getChildren()) {

                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                    int getAnswer = Integer.parseInt(questions.child("answer").getValue(String.class));

                    // im using String here to database value must be in String form

                    // creating questions list object and add details
                    final QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2, getOption3, getOption4, getAnswer);

                    // adding questionsList object into the list
                    questionsLists.add(questionsList);
                }

                // setting total questions to TextView
                totalQuestionTV.setText("/" + questionsLists.size());

                // start quiz timer and pass max time in seconds
                startQuizTimer(getQuizTime);

                // select first question by default
                selectQuestion(currentQuestionPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String errorMessage = error.getMessage();
                Toast.makeText(Arquitectura.this, "No se pudo obtener los datos de Firebase: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // assign 1 as first option is selected
                selectedOption = 1;

                // select option
                selectOption(option1Layout, option1Icon);
            }
        });

        option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // assign 2 as second option is selected
                selectedOption = 2;

                // select option
                selectOption(option2Layout, option2Icon);
            }
        });
        option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign 3 as third option is selected
                selectedOption = 3;

                // select option
                selectOption(option3Layout, option3Icon);
            }
        });
        option4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // assign 4 as fourth option is selected
                selectedOption = 4;

                // select option
                selectOption(option4Layout, option4Icon);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if user has select an option or not
                if (selectedOption != 0) {

                    // set user selected answer
                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);

                    // reset selected option to default value (0)
                    selectedOption = 0;
                    currentQuestionPosition++; // increase current question value to getting next question

                    // check if list has more questions
                    if (currentQuestionPosition < questionsLists.size()) {
                        selectQuestion(currentQuestionPosition); //  select question /  next question
                    } else {

                        // list has no questions left so finish the quiz
                        countDownTimer.cancel();  // stop countdown timer
                        finishQuiz(); // finish quiz
                    }
                } else {
                    Toast.makeText(Arquitectura.this, "Por favor selecciona una opcion", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void finishQuiz() {

        // creating intent to open QuizResult activity
        Intent intent = new Intent(Arquitectura.this, QuizResult.class);

        // creating bundle to pass quizQuestionsLists
        Bundle bundle = new Bundle();
        bundle.putSerializable("quetions", (Serializable) questionsLists);

        // add bundle to intent
        intent.putExtras(bundle);

        // start activity to open QuizResult activity
        startActivity(intent);

        // destroy current activity
        finish();
    }

    private void startQuizTimer(int maxTimeInSeconds) {

        countDownTimer = new CountDownTimer(maxTimeInSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSecond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String generateTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                        getSecond - TimeUnit.MINUTES.toSeconds(getMinute));

                quizTimer.setText(generateTime);
            }

            @Override
            public void onFinish() {

                // finish quiz when time i finished
                finishQuiz();
            }
        };

        // start timer
        countDownTimer.start();
    }

    private void selectQuestion(int questionListPosition) {

        // reset options fro new question
        resetOptions();

        // getting question details and set to TextViews
        questionTV.setText(questionsLists.get(questionListPosition).getQuestion());
        option1TV.setText(questionsLists.get(questionListPosition).getOption1());
        option2TV.setText(questionsLists.get(questionListPosition).getOption2());
        option3TV.setText(questionsLists.get(questionListPosition).getOption3());
        option4TV.setText(questionsLists.get(questionListPosition).getOption4());

        // setting current question number to TextView
        currentQuestion.setText("Question" + (questionListPosition + 1));
    }

    private void resetOptions() {

        option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        option1Icon.setImageResource(R.drawable.round_back_white50_100);
        option2Icon.setImageResource(R.drawable.round_back_white50_100);
        option3Icon.setImageResource(R.drawable.round_back_white50_100);
        option4Icon.setImageResource(R.drawable.round_back_white50_100);
    }

    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon) {

        // reset options to select new option
        resetOptions();

        selectedOptionIcon.setImageResource(R.drawable.check_icon);
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);
    }
}