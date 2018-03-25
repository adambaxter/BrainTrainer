package com.spryfieldsoftwaresolutions.android.braintrainer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Adam Baxter on 20/03/18.
 * <p>
 * An object that will create a question.
 */

class Question{
    private static int mFirstNumber;
    private static int mSecondNumber;
    private static int mAnswer;
    private static ArrayList<Integer> mOptions;
    private static ArrayList<Integer> mRandomizedAnswers;
    private static Random mRng;


    /**
     * Creates a new question object, with a new instance
     * of Random.
     */
    Question() {
        mRng = new Random();
    }

    /**
     * Generate all the numbers needed for
     * a question.
     **/
    private static void generateNumbers() {
        if (mRng == null) {
            mRng = new Random();
        }
        mFirstNumber = mRng.nextInt(39) + 1;
        mSecondNumber = mRng.nextInt(39) + 1;
        mAnswer = mFirstNumber + mSecondNumber;
        mOptions = new ArrayList<>();
        mOptions.add(mAnswer);
        int min = mAnswer - 10;
        int max = mAnswer + 10;
        while (mOptions.size() < 4){
            int rdm = mRng.nextInt((max - min) + 1) + min;
            if (!mOptions.contains(rdm)){
                mOptions.add(rdm);
            }
        }
    }

    static Boolean checkAnswer(ArrayList<Integer> question, int answer) {
        return (question.get(2) == answer);
    }

    /**
     * Randomize the answer options so the correct answer
     * isn't in the same position every time.
     */
    private static void randomizeAnswers() {
        mRandomizedAnswers = new ArrayList<>();
        while (mRandomizedAnswers.size() < 4) {
            int random = mRng.nextInt(4);
            if (!mRandomizedAnswers.contains(mOptions.get(random))) {
                mRandomizedAnswers.add(mOptions.get(random));
            }
        }
    }

    /** Generates everything needed for an question including the question
     * correct answer and three wrong answers. Everything is in an arraylist.
     *
     * @return ArrayList<Integer> of all ints needed for a question
     */
    ArrayList<Integer> generateQuestion() {
        generateNumbers();
        randomizeAnswers();

        ArrayList<Integer> mQuestion; mQuestion = new ArrayList<>();

        mQuestion.add(mFirstNumber);
        mQuestion.add(mSecondNumber);
        mQuestion.add(mAnswer);
        mQuestion.add(mRandomizedAnswers.get(0));
        mQuestion.add(mRandomizedAnswers.get(1));
        mQuestion.add(mRandomizedAnswers.get(2));
        mQuestion.add(mRandomizedAnswers.get(3));

        return mQuestion;
    }
}
