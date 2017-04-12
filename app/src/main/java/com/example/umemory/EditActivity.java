package com.example.umemory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.umemory.model.Memory;

import org.litepal.crud.DataSupport;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private EditText eTitle,eContent;
    private TextView eDate;
    private String title,content,category,time;
    private List<Memory> memorys;
    private Spinner eSpinner;
    private List<String> spinnerList=new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;
    private EditText newCategory;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent=getIntent();
        int mid=intent.getIntExtra("m_id",1);

        eTitle = (EditText)findViewById(R.id.eTitle);
        eContent = (EditText)findViewById(R.id.eContent);
        eDate = (TextView)findViewById(R.id.eDate);
        eSpinner=(Spinner)findViewById(R.id.eSpinner);

        //获得当前时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=dateFormat.format(date);

        //获得类别
        c = DataSupport.findBySQL("select category from Memory group by category");
        if (c.moveToFirst()!=false)
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                spinnerList.add(c.getString(c.getColumnIndex("category")));
            }
        spinnerList.add("新建类别");
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerList);
        eSpinner.setAdapter(spinnerAdapter);


        //切换类别
        eSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //某类别被选中时设置为默认类别，若新建则调用新建类别方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerAdapter.getItem(position).equals("新建类别")){
                    showInputDialog();
                }else{
                    category=spinnerAdapter.getItem(position);
                }
            }

            //未选中类别时设置为未分类
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    category="未分类";
            }
        });

        memorys= DataSupport.where("id = ?",""+mid).find(Memory.class);  //获得对应的记忆信息
        eTitle.setText(memorys.get(0).getTitle());
        eContent.setText(memorys.get(0).getContent());
        eDate.setText("时间："+memorys.get(0).getDate());
        for (int i=0;i<spinnerAdapter.getCount();i++){
            if (memorys.get(0).getCategory().toString().equals(spinnerAdapter.getItem(i).toString()))
                eSpinner.setSelection(i);
        }

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
                    memory.setCategory(category);
                    memory.setDate(time);
                    memory.updateAll("id = ?",""+memorys.get(0).getId());
                    Intent intent=new Intent(EditActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else if (title==null&&content!=null){
                    title="无标题";
                    Memory memory = new Memory();
                    memory.setTitle(title);
                    memory.setContent(content);
                    memory.setDate(time);
                    memory.update(memorys.get(0).getId());
                    Intent intent=new Intent(EditActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });  //设置一个悬浮按钮监听器
    }

    //新建类别，弹出一个dialog提供输入
    private void showInputDialog() {
        newCategory = new EditText(EditActivity.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(EditActivity.this);
        inputDialog.setTitle("请输入你要新建的类别名").setView(newCategory);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spinnerList.clear();
                        spinnerList.add(newCategory.getText().toString());
                        if (c.moveToFirst()!=false)
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                spinnerList.add(c.getString(c.getColumnIndex("category")));
                            }
                        spinnerList.add("新建类别");
                        spinnerAdapter.notifyDataSetChanged();
                        eSpinner.setSelection(0);
                    }
                }).show();
    }

}
