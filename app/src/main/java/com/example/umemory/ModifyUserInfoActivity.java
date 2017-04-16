package com.example.umemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umemory.model.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyUserInfoActivity extends AppCompatActivity {
    private EditText modify_username,modify_email;
    private Button modify_confirm;
    private String userId;
    private User person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        modify_username = (EditText)findViewById(R.id.modify_username);
        modify_email = (EditText)findViewById(R.id.modify_email);
        modify_confirm = (Button)findViewById(R.id.modify_confirm);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        BmobQuery<User> user = new BmobQuery<>();
        user.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                person = user;
            }
        });

        modify_username.setText(person.getUsername());
        modify_email.setText(person.getEmail());

        modify_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                person.setUsername(modify_username.getText().toString().trim());
                person.setEmail(modify_email.getText().toString().trim());
                person.update(userId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ModifyUserInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ModifyUserInfoActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
