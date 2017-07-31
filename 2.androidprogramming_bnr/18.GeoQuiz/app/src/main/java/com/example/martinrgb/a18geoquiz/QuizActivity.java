package com.example.martinrgb.a18geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


//Activity本身就是Context的子类
public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    //用来在生命周期切换保存数据
    private static final String KEY_INDEX = "index";

    private static final int REQUEST_CODE_CHEAT = 0;

    @Bind(R.id.questiontext)
    TextView mQuestionText;
    @Bind(R.id.truebutton)
    Button mTrueButton;
    @Bind(R.id.falsebutton)
    Button mFalseButton;
    @Bind(R.id.next_button)
    ImageButton mNextButton;
    @Bind(R.id.prev_button)
    ImageButton mPrevButton;
    @Bind(R.id.cheat_button)
    Button mCheatButton;

    final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果没有收集到RESULT_OK，说明没有作弊
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        //如果转场过去了
        if (requestCode == REQUEST_CODE_CHEAT) {
            //如果没集到了作弊信息,跳出来
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //在生命周期结束后，保留mCurrentIndex;
        saveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        setQuestion();
        Log.d(TAG, "onCreate(Bundle) called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(Bundle) called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause(Bundle) called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume(Bundle) called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(Bundle) called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy(Bundle) called");
    }

    private void setQuestion() {
        //抛出一个一异常，方便检查
        //通过抛出异常时，检测"表象"是否真的异常，如果真的异常，找到具体抛出异常的地方，进行检查
        //Log.d(TAG,"Updating question text for question #" + mCurrentIndex,new Exception());

        //这里用的是 resId 方法
        int mQuestion = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionText.setText(mQuestion);
    }

    @OnClick({R.id.truebutton, R.id.falsebutton, R.id.next_button, R.id.prev_button,R.id.cheat_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.truebutton:
                checkAnswer(true);
                break;
            case R.id.falsebutton:
                checkAnswer(false);
                break;
            case R.id.next_button:
                indexCarouselNext();
                setQuestion();
                mIsCheater = false;
                break;
            case R.id.prev_button:
                indexCarouselPrev();
                setQuestion();
                break;
            case R.id.cheat_button:
//                Intent i = new Intent(getApplicationContext(),CheatActivity.class);
//                startActivity(i);

                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                //向CheatActivity传递本题的正确答案;
                Intent i = CheatActivity.newIntent(getApplicationContext(),answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);

                break;
            default:
        }
    }

    //检查答案是否正确
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        //用一个int保存 resId
        int messageResId = 0;

        //如果对了，选了对，下一题，如果错了，选了错，下一题

        if(mIsCheater){

            messageResId = R.string.judgment_toast;

        }else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                indexCarouselNext();
                setQuestion();
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }


        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    //index轮播
    private void indexCarouselNext() {
        if (mCurrentIndex < 4) {
            mCurrentIndex++;
        } else {
            mCurrentIndex = 0;
        }

    }

    private void indexCarouselPrev() {
        if (mCurrentIndex > 0) {
            mCurrentIndex--;
        } else {
            mCurrentIndex = 4;
        }
    }

}
