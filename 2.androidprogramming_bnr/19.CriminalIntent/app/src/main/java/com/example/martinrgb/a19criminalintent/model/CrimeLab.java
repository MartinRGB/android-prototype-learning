package com.example.martinrgb.a19criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.martinrgb.a19criminalintent.database.CrimeBaseHelper;
import com.example.martinrgb.a19criminalintent.database.CrimeCursorWrapper;
import com.example.martinrgb.a19criminalintent.database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    //Getter ;取CrimeLab
    public static CrimeLab get(Context context) {
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    //用ContentVlaues贮存数据
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }

    //Constructor - 新建Crime List列表
    //Singleton 开始
    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
        //mCrimes = new ArrayList<>();
        //含量为100的crimeList;
//        for(int i =0;i<100;i++){
//            Crime crime = new Crime();
//            crime.setTitle("丑态记录" + i);
//            crime.setSolved(i%2 == 0);//每隔一个设置
//            mCrimes.add(crime);
//        }
    }

    //查询返回的cursor封装到 CrimeCursorWrapper中
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(

        CrimeDbSchema.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy

        );
        return new CrimeCursorWrapper(cursor);
    }

    //取Crime List列表
    public List<Crime> getCrimes(){
//        return mCrimes;

        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        //Cursor像一根手指一样，一个接一个读取下一行记录
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return crimes;
    }

    //取出指定Id的Crime
    public Crime getCrime(UUID id){
        //遍历所有的crime，如果id匹配，那么返回该Crime
//        for(Crime crime:mCrimes){
//            if(crime.getID().equals(id)){
//                return crime;
//            }
//        }
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    //定位图片文件
    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        //返回指向某个具体位置的文件对象
        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getID().toString();
        //拿到更新Crime Model的所有的所需数据类型
        ContentValues values = getContentValues(crime);

        //表名，数据
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,
                CrimeDbSchema.CrimeTable.Cols.UUID+"=?", new String[]{uuidString});
    }

    public void addCrime(Crime c){
       // mCrimes.add(c);
        //拿到新建Crime Model的所有的所需数据类型
        ContentValues values = getContentValues(c);
        //数据库表名/null/写入的数据
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

}
