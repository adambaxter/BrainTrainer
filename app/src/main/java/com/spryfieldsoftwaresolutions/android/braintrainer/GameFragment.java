package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Adam Baxter on 19/03/18.
 * <p>
 * Creates a fragment that will hold the game.
 */

public class GameFragment extends Fragment {

    private ScoreBoard mScoreBoard;
    private ArrayList<Integer> mQuestion;
    private long mTimeRemaining;
    private int mTimerLength;
    private CountDownTimer mTimer;
    private MediaPlayer mMediaPlayer;

    private final String KEY_GAMESTATE = "savedGameState";


    /**
     * Creates a new instance of GameFragment
     *
     * @return A new instance of Game Fragment
     */
    public static GameFragment newInstance() {
        return new GameFragment();

    }

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState used to save the state of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuestionList mQuestionList = QuestionList.get();
        mQuestionList.generateQuestionList();
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.click);
        mMediaPlayer.setVolume(.15f, .15f);


        if (savedInstanceState != null && SaveGameState.restoreScoreboard() != null) {
            savedInstanceState.getParcelable(KEY_GAMESTATE);
            mScoreBoard = SaveGameState.restoreScoreboard();
            mTimeRemaining = SaveGameState.restoreTimeRemaining();
        } else {
            mScoreBoard = new ScoreBoard();
            SaveGameState.get();
        }

    }

    /**
     * Called to create and inflate the view
     *
     * @param inflater             Inflates the given layout
     * @param container            ViewGroup that the layout is inflated to
     * @param onSavedInstanceState saved state of the fragment
     * @return A view with layout inflated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        final GridLayout grid = view.findViewById(R.id.grid_layout);
        final ConstraintLayout startGameLayout = view.findViewById(R.id.start_game_layout);
        final ConstraintLayout gameOverScreen = view.findViewById(R.id.game_over_screen);
        final TextView timerTextView = view.findViewById(R.id.timer);
        final TextView scoreBoardView = view.findViewById(R.id.score);
        final TextView questionTextView = view.findViewById(R.id.question);
        final TextView box00TextView = view.findViewById(R.id.box_00_textView);
        final TextView box01TextView = view.findViewById(R.id.box_01_textView);
        final TextView box10TextView = view.findViewById(R.id.box_10_textView);
        final TextView box11TextView = view.findViewById(R.id.box_11_textView);
        final Button startGameButton = view.findViewById(R.id.start_game_button);
        final Button newGameButton = view.findViewById(R.id.new_game_button);
        startGameLayout.bringToFront();
        questionTextView.setVisibility(View.GONE);

        scoreBoardView.setText(mScoreBoard.updateScore());

        mQuestion = QuestionList.displayQuestion(questionTextView, box00TextView, box01TextView,
                box10TextView, box11TextView);

        /* When start button is clicked, hide the start game layout so gridlayout is
         * visible, create a new timer and start the questions.
         *
         * timerLength = length of the game (30 seconds)
         * interval = the interval that the time updates the timerView (every second)
         **/
        //final int timerLength;
        setTimerLength();

        final int interval = 1000;
        setInitialTimerView(timerTextView, mTimerLength);
        SaveGameState.saveTimeRemaining(mTimerLength);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                mTimer = new Timer(getContext(), timerTextView, mScoreBoard, gameOverScreen, mTimerLength, interval).start();
                startGameLayout.setVisibility(View.GONE);
                questionTextView.setVisibility(View.VISIBLE);
                scoreBoardView.setText(mScoreBoard.updateScore());

            }
        });

        /* When new Game button is clicked, hide the game over screen, and start
         * a new game.
         **/
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.start();
                SaveGameState.resetGameState();
                mTimeRemaining = SaveGameState.restoreTimeRemaining();
                setTimerLength();
                resetGame(timerTextView, scoreBoardView, gameOverScreen, mTimerLength, interval, mScoreBoard);
                mQuestion = QuestionList.displayQuestion(questionTextView, box00TextView, box01TextView,
                        box10TextView, box11TextView);
            }
        });


        /* Get the number of children of grid and set a listener on each.
         * Will be used for the user to select an answer.
         **/
        int childCount = grid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final TextView child = (TextView) grid.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMediaPlayer.start();
                    mScoreBoard.questionComplete(
                            Question.checkAnswer(mQuestion, Integer.parseInt(child.getText().toString())));
                    mQuestion = QuestionList.displayQuestion(questionTextView, box00TextView, box01TextView,
                            box10TextView, box11TextView);
                    scoreBoardView.setText(mScoreBoard.updateScore());
                    SaveGameState.saveScoreBoard(mScoreBoard);
                }
            });


        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (SaveGameState.restoreScoreboard() != null) {
            savedInstanceState.putParcelable(KEY_GAMESTATE, SaveGameState.get());
        }
        if(mTimer != null) {

            mTimer.cancel();
        }
    }

    /**
     * Resets the time and hides the game over screen when new game on
     * game over screen
     *
     * @param timerTextView  View that holds a timer object
     * @param gameOverScreen View that displays when the game has ended
     * @param timerLength    Duration of timer in milliseconds
     * @param interval       Interval the timer updates the view in milliseconds
     * @param scoreBoard     Scoreboard object that displays the current score
     */
    private void resetGame(TextView timerTextView, TextView scoreboardView, ConstraintLayout gameOverScreen,
                           long timerLength, long interval, ScoreBoard scoreBoard) {
        new Timer(getContext(), timerTextView, scoreBoard, gameOverScreen, timerLength, interval).start();
        gameOverScreen.setVisibility(View.GONE);
        setInitialTimerView(timerTextView, timerLength);
        scoreBoard.reset();
        scoreboardView.setText(scoreBoard.updateScore());
        SaveGameState.resetGameState();

    }

    /**
     * Resets the timerTextView to the timer length on first run and start
     * of new game
     *
     * @param timerTextView TextView that holds the timer
     * @param timerLength   Duration of timer in milliseconds
     */
    private void setInitialTimerView(TextView timerTextView, long timerLength) {
        int mins = (int) timerLength / 60000;
        int seconds = (int) (timerLength / 1000) % 60;

        String formattedTime = String.format(Locale.CANADA, "%d:%02d", mins, seconds);
        timerTextView.setText(String.format(getResources().getString(R.string.timer_initial_text), formattedTime));
        SaveGameState.saveTimeRemaining(timerLength);
    }

    /** Sets the timer length of new timers */
    private void setTimerLength(){
        if(mTimeRemaining > 0){
            mTimerLength = (int) mTimeRemaining;
        }else {
            mTimerLength = 30000;
        }
    }

}
