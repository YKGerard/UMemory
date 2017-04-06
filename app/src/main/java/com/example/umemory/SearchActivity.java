package com.example.umemory;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.umemory.model.Memory;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;  //创建一个CoordinatorLayout的实例
    private List<Memory> memoryList = new ArrayList<>();  //创建一个List<Memory>对象
    private MemoryAdapter adapter;  //创建MemoryAdapter的实例


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //设置滚动列表
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.s_recycler_view);  //获得RecyclerView的实例
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);  //指定RecyclerView的布局为网格布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoryAdapter(memoryList);  //将数据传入MemoryAdapter构造函数中
        recyclerView.setAdapter(adapter);  //完成适配器设置

        //使用ToolBar替换ActionBar
        Toolbar toolbar=(Toolbar)findViewById(R.id.stoolbar);  //获得ToolBar实例
        setSupportActionBar(toolbar);  //将ToolBar实例传入

    }
    //添加ToolBar菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar,menu);
        return true;
    }

    //设置ToolBar菜单中按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:  //导航按钮点击事件
                break;
            default:
                break;
        }
        return true;
    }
}
