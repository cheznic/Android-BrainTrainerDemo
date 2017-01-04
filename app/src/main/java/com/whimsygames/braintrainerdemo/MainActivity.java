package com.whimsygames.braintrainerdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static final int DURATION = 60000;

    private Button startButton;
    private TextView timerTextView, challengeTextView, feedbackTextView, scoreTextView;
    private int timeRemaining = 0;
    private Random rand = new Random();
    private Button[] buttonGrid = new Button[4];
    private int answerLocation;

    private class Score {
        int correct;
        int attempted;
    }
    private Score score = new Score();

    private Handler countDownTimer;

    private void startCountDown() {
        timeRemaining = DURATION;
        countDownTimer.postDelayed(timerRunnable, 0);
    }

    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            countDownTimer.postDelayed(timerRunnable, 1000);

            timeRemaining -= 1000;
            timerTextView.setText(String.valueOf(timeRemaining / 1000) + "s");

            if (timeRemaining <= 0) {
                countDownTimer.removeCallbacks(timerRunnable);
                // present the start button
                for (Button button : buttonGrid) {
                    button.setClickable(false);
                }
                feedbackTextView.setText("Score: " + score.correct + "/" + score.attempted);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGame();
    }

    private void initGame() {
        buttonGrid[0] = (Button) findViewById(R.id.button0);
        buttonGrid[1] = (Button) findViewById(R.id.Button1);
        buttonGrid[2] = (Button) findViewById(R.id.button2);
        buttonGrid[3] = (Button) findViewById(R.id.button3);

        startButton = (Button) findViewById(R.id.startButton);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        challengeTextView = (TextView) findViewById(R.id.challengeTextView);
        feedbackTextView = (TextView) findViewById(R.id.feedbackTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);

        timerTextView.setText(DURATION / 1000 + "s");

        countDownTimer = new Handler();

        startButton.setVisibility(View.VISIBLE);

        score.correct = 0;
        score.attempted = 0;
        scoreTextView.setText(score.correct + "/" + score.attempted);

        for (Button button : buttonGrid) {
            button.setClickable(false);
        }

        challengeTextView.setText("x + y");
        feedbackTextView.setText("Good Luck!");
    }

    protected void guess(View view) {
        int tag = Integer.valueOf(view.getTag().toString());

        if (tag == answerLocation) {
            feedbackTextView.setText("Correct!");
            score.correct++;

        } else {
            feedbackTextView.setText("Sorry");
        }
        score.attempted++;
        scoreTextView.setText(Integer.toString(score.correct) + "/" + Integer.toString(score.attempted));

        resetChallenge();
    }

    private void resetChallenge() {
        int a = rand.nextInt(21);
        int b = rand.nextInt(21);
        Integer[] paddingValues = {1, 2, 3, 4, 6, 7, 8, 9};
        List<Integer> padding = new ArrayList<Integer>(Arrays.asList(paddingValues));
        int idx, wrong;

        answerLocation = rand.nextInt(4);

        for (int i = 0; i < buttonGrid.length; i++) {

            if (i == answerLocation) {
                buttonGrid[i].setText(Integer.toString(a + b));
            } else {
                idx = rand.nextInt(padding.size());
                wrong = a + b - 5 + padding.get(idx);
                padding.remove(idx);
                buttonGrid[i].setText(Integer.toString(wrong));
            }

        }

        challengeTextView.setText(a + " + " + b);
    }

    protected void start(View view) {
        // hide start button
        score.attempted = 0;
        score.correct = 0;
        scoreTextView.setText(score.correct + "/" + score.attempted);
        startCountDown();
        startButton.setVisibility(View.INVISIBLE);

        resetChallenge();
        for (Button button : buttonGrid) {
            button.setClickable(true);
        }
    }
}
