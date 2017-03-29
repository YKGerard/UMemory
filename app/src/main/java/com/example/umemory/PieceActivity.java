package com.example.umemory;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PieceActivity extends AppCompatActivity {
    private EditText pTitle;
    private EditText pContent;
    private String title;
    private String content;
    private String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        pTitle = (EditText)findViewById(R.id.pTitle);
        pContent = (EditText)findViewById(R.id.pContent);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=dateFormat.format(date);

        //悬浮按钮，用于存储记忆
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.pFab);  //获得FloatingActionButton的实例
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pTitle.getText().toString().trim();
                content=pContent.getText().toString().trim();
                if (title!=""){
                    Memory memory = new Memory(title,content,"new",time);
                    memory.save();
                    Intent intent=new Intent(PieceActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else if (title==null&&content!=null){
                    title="无标题";
                    Memory memory = new Memory(title,content,"new",time);
                    memory.save();
                    Intent intent=new Intent(PieceActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });  //设置一个悬浮按钮监听器
    }
}
