package com.example.umemory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;//创建活动菜单对象
    private SwipeRefreshLayout swipeRefresh;//创建下拉刷新对象
    private List<Memory> memoryList = new ArrayList<>();  //创建一个List<Memory>对象
    private MemoryAdapter adapter;  //创建MemoryAdapter的实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LitePal.getDatabase();  //创建数据库
        if (DataSupport.findFirst(Memory.class)==null) {
            Memory memory = new Memory("这是你的第一个记忆", "让记忆在此绽放", "记忆体", "20121212");
            memory.save();
        }

        initMemory();

        //设置滚动列表
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);  //获得RecyclerView的实例
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);  //指定RecyclerView的布局为网格布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoryAdapter(memoryList);  //将数据传入MemoryAdapter构造函数中
        recyclerView.setAdapter(adapter);  //完成适配器设置

        //使用ToolBar替换ActionBar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);  //获得ToolBar实例
        setSupportActionBar(toolbar);  //将ToolBar实例传入

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);  //获得DrawerLayout实例
        ActionBar actionBar=getSupportActionBar();  //获得actionBar实例即ToolBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);  //显示左侧导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.logo_small);  //设置导航按钮图片
        }

        //悬浮按钮，用于打开一个新的记忆
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);  //获得FloatingActionButton的实例
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,PieceActivity.class);
                startActivity(intent);
            }
        });  //设置一个悬浮按钮监听器

        //滑动菜单内按钮
        NavigationView navView =(NavigationView)findViewById(R.id.nav_view);  //获得NavigationView实例
        navView.setCheckedItem(R.id.nav_person);  //将person菜单项设置为默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {  //设置一个菜单项选中事件的监听器
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {   //当用户点击了任意菜单项时，就会回调到该方法中
                switch(item.getItemId()){
                    case R.id.nav_person:
                        break;
                    case R.id.nav_setting:
                        break;
                    case R.id.nav_feedback:
                        break;
                    case R.id.nav_about:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);  //获得SwipeRefreshLayout的实例
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.textColorPrimary));  //设置下拉刷新进度条颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMemory();
            }
        });  //设置一个下拉刷新监听器
    }

    //下拉刷新数据
    private void refreshMemory(){
        new Thread(new Runnable() {
            @Override
            public void run() {  //开启一个线程
                try {
                    Thread.sleep(2000);  //线程沉睡 2 秒钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initMemory();  //重新生成数据
                        adapter.notifyDataSetChanged();  //通知数据发生了变化
                        swipeRefresh.setRefreshing(false);  //表示刷新事件结束，并隐藏刷新进度条
                    }
                });
            }
        }).start();
    }

    //加载数据库中的数据
    private void initMemory(){
        memoryList = DataSupport.order("id desc").find(Memory.class);//根据ID倒序排列查找并存储到memoryList中
    }

    //添加ToolBar菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //设置ToolBar菜单中按钮的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.category:
                break;
            case R.id.search:
                break;
            case android.R.id.home:  //导航按钮点击事件
                drawerLayout.openDrawer(GravityCompat.START);  //展示滑动菜单
                break;
            default:
                break;
        }
        return true;
    }
}
