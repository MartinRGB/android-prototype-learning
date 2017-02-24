package com.example.martinrgb.a19criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.example.martinrgb.a19criminalintent.R;
import com.example.martinrgb.a19criminalintent.model.Crime;
import com.example.martinrgb.a19criminalintent.model.CrimeLab;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.crime_suspect)
    Button mSuspectButton;
    @BindView(R.id.crime_report)
    Button mReportButton;
    private Crime mCrime;

    //Argument传数据法
    //######### 实例化直接在本体完成
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;

    public static CrimeFragment newInstance(UUID crimeId) {
        //实例化同时接受Bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
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

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private boolean initChecked;

    //Fragment 本身的 创建
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        ButterKnife.bind(this, v);
        mTitleFieldChangedListener();
        mSolvedCheckBoxListener();
        mDateButtonListener();
        mReportBtnClickListener();

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(pickContact,REQUEST_CONTACT);

            }

        });

        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }


        //检查是否存在联系人应用
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }



        mTitleField.setText(mCrime.getTitle());
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        initChecked = mCrime.isSolved();

        updateDate();
        //mDateButton.setEnabled(false);
        return v;
    }


    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    private void mDateButtonListener() {
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //Fragment 直接的数据传输
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
    }

    //接受DatePickerFragment返送的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if(requestCode == REQUEST_CONTACT && data !=null){
            Uri contactUri = data.getData();

            //创建查询语句，返回全部联系人名字，
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            //查询联系人数据库，获得可用Cursor
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            //只包含一条记录，Cursor移动到第一条记录并使用其字符串
            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return; }
                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }
    }

    private void updateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEEEE, MMM d, yyyy", Locale.CHINESE);
        mDateButton.setText(dateFormat.format(mCrime.getDate()));
    }

    private void mSolvedCheckBoxListener() {
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                //如果点击过，返送一个信号

                if (initChecked != isChecked) {

                    returnResult();
                }

                if (isChecked) {
                    mSolvedCheckBox.setText(R.string.crime_solved_label);
                } else {
                    mSolvedCheckBox.setText(R.string.crime_solve_label);
                }
            }
        });
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

    private void mReportBtnClickListener(){
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐式Intent
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

}
