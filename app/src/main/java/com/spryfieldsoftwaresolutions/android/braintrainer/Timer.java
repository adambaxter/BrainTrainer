package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Adam Baxter on 19/03/18.
 *
 * This class will create a timer for our game
 *
 * Sound effects obtained from https://www.zapsplat.com
 */

public class Timer extends CountDownTimer {

    private TextView mTimerTextView;
    private ScoreBoard mScoreBoard;
    private Context mContext;
    private ConstraintLayout mGameOverScreen;
    private MediaPlayer mMediaPlayer;

    /* Overload the constructor of CountDownTimer to take in the timerTextView and gameOverTextViews. This
     * will allow the timer access to set the text of the timer, and when the timer is complete display the
     * game over screen.
     *
     * timerTextView = view that holds the timer
     * gameOverTextView = game over screen
     * gridView = grid that holds answers
     * millis = timer length
     * countdownInterval = interval that the timer will update the timerView
     */
    Timer(Context context, TextView timerTextView, ScoreBoard scoreBoard, ConstraintLayout gameOverScreen, long millis, long countdownInterval){
        super(millis, countdownInterval);
        mContext = context;
        mTimerTextView = timerTextView;
        mScoreBoard = scoreBoard;
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.gameover);
        mMediaPlayer.setVolume(.5f, 0.5f);
        mGameOverScreen = gameOverScreen;
        mTimerTextView.setText(formatTime(millis))
        ;
    }

    /** Sets the timerTextView every second (second is the interval used,
     * but can be adjusted in countdownInterval)
     **/
    public void onTick(long millisUntilDone) {
        mTimerTextView.setText(formatTime(millisUntilDone));
        SaveGameState.saveTimeRemaining(millisUntilDone);
    }

    /** When the timer finishes, display the game over screen,
     * hides the grid, and resets the timer.
     **/
    public void onFinish() {
        mMediaPlayer.start();
        mGameOverScreen.setVisibility(View.VISIBLE);
        TextView gameOverText = mGameOverScreen.findViewById(R.id.game_over_text);
        String gameRecap = String.format(mContext.getResources().getString(R.string.game_over_recap), String.valueOf(mScoreBoard.calculatePercent()));
        gameOverText.setText(gameRecap);
        SaveGameState.saveTimeRemaining(-1);
        Log.i("onFinish", "millisRemaining: " + SaveGameState.restoreTimeRemaining());
    }

    /** Method to format a string of millis into a time format **/
    private static String formatTime(long millis){
        int mins = (int) millis / 60000;
        int seconds = (int) (millis / 1000) % 60;

        return String.format(Locale.CANADA,"%d:%02d",mins, seconds);
    }
}
