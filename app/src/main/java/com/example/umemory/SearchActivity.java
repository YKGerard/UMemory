package com.example.umemory;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umemory.model.Memory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Memory> searchList = new ArrayList<>();  //创建一个List<Memory>对象
    private MemoryAdapter adapter;  //创建MemoryAdapter的实例
    private SearchView searchView;
    private Button clearRecord;
    /*private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<String> recordList = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        clearRecord=(Button)findViewById(R.id.clearRecord);
        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);  //不自动缩小图标
        searchView.setSubmitButtonEnabled(true);  //显示搜索按钮
        searchView.setQueryHint("请输入您想要查找的内容");

        /*pref = getSharedPreferences("recordList",MODE_PRIVATE);
        editor = pref.edit();

        //将历史记录存入sharedPreference
        editor.putInt("itemNum",recordList.size());
        for (int i = 0;i<recordList.size();i++){
            editor.putString("item_"+i,recordList.get(i));
        }
        //将历史记录从sharedPreference取出
        int itemNum = pref.getInt("itemNum",0);
        for (int i = 0;i<itemNum;i++){
            recordList.add(pref.getString("item_"+i,""));
        }*/

        //设置滚动列表
        final RecyclerView recyclerView=(RecyclerView)findViewById(R.id.s_recycler_view);  //获得RecyclerView的实例
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);  //指定RecyclerView的布局为网格布局
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoryAdapter(searchList);  //将数据传入MemoryAdapter构造函数中
        recyclerView.setAdapter(adapter);  //完成适配器设置

        //清空历史记录
        clearRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.clear();
                if (!newText.equals("")){
                    searchList=DataSupport.where("title like ?","%"+newText+"%").find(Memory.class);
                }
                adapter = new MemoryAdapter(searchList);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
    }

}
