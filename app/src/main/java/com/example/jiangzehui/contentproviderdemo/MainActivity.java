package com.example.jiangzehui.contentproviderdemo;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentValues values = new ContentValues();
        values.put("name","jiangzehui");
        values.put("age","25");
        if(DBService.getInstence(this).save(values)){
            Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
        }
        DBService.getInstence(this).search();
        if(DBService.getInstence(this).delete(4)){
            Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
        }




    }
}
