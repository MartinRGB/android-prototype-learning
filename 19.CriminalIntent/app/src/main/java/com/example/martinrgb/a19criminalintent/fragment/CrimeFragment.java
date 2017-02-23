package com.example.martinrgb.a19criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.martinrgb.a19criminalintent.CrimeActivity;
import com.example.martinrgb.a19criminalintent.R;
import com.example.martinrgb.a19criminalintent.model.Crime;
import com.example.martinrgb.a19criminalintent.model.CrimeLab;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class CrimeFragment extends Fragment {
    @BindView(R.id.crime_title)
    EditText mTitleField;
    @BindView(R.id.crime_date)
    Button mDateButton;
    @BindView(R.id.crime_solved)
    CheckBox mSolvedCheckBox;
    private Crime mCrime;

    //Argument传数据法
    //######### 实例化直接在本体完成
    private static final String ARG_CRIME_ID = "crime_id";
    public static CrimeFragment newInstance(UUID crimeId){
        //实例化同时接受Bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        //Arguments从Bundles里面拿数据
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //######### 托管Fragment的Activity要调用
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //让Fragment拿到Activity的EXTRA_CRIME_ID
        //UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        //拿取Arguments->拿取Bundle
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        //让对应的Crime获取正确的ID
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    private boolean initChecked;

    //Fragment 本身的 创建
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        ButterKnife.bind(this, v);
        mTitleFieldChangedListener();
        mSolvedCheckBoxListener();

        mTitleField.setText(mCrime.getTitle());
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        initChecked = mCrime.isSolved();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEEEE, MMM d, yyyy", Locale.CHINESE);
        mDateButton.setText(dateFormat.format(mCrime.getDate()));
        mDateButton.setEnabled(false);
        return v;
    }

    private void mSolvedCheckBoxListener(){
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                //如果点击过，返送一个信号

                if(initChecked != isChecked){

                    returnResult();
                }

                if(isChecked){
                    mSolvedCheckBox.setText(R.string.crime_solved_label);
                }else {
                    mSolvedCheckBox.setText(R.string.crime_solve_label);
                }
            }
        });
    }

    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }


    private void mTitleFieldChangedListener() {
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //用户输入的返回
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
