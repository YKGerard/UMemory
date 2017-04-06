package com.example.umemory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.umemory.model.Memory;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private EditText eTitle;
    private EditText eContent;
    private TextView eDate;
    private String title;
    private String content;
    private String time;
    private List<Memory> memorys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent=getIntent();
        int mid=intent.getIntExtra("m_id",1);

        eTitle = (EditText)findViewById(R.id.eTitle);
        eContent = (EditText)findViewById(R.id.eContent);
        eDate = (TextView)findViewById(R.id.eDate);

        eTitle.setEnabled(false);
        eContent.setEnabled(false);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=dateFormat.format(date);

        memorys= DataSupport.where("id = ?",""+mid).find(Memory.class);  //获得对应的记忆信息
        eTitle.setText(memorys.get(0).getTitle());
        eContent.setText(memorys.get(0).getContent());
        eDate.setText("时间："+memorys.get(0).getDate());

        //悬浮按钮，用于存储记忆
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.eFab);  //获得FloatingActionButton的实例
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=eTitle.getText().toString().trim();
                content=eContent.getText().toString().trim();
                if (title!=null){
                    Memory memory = new Memory();
                    memory.setTitle(title);
                    memory.setContent(content);
                    memory.setDate(time);
                    memory.updateAll("id = ?",""+memorys.get(0).getId());
                    Intent intent=new Intent(EditActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else if (title==null&&content!=null){
                    title="无标题";
                    Memory memory = new Memory();
                    memory.setTitle(title);
                    memory.setContent(content);
                    memory.setDate(time);
                    memory.update(memorys.get(0).getId());
                    Intent intent=new Intent(EditActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });  //设置一个悬浮按钮监听器

        //悬浮按钮，用于存储记忆
        FloatingActionButton fabEdit=(FloatingActionButton)findViewById(R.id.eFabEdit);  //获得FloatingActionButton的实例
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eTitle.setEnabled(true);
                eContent.setEnabled(true);
            }
        });  //设置一个悬浮按钮监听器
    }
}
