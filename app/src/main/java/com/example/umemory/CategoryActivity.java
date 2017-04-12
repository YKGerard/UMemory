package com.example.umemory;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private List<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //使用ToolBar替换ActionBar
        Toolbar toolbar=(Toolbar)findViewById(R.id.c_toolbar);  //获得ToolBar实例
        setSupportActionBar(toolbar);  //将ToolBar实例传入

        final ListView cListView = (ListView) findViewById(R.id.c_list_view);

        cursor = DataSupport.findBySQL("select category from Memory group by category");
        if (cursor.moveToFirst()!=false)
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                categoryList.add(cursor.getString(cursor.getColumnIndex("category")));
            }
        categoryList.add("全部");

        categoryAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categoryList);
        cListView.setAdapter(categoryAdapter);
        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this,HomeActivity.class);
                intent.putExtra("category",categoryList.get(position).toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        ActionBar actionBar=getSupportActionBar();  //获得actionBar实例即ToolBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);  //显示左侧导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    //设置ToolBar菜单中按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:  //导航按钮点击事件
                finish();
                break;
        }
        return true;
    }
}
