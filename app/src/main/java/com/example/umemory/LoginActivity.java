package com.example.umemory;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.umemory.model.User;
import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout l_emailWrapper,l_passwordlWrapper;
    private Button login,forgetPassword,register;
    private BottomMenu menuWindow;
    private EditText l_email,l_password;
    private CheckBox remenberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l_emailWrapper=(TextInputLayout)findViewById(R.id.l_emailWrapper);
        l_passwordlWrapper=(TextInputLayout)findViewById(R.id.l_passwordWrapper);
        l_email=(EditText)findViewById(R.id.l_email);
        l_password=(EditText)findViewById(R.id.l_password);
        login=(Button)findViewById(R.id.login);
        forgetPassword=(Button)findViewById(R.id.forgetPassword);
        register=(Button)findViewById(R.id.l_newUser);
        remenberPass=(CheckBox)findViewById(R.id.remenber_pass);
        pref= getSharedPreferences("user",MODE_PRIVATE);
        boolean isRemenber = pref.getBoolean("remenber_password",false);

        if (isRemenber){
            //将邮箱和密码都设置到文本框中
            String email = pref.getString("email","");
            String password = pref.getString("password","");
            l_email.setText(email);
            l_password.setText(password);
            remenberPass.setChecked(true);
        }

        l_emailWrapper.setHint("邮箱");
        l_passwordlWrapper.setHint("密码");

        //登录按钮事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String email=l_email.getText().toString().trim();
                String password=l_password.getText().toString().trim();
                if (!validateEmail(email)){
                    l_emailWrapper.setError("邮箱输入错误");
                }else if (!validatePassword(password)){
                    l_passwordlWrapper.setError("密码输入错误");
                }else{
                    l_emailWrapper.setErrorEnabled(false);
                    l_passwordlWrapper.setErrorEnabled(false);
                    doLogin(email,password);
                }
            }
        });

        //忘记密码按钮事件
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new BottomMenu(LoginActivity.this, clickListener);
                menuWindow.show();
            }
        });

        //新用户注册按钮事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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
        userList= DataSupport.where("email = ?",email).find(User.class);
        String userEmail=userList.get(0).getEmail().toString().trim();
        if (!userEmail.equals("")){
            if (email.equals(userEmail)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    //密码输入验证
    public boolean validatePassword(String password) {
        String userPassword=userList.get(0).getPassword().toString().trim();
        if (!userPassword.equals("")){
            if (password.equals(userPassword)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    //执行登录操作
    public void doLogin(String email,String password){
        editor = pref.edit();
        if (remenberPass.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("email",email);
            editor.putString("password",password);
        }else{
            editor.clear();
        }
        editor.apply();
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra("userId",userList.get(0).getId());
        startActivity(intent);
        finish();
    }

    //定义点击事件内部类
    private View.OnClickListener clickListener = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.findByEmail:
                    break;
                case R.id.findByContact:
                    try {
                        CopyToClipboard(LoginActivity.this,"客服QQ已复制到剪贴板");
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this,"请检查是否安装QQ",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void CopyToClipboard(Context context,String text){
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
     }
}
