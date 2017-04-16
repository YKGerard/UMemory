package com.example.umemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umemory.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPwdActivity extends AppCompatActivity {
    private EditText original_pwd,modify_pwd,modify_pwd_re;
    private Button modify_pwd_confirm;
    private String userId;
    private User person;
    private Matcher matcher;
    private static final String PASSWORD_PATTERN = "^[0-9a-zA-Z]{6,16}$";  //密码正则表达式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        original_pwd = (EditText)findViewById(R.id.original_pwd);
        modify_pwd = (EditText)findViewById(R.id.modify_pwd);
        modify_pwd_re = (EditText)findViewById(R.id.modify_pwd_re);
        modify_pwd_confirm = (Button)findViewById(R.id.modify_pwd_confirm);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        BmobQuery<User> user = new BmobQuery<>();
        user.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                person = user;
            }
        });

        original_pwd.setText(person.getPassword());

        modify_pwd_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePassword(modify_pwd.getText().toString().trim())) {
                    if (modify_pwd.getText().equals(modify_pwd_re.getText())) {
                        if (!original_pwd.getText().equals(modify_pwd.getText())) {
                            person.setPassword(modify_pwd.getText().toString().trim());
                            person.update(userId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(ModifyPwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ModifyPwdActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ModifyPwdActivity.this, "新密码不能与原密码相同，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ModifyPwdActivity.this, "两次输入的密码不相同，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ModifyPwdActivity.this, "密码存在非法字符，请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //密码格式输入验证
    public boolean validatePassword(String password) {
        matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);
        return matcher.matches();
    }
}
