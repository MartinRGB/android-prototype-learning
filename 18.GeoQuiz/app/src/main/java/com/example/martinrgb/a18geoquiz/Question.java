package com.example.martinrgb.a18geoquiz;

/**
 * Created by MartinRGB on 2017/2/22.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    //存取方法，避免手工输入
    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }



    public Question(int textResId,boolean answerTrue){

        mTextResId = textResId;
        mAnswerTrue = answerTrue;

    }
}
