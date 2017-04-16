package com.example.umemory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    private EditText f_email,f_content;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        f_email = (EditText)findViewById(R.id.f_email);
        f_content = (EditText)findViewById(R.id.f_content);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                startSendEmailIntent();
                Toast.makeText(FeedbackActivity.this, "感谢您的反馈,我们会尽快处理您的意见。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //发送邮件
    private void startSendEmailIntent(){
        /*Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:344088243@qq.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, f_email.getText());
        data.putExtra(Intent.EXTRA_TEXT, f_content.getText());
        startActivity(data);
        finish();*/
        Intent intent = new Intent("com.google.android.gm.action.AUTO_SEND");
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL,"344088243@qq.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, f_email.getText());
        intent.putExtra(Intent.EXTRA_TEXT, f_content.getText());
        startActivity(intent);
        finish();
    }

    //隐藏虚拟键盘
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
