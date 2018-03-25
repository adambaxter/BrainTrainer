package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam Baxter on 20/03/18.
 *
 * Will hold the list of questions to use for the current game.
 */


class QuestionList{
    private static QuestionList sQuestionList;
    private Question mQuestionGenerator;
    private static List<ArrayList<Integer>> mQuestions;

    static QuestionList get() {
        if (sQuestionList == null) {
            sQuestionList = new QuestionList();
        }
        return sQuestionList;
    }

    /** Creates a new QuestionList and fills it with questions
     *  using generateQuestionList() method.
     */
    private QuestionList(){
        mQuestions = new ArrayList<>();
        mQuestionGenerator = new Question();
    }

    /** Adds a question to QuestionList
     *
     * @param question ArrayList of all the ints needed to display a question
     */
    private void addQuestion(ArrayList<Integer> question) {
        mQuestions.add(question);
    }

    /** Removes a question from the QuestionList
     *
     * @param index the index of the question to remove.
     */
    @SuppressWarnings("SameParameterValue")
    private void removeQuestion(int index){
        mQuestions.remove(index);
    }

    /** Pulls a question from the QuestionList, removes it, and
     * replaced it with a new question
     *
     * @return question object pulled from the list.
     */
    private ArrayList<Integer> getQuestion(){
        ArrayList<Integer> question = mQuestions.get(0);
        sQuestionList.removeQuestion(0);
        sQuestionList.addQuestion(mQuestionGenerator.generateQuestion());

        return question;
    }

    /** Returns size of the QuestionList
     *
     * @return int size of the QuestionList
     */
    private int size(){
        return mQuestions.size();
    }

    /** Adds a number (set by questionCount) of questions to questionList.
     *
     */
    void generateQuestionList(){
        int questionCount = 5;
        while(sQuestionList.size() <= questionCount){
            sQuestionList.addQuestion(mQuestionGenerator.generateQuestion());
        }
    }

    /** Display a question in Game Fragment.
     *
     * @param questionTextView where the question will be displayed
     * @param box00 where answer option 1 will be displayed.
     * @param box01 where answer option 2 will be displayed.
     * @param box10 where answer option 3 will be displayed.
     * @param box11 where answer option 4 will be displayed.
     * @return question The question that was displayed.
     */
    static ArrayList<Integer> displayQuestion(TextView questionTextView, TextView box00,
                                TextView box01, TextView box10, TextView box11) {

        ArrayList<Integer> question = sQuestionList.getQuestion();
        questionTextView.setText(formatQuestion(question));
        box00.setText(String.valueOf(question.get(3)));
        box01.setText(String.valueOf(question.get(4)));
        box10.setText(String.valueOf(question.get(5)));
        box11.setText(String.valueOf(question.get(6)));

        return question;
    }

    /** Takes the two numbers and formats them to display as a question.
     *
     * @param question question object holding the numbers.
     * @return a formatted string.
     */
    private static String formatQuestion(ArrayList<Integer> question) {
        return question.get(0) + " + " + question.get(1);
    }
}
