package com.example.martinrgb.a49gles_version;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by MartinRGB on 2017/4/17.
 */

public class MyListActivity extends android.app.ListActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.options)));
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 == 0) {
            startActivity(new Intent(MyListActivity.this, MainActivity.class));
        }
        else if (arg2 == 1) {
            Dialog d = new Dialog(this);
            d.setContentView(R.layout.highscore);
            d.setTitle("High Score");
            d.show();
        }
        else if (arg2 == 2) {
            Dialog d = new Dialog(this);
            d.setContentView(R.layout.editplayer);
            d.setTitle("Edit Player");
            d.show();
        }
    }

}
