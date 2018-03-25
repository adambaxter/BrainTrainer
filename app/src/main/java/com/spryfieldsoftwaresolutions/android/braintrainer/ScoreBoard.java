package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adam Baxter on 19/03/18.
 *
 * Class that will handle keeping track of, and displaying
 * the score
 */

class ScoreBoard implements Parcelable {
    private int mCorrectAnswers;
    private int mQuestionsAsked;

    /** Constructor creates a new scoreboard, and displays
     * current score in score text view.
     **/
    ScoreBoard() {
        mCorrectAnswers = 0;
        mQuestionsAsked = 0;
    }

    /** Updates score text view with the current score.
     * Should call after every question the user answers.
     *
     *
     **/
    String updateScore() {
        return String.format("%s/%s", mCorrectAnswers, mQuestionsAsked);
    }

    /** Updates correct answers and questions asked
     *
     * @param isCorrect whether question was answer was correct.
     */
    void questionComplete(Boolean isCorrect){
        if(isCorrect){
            mCorrectAnswers++;
        }
        mQuestionsAsked++;
    }
    /** Calculates a score as a percent, used to display score
     * on the game over screen.
     **/
    int calculatePercent() {
        if(mQuestionsAsked == 0){
            return 0;
        }else {
            return (int) (((double) mCorrectAnswers / mQuestionsAsked) * 100);
        }
    }

    /** Resets scoreboard at the start of a new game **/
    void reset() {
        mCorrectAnswers = 0;
        mQuestionsAsked = 0;
       // updateScore();
    }

    private ScoreBoard(Parcel in){
        mCorrectAnswers = in.readInt();
        mQuestionsAsked = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCorrectAnswers);
        dest.writeInt(mQuestionsAsked);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ScoreBoard> CREATOR = new Parcelable.Creator<ScoreBoard>(){
        public ScoreBoard createFromParcel(Parcel in){
            return new ScoreBoard(in);
        }

        public ScoreBoard[] newArray(int size) {
            return new ScoreBoard[size];
        }
    };
}
