package com.example.jiangzehui.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jiangzehui.contentproviderdemo.db.DBHelper;

/**
 * Created by jiangzehui on 17/11/14.
 */

public class MyContentProvider extends ContentProvider {

    private DBHelper helper;
    private UriMatcher uriMatcher;
    private final int ALL = 1;
    private final int ITEM = 2;
    private static String TABLE_NAME = "student";
    //authorities
    private static String AUTHORITIES = "com.example.jiangzehui.contentproviderdemo.MyContentProvider";
    public static String URI_ALL = "content://"+AUTHORITIES+"/"+TABLE_NAME;
    public static String URI_ITEM = "content://"+AUTHORITIES+"/"+TABLE_NAME+ "/#";


    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, TABLE_NAME, ALL);
        uriMatcher.addURI(AUTHORITIES, TABLE_NAME + "/#", ITEM);
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {


        switch (uriMatcher.match(uri)) {
            case ALL:
                return "vnd.android.cursor.dir/" + TABLE_NAME;
            case ITEM:
                return "vnd.android.cursor.item/" + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case ALL:
                return helper.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case ITEM:
                return helper.getReadableDatabase().query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }


    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        switch (uriMatcher.match(uri)) {
            case ALL:
            case ITEM:
                Uri insertUri = ContentUris.withAppendedId(uri, helper.getWritableDatabase().insert(TABLE_NAME, null, contentValues));
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;

            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case ALL:
                return helper.getWritableDatabase().delete(TABLE_NAME, null, null);
            case ITEM:
                return helper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (uriMatcher.match(uri)) {
            case ALL:
            case ITEM:
                return helper.getWritableDatabase().update(TABLE_NAME, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }
    }
}
