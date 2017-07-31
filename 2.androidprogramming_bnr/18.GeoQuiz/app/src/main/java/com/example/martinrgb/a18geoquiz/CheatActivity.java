package com.example.martinrgb.a18geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.martinrgb.a18geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.martinrgb.a18geoquiz.answer_shown";
    @Bind(R.id.answer_text_view)
    TextView mAnswerTextView;
    @Bind(R.id.show_answer_button)
    Button mShowAnswer;

    private boolean mAnswerIsTrue;

    //QuizActivity 转场过来调用，向 CheatActivity传递信息
    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Intent i = new Intent(context, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }


    //测试CheatActivity是否看了答案,将结果代码和Intent打包
    private void setAnswerShownResult(boolean isAnswerShown){
        //新建一个intent
        Intent data = new Intent();
        //忘intent里面堆积数据
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        //activity拿到数据
        setResult(RESULT_OK,data);
    }

    //QuizActivity 转场回去调用 检测，解析结果 Intent
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        ButterKnife.bind(this);

        //getIntent方法返回了 startActivity 转发的Intent对象,false默认值不用理会
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    }

    @OnClick(R.id.show_answer_button)
    public void onClick() {
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }
        else {
            mAnswerTextView.setText((R.string.false_button));
        }
        //用户点击Show Answer的时候，CheatActivity 调用 setResult方法，将结果代码和Intent打包
        setAnswerShownResult(true);

        int cx = mShowAnswer.getWidth() / 2;
        int cy = mShowAnswer.getHeight() / 2;
        float radius = mShowAnswer.getWidth();
        Animator anim = ViewAnimationUtils
                .createCircularReveal(mShowAnswer, cx, cy, radius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mShowAnswer.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }
}
