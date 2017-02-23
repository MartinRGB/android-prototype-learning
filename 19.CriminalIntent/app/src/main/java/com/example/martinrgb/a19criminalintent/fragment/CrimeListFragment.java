package com.example.martinrgb.a19criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinrgb.a19criminalintent.CrimeActivity;
import com.example.martinrgb.a19criminalintent.CrimePagerActivity;
import com.example.martinrgb.a19criminalintent.R;
import com.example.martinrgb.a19criminalintent.model.Crime;
import com.example.martinrgb.a19criminalintent.model.CrimeLab;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class CrimeListFragment extends Fragment {
    @BindView(R.id.crime_recycler_view)
    RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private static final int REQUEST_CRIME = 1;
    private static final String TAG ="CrimeListFragment";

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        ButterKnife.bind(this, view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //创建的时候，Boolean值从Instance取 (创建时)
        if (savedInstance != null) {
            mSubtitleVisible = savedInstance.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }


    //保存Instance的时候，顺手放进一个Boolean值
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //覆盖了菜单的方法，实现XML中自定义创建
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        //切换 Tittle Text;
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    //顶部项目的点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                //点击之后，新建一个crime Model
                Crime crime = new Crime();
                //加入 Model 管理池
                CrimeLab.get(getActivity()).addCrime(crime);
                //转场，获取 Model ID
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getID());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean mSubtitleVisible;

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //覆盖后的菜单方法，要告知FragmentManger，要使用
        setHasOptionsMenu(true);
    }

    //#### 刷新
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //#### 刷新
    private void updateUI() {
        //拿取模型库
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        //从模型库里面创建List
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null){
            //实例化Adapter
            mAdapter = new CrimeAdapter(crimes);
            //设置Adapter
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {
            if(worthToNotify){
                mAdapter.notifyItemChanged(clickedViewHolderPosition);
                Log.e(TAG,"Notified");
            }
        }

        updateSubtitle();

    }

    private int clickedViewHolderPosition;
    private boolean worthToNotify;

    //创建TextView风格的ViewHolder
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;
        private View mHolderView;

        //承担了findViewById的遍历搜寻
        public CrimeHolder(View itemView) {
            super(itemView);
            //ViewHolder引用了TextView,这就要求 itemView必须是个 TextView;
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_tittle_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);
            mHolderView = itemView;
            mSolvedCheckBoxListener();

        }

        //承担了数据绑定
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.CHINESE);
            mDateTextView.setText(dateFormat.format(mCrime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //######注意,必须设置了 Model 层 mCrime的 setSolved，当CrimeFragment接收到 CrimeFragment的时候
        //才能获取到当前Model对应的Check boolean状态
        private void mSolvedCheckBoxListener(){
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCrime.setSolved(isChecked);
                }
            });
        }

        //######承担了单击事件里面的转场
        @Override
        public void onClick(View v){
            //Toast.makeText(getActivity(),mCrime.getTitle() + " clicked",Toast.LENGTH_SHORT).show();
            //告知CrimeActivity它的Id
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getID());
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CRIME);

            clickedViewHolderPosition = this.getAdapterPosition();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            worthToNotify = true;
        }
        else {
            worthToNotify = false;
        }
        if (requestCode == REQUEST_CRIME) {
            //
        }
    }

    //创建具体ViewHolder的Adapter
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        //Constructor
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        //创建 ViewHolder
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //创建 View 视图
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            //封装到 ViewHolder 中
            return new CrimeHolder(view);
        }

        //绑定 ViewHolder 到 RecyclerView 的 ViewHolder
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            //拿去对应Cell的模型
            Crime crime = mCrimes.get(position);
            //把对应的模型绑定到ViewHolder上
            holder.bindCrime(crime);

            final int mPosition = position;

        }

        //获得 ItemCount
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }


}
