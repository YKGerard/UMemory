package com.example.umemory;

import android.app.AlertDialog;
import android.app.Dialog;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout r_usernameWrapper,r_emailWrapper,r_passwordlWrapper;
    private Button register,readBtn;
    private static final String USERNAME_PATTERN = "^[\\\\u4e00-\\\\u9fa5_a-zA-Z0-9-]{1,16}$";  //用户名正则表达式，限16个字符，支持中英文、减号、数字、下划线
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final String PASSWORD_PATTERN = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]){6,20}$";  //密码正则表达式，6-20 位，字母、数字、字符
    private Matcher matcher;
    private String username,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        r_usernameWrapper=(TextInputLayout)findViewById(R.id.r_usernameWrapper);
        r_emailWrapper=(TextInputLayout)findViewById(R.id.r_emailWrapper);
        r_passwordlWrapper=(TextInputLayout)findViewById(R.id.r_passwordWrapper);
        register=(Button)findViewById(R.id.register);
        readBtn=(Button)findViewById(R.id.readBtn);

        r_usernameWrapper.setHint("用户名");
        r_emailWrapper.setHint("邮箱");
        r_passwordlWrapper.setHint("密码");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                username=r_usernameWrapper.getEditText().getText().toString();
                email=r_emailWrapper.getEditText().getText().toString();
                password=r_passwordlWrapper.getEditText().getText().toString();
                if (!validateUsername(username)){
                    r_usernameWrapper.setError("用户名输入错误");
                }else if (!validateEmail(email)){
                    r_emailWrapper.setError("邮箱输入错误");
                }else if (!validatePassword(password)){
                    r_passwordlWrapper.setError("密码输入错误");
                }else {
                    r_usernameWrapper.setErrorEnabled(false);
                    r_emailWrapper.setErrorEnabled(false);
                    r_passwordlWrapper.setErrorEnabled(false);
                    doRegister();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("用户协议");
                builder.setIcon(R.drawable.logo_small);
                builder.setMessage(R.string.User_Agreement);
                builder.create().show();
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

    //用户名输入验证
    public boolean validateUsername(String username){
        matcher = Pattern.compile(USERNAME_PATTERN).matcher(username);
        return matcher.matches();
    }

    //邮箱输入验证
    public boolean validateEmail(String email) {
        matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        return matcher.matches();
    }

    //密码输入验证
    public boolean validatePassword(String password) {
        matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);
        return matcher.matches();
    }

    public void doRegister(){
        User user=new User(username,email,password);
        user.save();
    }
}
