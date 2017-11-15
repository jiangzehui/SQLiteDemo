package com.example.jiangzehui.contentproviderdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jiangzehui on 17/11/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    public final String TABLE_NAME = "student"; //表名
    private static final String DATABASE_NAME = "test.db";//库名

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + TABLE_NAME + " (id integer primary key autoincrement,name varchar(15),age integer) ";
        sqLiteDatabase.execSQL(sql);
    }


    //数据库版本或表结构改变会被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
