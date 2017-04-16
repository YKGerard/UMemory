package com.example.umemory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umemory.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonActivity extends AppCompatActivity {
    private Button modify_person,modify_password,logout;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        modify_person=(Button)findViewById(R.id.modify_person);
        modify_password=(Button)findViewById(R.id.modify_password);
        logout=(Button)findViewById(R.id.logout);

        Intent intent = getIntent();
        userId=intent.getStringExtra("userId");

        modify_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_person = new Intent(PersonActivity.this,ModifyUserInfoActivity.class);
                intent_person.putExtra("userId",userId);
                startActivity(intent_person);
            }
        });

        modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent_pwd = new Intent(PersonActivity.this,ModifyPwdActivity.class);
                intent_pwd.putExtra("userId",userId);
                startActivity(intent_pwd);*/
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
