package com.example.martinrgb.a21nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by MartinRGB on 2017/2/24.
 */

public class NerdLauncherFragment extends Fragment {
    private static final String Tag = "NerdLauncherFragment";
    private RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstaceState){
        View v = inflater.inflate(R.layout.fragment_nerd_launcher,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }
    //从packagemanager中，拿到所有的Activity,并且排序
    private void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        //Launcher的Intent
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        //查询Activities的总数
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent,0);

        //标签排序
        Collections.sort(activities, new Comparator<ResolveInfo>(){
            public int compare(ResolveInfo a,ResolveInfo b){
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString()
                );
            }
        });
        mRecyclerView.setAdapter(new ActivityAdapter(activities));

        Log.i(Tag,"Found " + activities.size() + " activities.");
    }
    //Holder
    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;
        private ImageView mIconView;

        //ViewHolder内部的建立
        public ActivityHolder(LayoutInflater inflater,ViewGroup container){
            super(inflater.inflate(R.layout.list_item_activity,container,false));
            mNameTextView = (TextView) itemView.findViewById(R.id.holder_text);
            mIconView = (ImageView) itemView.findViewById(R.id.holder_icon);
            mNameTextView.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo resolveInfo){
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            Drawable appIcon = mResolveInfo.loadIcon(pm);
            mIconView.setImageDrawable(appIcon);
            mNameTextView.setText(appName);
        }

        @Override
        public void onClick(View v){
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            //包名+ 类名
            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,activityInfo.name);

                    //加为新建一个Task,不加则内部运行
                    //.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
            startActivity(i);
        }

    }
    //Adapter
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent,int viewType){
            //拿到上下文
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //拿到layout，创建ViewHolder容器
            //View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);

            return new ActivityHolder(layoutInflater,parent);
//            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder activityHolder,int position){
            ResolveInfo resolveInfo = mActivities.get(position);
            activityHolder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount(){
            return mActivities.size();
        }

    }

}
