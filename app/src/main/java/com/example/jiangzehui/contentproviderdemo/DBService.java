package com.example.jiangzehui.contentproviderdemo;

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
    private Helper helper;
    private final String TABLE_NAME = "student";

    /**
     * 查询
     *
     * @return
     */
    protected ArrayList<JSONObject> search() {
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
    protected boolean save(ContentValues contentValues) {
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
    protected boolean update(Object id, ContentValues contentValues) {
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
    protected boolean delete(Object id) {
        long result = helper.getWritableDatabase().delete(TABLE_NAME, "id=?", new String[]{id + ""});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
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
        helper = new Helper(context);
    }


    private synchronized void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }



}
