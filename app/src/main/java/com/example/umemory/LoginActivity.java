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

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout l_emailWrapper, l_passwordlWrapper;
    private Button login, forgetPassword, register;
    private BottomMenu menuWindow;
    private EditText l_email, l_password;
    private CheckBox remenberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l_emailWrapper = (TextInputLayout) findViewById(R.id.l_emailWrapper);
        l_passwordlWrapper = (TextInputLayout) findViewById(R.id.l_passwordWrapper);
        l_email = (EditText) findViewById(R.id.l_email);
        l_password = (EditText) findViewById(R.id.l_password);
        login = (Button) findViewById(R.id.login);
        forgetPassword = (Button) findViewById(R.id.forgetPassword);
        register = (Button) findViewById(R.id.l_newUser);
        remenberPass = (CheckBox) findViewById(R.id.remenber_pass);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemenber = pref.getBoolean("remember_password", false);

        //第一：默认初始化
        //Bmob.initialize(this, "Your Application ID");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("3892e7b2105ce8a393aab6e33262ffc7")////设置appkey
                .setConnectTimeout(30)////请求超时时间（单位为秒）：默认15s
                .setUploadBlockSize(1024 * 1024)////文件分片上传时每片的大小（单位字节），默认512*1024
                .setFileExpiration(2500)////文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);

        if (isRemenber) {
            //将邮箱和密码都设置到文本框中
            String email = pref.getString("username", "");
            String password = pref.getString("password", "");
            l_email.setText(email);
            l_password.setText(password);
            remenberPass.setChecked(true);
        }

        l_emailWrapper.setHint("用户名或邮箱");
        l_passwordlWrapper.setHint("密码");

        //登录按钮事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                final String email = l_email.getText().toString().trim();
                final String password = l_password.getText().toString().trim();
                //查询邮箱是否存在
                BmobQuery<User> bmobUsername = new BmobQuery<>();
                bmobUsername.addWhereEqualTo("email", email);
                bmobUsername.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            //查询邮箱成功,判断密码是否正确
                            if (list.get(0).getPassword().equals(password)){
                                        doLogin(list);//密码正确
                                    } else {
                                        Toast.makeText(LoginActivity.this, "密码错误，请重新填写", Toast.LENGTH_SHORT).show();
                                    }
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或邮箱错误，请重新填写", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 2);
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

    //执行登录操作
    public void doLogin(List<User> user) {
        editor = pref.edit();
        if (remenberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("username", user.get(0).getUsername());
            editor.putString("password", user.get(0).getPassword());
        } else {
            editor.clear();
        }
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("userId", user.get(0).getObjectId());
        startActivity(intent);
        finish();
    }

    //定义点击事件内部类
    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.findByEmail:
                    break;
                case R.id.findByContact:
                    try {
                        CopyToClipboard(LoginActivity.this, "客服QQ号码已复制到剪贴板");
                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "请检查是否安装QQ", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void CopyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    String username = data.getStringExtra("username");
                    l_email.setText(username);
                }
        }
    }
}
