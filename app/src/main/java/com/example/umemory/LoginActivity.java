package com.example.umemory;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.umemory.model.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout l_emailWrapper;
    private TextInputLayout l_passwordlWrapper;
    private Button login;
    private Button forgetPassword;
    private Button register;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l_emailWrapper=(TextInputLayout)findViewById(R.id.l_emailWrapper);
        l_passwordlWrapper=(TextInputLayout)findViewById(R.id.l_passwordWrapper);
        login=(Button)findViewById(R.id.login);
        forgetPassword=(Button)findViewById(R.id.forgetPassword);
        register=(Button)findViewById(R.id.register);

        l_emailWrapper.setHint("邮箱");
        l_passwordlWrapper.setHint("密码");

        //登录按钮事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String email=l_emailWrapper.getEditText().getText().toString();
                String password=l_passwordlWrapper.getEditText().getText().toString();
                if (!validateEmail(email)){
                    l_emailWrapper.setError("邮箱输入错误");
                }else if (!validatePassword(password)){
                    l_passwordlWrapper.setError("密码输入错误");
                }else{
                    l_emailWrapper.setErrorEnabled(false);
                    l_passwordlWrapper.setErrorEnabled(false);
                    doLogin();
                }
            }
        });

        //忘记密码按钮事件
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //新用户注册按钮事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //隐藏虚拟键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //邮箱输入验证
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        userList= DataSupport.where("email = ?",email).find(User.class);
        if ((userList!=null)&&(matcher.matches())){
            return true;
        }else{
            return false;
        }
    }

    //密码输入验证
    public boolean validatePassword(String password) {
        if (password==userList.get(0).getPassword()){
            return true;
        }else{
            return false;
        }
    }

    public void doLogin(){
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra("userId",userList.get(0).getId());
        startActivity(intent);
    }
}
