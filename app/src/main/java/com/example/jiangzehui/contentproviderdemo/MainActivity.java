package com.example.jiangzehui.contentproviderdemo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiangzehui.contentproviderdemo.db.DBService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    ArrayList<JSONObject> list;
    MyAdapter adapter;
    Random random;
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        list = DBService.getInstence(this).search();
        ListView lv = (ListView) findViewById(R.id.lv);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");
        adapter = new MyAdapter();
        lv.setAdapter(adapter);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name", "jiangzehui" + random.nextInt(100));
                values.put("age", 20 + random.nextInt(10));
                if (DBService.getInstence(MainActivity.this).save(values)) {
                    list = DBService.getInstence(MainActivity.this).search();
                    adapter.notifyDataSetChanged();
                    tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");
                } else {
                    Toast.makeText(MainActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBService.getInstence(MainActivity.this).deleteAll();
                list = DBService.getInstence(MainActivity.this).search();
                adapter.notifyDataSetChanged();
                tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setTitle("删除").setItems(new String[]{"sqlite方式删除", "provider方式删除", "取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                try {
                                    if (DBService.getInstence(MainActivity.this).delete(list.get(position).getInt("id"))) {
                                        list = DBService.getInstence(MainActivity.this).search();
                                        adapter.notifyDataSetChanged();
                                        tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");
                                    } else {
                                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                try {
                                    int id = list.get(position).getInt("id");
                                    getContentResolver().delete(Uri.parse(MyContentProvider.URI_ITEM),"id=?",new String[]{id+""});
                                    list = DBService.getInstence(MainActivity.this).search();
                                    adapter.notifyDataSetChanged();
                                    tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }).show();
                return true;
            }
        });


        //contentProvider操作方式
        findViewById(R.id.btnAdd2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name", "jiangzehui" + random.nextInt(100));
                values.put("age", 20 + random.nextInt(10));
                getContentResolver().insert(Uri.parse(MyContentProvider.URI_ALL), values);

                Cursor cursor = getContentResolver().query(Uri.parse(MyContentProvider.URI_ALL), null, null, null, null);
                list = new ArrayList<>();
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
                adapter.notifyDataSetChanged();
                tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");


            }
        });
        findViewById(R.id.btnDel2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContentResolver().delete(Uri.parse(MyContentProvider.URI_ALL), null, null);
                Cursor cursor = getContentResolver().query(Uri.parse(MyContentProvider.URI_ALL), null, null, null, null);
                list = new ArrayList<>();
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
                adapter.notifyDataSetChanged();
                tvTotal.setText("记录条数：" + DBService.getInstence(MainActivity.this).getCount() + "");
            }
        });


    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(MainActivity.this);
            try {
                tv.setText("id=" + list.get(position).getString("id") + "  name=" + list.get(position).getString("name") + "  age=" + list.get(position).getString("age"));
                tv.setPadding(20, 20, 20, 20);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tv;
        }
    }

}
