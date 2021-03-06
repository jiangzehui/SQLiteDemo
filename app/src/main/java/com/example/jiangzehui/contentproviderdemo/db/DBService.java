package com.example.jiangzehui.contentproviderdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jiangzehui on 17/11/15.
 */

public class DBService {

    private static DBService mInstence;
    private DBHelper helper;
    private String TABLE_NAME;

    /**
     * 查询
     *
     * @return
     */
    public ArrayList<JSONObject> search() {
        ArrayList<JSONObject> list = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            JSONObject object = new JSONObject();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            try {
                object.put("id", id);
                object.put("age", age);
                object.put("name", name);
                Log.d("object=", object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(object);

        }
        return list;
    }


    /**
     * 保存
     *
     * @param contentValues
     * @return
     */
    public boolean save(ContentValues contentValues) {
        long result = helper.getWritableDatabase().insert(TABLE_NAME, null, contentValues);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 更新
     *
     * @param id
     * @param contentValues
     * @return
     */
    public boolean update(Object id, ContentValues contentValues) {
        long result = helper.getWritableDatabase().update(TABLE_NAME, contentValues, "id=?", new String[]{id + ""});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 删除某条记录
     *
     * @param id
     * @return
     */
    public boolean delete(Object id) {
        long result = helper.getWritableDatabase().delete(TABLE_NAME, "id=?", new String[]{id + ""});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAll() {
        long result = helper.getWritableDatabase().delete(TABLE_NAME, null, null);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    //对数据库表数据的统计操作
    public long getCount() {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select count(*) from " + TABLE_NAME, null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }


    public static DBService getInstence(Context context) {
        if (mInstence == null) {
            synchronized (DBService.class) {
                if (mInstence == null) {
                    mInstence = new DBService(context);
                }
            }
        }
        return mInstence;
    }

    public DBService(Context context) {
        close();
        helper = new DBHelper(context);
        TABLE_NAME = helper.TABLE_NAME;
    }


    private synchronized void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }


}
