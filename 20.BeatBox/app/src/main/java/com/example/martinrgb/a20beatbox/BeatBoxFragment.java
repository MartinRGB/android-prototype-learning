package com.example.martinrgb.a20beatbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MartinRGB on 2017/2/24.
 */

public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;

    //onCreate
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //Rotate屏幕的时候，保留Fragment，但是清除Fragment的视图
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mBeatBox.release();
    }

    //Init Fragment
    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    //Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beat_box, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_beat_box_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        return view;
    }

    //ViewHolder
    private class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound, container, false));
            mButton = (Button)itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);
        }

        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(View v){
            mBeatBox.play(mSound);
        }


    }

    //Adapter
    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{

        private List<Sound> mSoundList;

        public SoundAdapter(List<Sound> sounds){
            mSoundList = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder soundHolder,int position){

            Sound sound = mSoundList.get(position);
            soundHolder.bindSound(sound);

        }

        @Override
        public int getItemCount(){
            return  mSoundList.size();
        }
    }

}
