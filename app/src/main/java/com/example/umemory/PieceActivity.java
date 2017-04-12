package com.example.umemory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.umemory.model.Memory;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PieceActivity extends AppCompatActivity {
    private EditText pTitle,pContent;
    private String title,content,category,time;
    private Spinner pSpinner;
    private List<String> spinnerList=new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;
    private EditText newCategory;
    private Cursor c;
    private TextView pDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);

        pTitle = (EditText)findViewById(R.id.pTitle);
        pContent = (EditText)findViewById(R.id.pContent);
        pSpinner=(Spinner)findViewById(R.id.pSpinner);
        pDate=(TextView)findViewById(R.id.pDate);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=dateFormat.format(date);

        pDate.setText("时间："+time);

        //获得类别
        c = DataSupport.findBySQL("select category from Memory group by category");
        if (c.moveToFirst()!=false)
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                spinnerList.add(c.getString(c.getColumnIndex("category")));
            }
        spinnerList.add("新建类别");
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerList);
        pSpinner.setAdapter(spinnerAdapter);

        //切换类别
        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //悬浮按钮，用于存储记忆
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.pFab);  //获得FloatingActionButton的实例
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pTitle.getText().toString().trim();
                content=pContent.getText().toString().trim();
                if (title!=null){
                    Memory memory = new Memory(title,content,category,time);
                    memory.save();
                    Intent intent=new Intent(PieceActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else if (title==null&&content!=null){
                    title="无标题";
                    Memory memory = new Memory(title,content,category,time);
                    memory.save();
                    Intent intent=new Intent(PieceActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });  //设置一个悬浮按钮监听器
    }

    //新建类别，弹出一个dialog提供输入
    private void showInputDialog() {
        newCategory = new EditText(PieceActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(PieceActivity.this);
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
                        pSpinner.setSelection(0);
                    }
                }).show();
    }
}
