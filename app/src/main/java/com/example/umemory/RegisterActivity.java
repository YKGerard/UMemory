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
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.umemory.model.User;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout r_usernameWrapper,r_emailWrapper,r_passwordlWrapper;
    private Button register,readBtn;
    private CheckBox agreement;
    private static final String USERNAME_PATTERN = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+";  //用户名正则表达式
    private static final String EMAIL_PATTERN ="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";  //邮箱正则表达式
    private static final String PASSWORD_PATTERN = "^[0-9a-zA-Z]{6,16}$";  //密码正则表达式
    private Matcher matcher;
    private String username,email,password;
    private boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        r_usernameWrapper=(TextInputLayout)findViewById(R.id.r_usernameWrapper);
        r_emailWrapper=(TextInputLayout)findViewById(R.id.r_emailWrapper);
        r_passwordlWrapper=(TextInputLayout)findViewById(R.id.r_passwordWrapper);
        register=(Button)findViewById(R.id.register);
        readBtn=(Button)findViewById(R.id.readBtn);
        agreement=(CheckBox)findViewById(R.id.agreement);

        r_usernameWrapper.setHint("用户名");
        r_emailWrapper.setHint("邮箱");
        r_passwordlWrapper.setHint("密码");

        //第一：默认初始化
        //Bmob.initialize(this, "Your Application ID");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                .setApplicationId("3892e7b2105ce8a393aab6e33262ffc7")////设置appkey
                .setConnectTimeout(30)////请求超时时间（单位为秒）：默认15s
                .setUploadBlockSize(1024*1024)////文件分片上传时每片的大小（单位字节），默认512*1024
                .setFileExpiration(2500)////文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r_usernameWrapper.setErrorEnabled(false);
                r_emailWrapper.setErrorEnabled(false);
                r_passwordlWrapper.setErrorEnabled(false);
                hideKeyboard();
                username=r_usernameWrapper.getEditText().getText().toString().trim();
                email=r_emailWrapper.getEditText().getText().toString().trim();
                password=r_passwordlWrapper.getEditText().getText().toString().trim();

                if (!validateUsername(username)){
                    r_usernameWrapper.setError("用户名存在非法字符，请重新输入");
                }/*else if (!isUsernameExist(username)){
                    r_usernameWrapper.setError("用户名已存在，请重新输入");
                }*/else if (!validateEmail(email)){
                    r_emailWrapper.setError("邮箱存在非法字符或格式不正确，请重新输入");
                }/*else if (!isEmailExist(email)){
                    r_usernameWrapper.setError("邮箱已注册，请更换邮箱");
                }*/else if (!validatePassword(password)){
                    r_passwordlWrapper.setError("密码存在非法字符，请重新输入");
                }else if (!agreement.isChecked()){
                    Toast.makeText(RegisterActivity.this,"请阅读并同意用户协议",Toast.LENGTH_SHORT).show();
                }else {
                    doRegister();
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

    //用户名格式输入验证
    public boolean validateUsername(String username){
        matcher = Pattern.compile(USERNAME_PATTERN).matcher(username);
        return matcher.matches();
    }

    //用户名重复验证
    /*public boolean isUsernameExist(String username){
        isExist = false;
        final BmobQuery<User> bmobUsername = new BmobQuery<User>();
        bmobUsername.addWhereEqualTo("username",username);
        bmobUsername.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    isExist = true;
                    Toast.makeText(RegisterActivity.this,"isExist is really true",Toast.LENGTH_SHORT).show();
                }else{
                    isExist = false;
                    Toast.makeText(RegisterActivity.this,"isExist is really false",Toast.LENGTH_SHORT).show();
                }
            }
        });if (isExist){
            Toast.makeText(RegisterActivity.this,"isExist is true",Toast.LENGTH_SHORT).show();
        }else if(!isExist){
            Toast.makeText(RegisterActivity.this,"isExist is false",Toast.LENGTH_SHORT).show();
        }
        return isExist;
    }*/

    //邮箱格式输入验证
    public boolean validateEmail(String email) {
        matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        return matcher.matches();
    }

    //邮箱重复验证
    /*public boolean isEmailExist(String email){
        isExist = false;
        BmobQuery<User> bmobEmail = new BmobQuery<User>();
        bmobEmail.addWhereEqualTo("email",email);
        bmobEmail.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    isExist = true;
                }else{
                    isExist = false;
                }
            }
        });
        return isExist;
    }*/

    //密码格式输入验证
    public boolean validatePassword(String password) {
        matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);
        return matcher.matches();
    }

    public void doRegister(){
        User user=new User(email,username,password);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtra("username",username);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"注册失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
