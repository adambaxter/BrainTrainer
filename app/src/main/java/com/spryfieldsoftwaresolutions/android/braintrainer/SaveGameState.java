package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adam Baxter on 21/03/18.
 *
 * Save the current game state.
 */

public class SaveGameState implements Parcelable {
    private static SaveGameState sGameState;
    private static ScoreBoard sScoreBoard;
    private static long sMillisRemaining;

    /** Get sGameState or create a new one. Ensures
     * only one instance
     *
     * @return Current gamestate
     */
    static SaveGameState get(){
        if(sGameState == null) {
            sGameState = new SaveGameState();
        }
        return sGameState;
    }

    /** Private constructor. */
    private SaveGameState() {
        sScoreBoard = null;
        sMillisRemaining = -1;
    }

    /** Takes in the games current scoreboard and
     * saves it to the game state
     *
     * @param scoreBoard Current game score board
     */
    static void saveScoreBoard(ScoreBoard scoreBoard){
        sScoreBoard = scoreBoard;
    }

    /** Restores the saved score board when onSaveInstanceState()
     * is called.
     *
     * @return ScoreBoard
     */
    static ScoreBoard restoreScoreboard(){
        return sScoreBoard;
    }

    /** Saves the amount of milliseconds remaining
     * in the current timer.
     *
     * @param millisRemaining millis remaining in the timer at time of
     *                        onSaveInstanceState being called.
     */
    static void saveTimeRemaining(long millisRemaining){
        sMillisRemaining = millisRemaining;
    }

    /** Restores the time remaining on the timer when
     * onSaveInstanceState() was called.
     *
     * @return milliseconds remaining
     */
    static long restoreTimeRemaining(){
        return sMillisRemaining;
    }

    /** Resets game state when a new game is started */
    static void resetGameState(){
        sScoreBoard = null;
        sMillisRemaining = 0;
    }

    /** COnstructor used to make SaveGameState parcelable
     *
     * @param in parcel used to restore game state
     */
    private SaveGameState(Parcel in){
        sScoreBoard = in.readParcelable(ScoreBoard.class.getClassLoader());
        sMillisRemaining = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeParcelable(sScoreBoard, 0);
        dest.writeLong(sMillisRemaining);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SaveGameState> CREATOR = new Parcelable.Creator<SaveGameState>(){
        public SaveGameState createFromParcel(Parcel in) {
            return new SaveGameState(in);
        }

        public SaveGameState[] newArray(int size) {
            return new SaveGameState[size];
        }
    };
}
