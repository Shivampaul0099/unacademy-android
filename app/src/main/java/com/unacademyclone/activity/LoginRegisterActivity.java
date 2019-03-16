package com.unacademyclone.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.unacademyclone.*;
import com.unacademyclone.utility.TypefaceUtility;

public class LoginRegisterActivity extends AppCompatActivity {

    TextView tv_title, tv_sub_title, tv_google, tv_facebook, tv_or;
    TextInputLayout til_name, til_email, til_password;
    EditText et_name, et_email, et_password;
    TextView tv_submit, tv_disclaimer, tv_hint, tv_login_signup;

    TypefaceUtility tfUtil;
    String currentMode="LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        tfUtil=new TypefaceUtility(this);

        tv_title=findViewById(R.id.tv_title);
        tv_sub_title=findViewById(R.id.tv_sub_title);
        tv_google=findViewById(R.id.tv_google);
        tv_facebook=findViewById(R.id.tv_facebook);
        tv_or=findViewById(R.id.tv_or);
        til_name=findViewById(R.id.til_name);
        til_email=findViewById(R.id.til_email);
        til_password=findViewById(R.id.til_password);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        tv_submit=findViewById(R.id.tv_submit);
        tv_disclaimer=findViewById(R.id.tv_disclaimer);
        tv_hint=findViewById(R.id.tv_hint);
        tv_login_signup=findViewById(R.id.tv_login_signup);

        tv_title.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_sub_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_google.setTypeface(tfUtil.getTypefaceRegular());
        tv_facebook.setTypeface(tfUtil.getTypefaceRegular());
        tv_or.setTypeface(tfUtil.getTypefaceRegular());
        et_name.setTypeface(tfUtil.getTypefaceRegular());
        et_email.setTypeface(tfUtil.getTypefaceRegular());
        et_password.setTypeface(tfUtil.getTypefaceRegular());
        tv_submit.setTypeface(tfUtil.getTypefaceRegular());
        tv_disclaimer.setTypeface(tfUtil.getTypefaceRegular());
        tv_hint.setTypeface(tfUtil.getTypefaceRegular());
        tv_login_signup.setTypeface(tfUtil.getTypefaceRegular());

        tv_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMode.equals("LOGIN")){
                    currentMode="REGISTER";
                    showRegisterForm();
                }
                else{
                    currentMode="LOGIN";
                    showLoginForm();
                }
            }
        });
        et_email.clearFocus();
        et_name.clearFocus();
        et_password.clearFocus();
        tv_title.requestFocus();
    }

    public void showLoginForm(){
        tv_title.setText("Welcome to unacademy");
        tv_sub_title.setText("Enroll in courses and watch lessons from India's best educators.");
        til_name.setVisibility(View.GONE);
        tv_submit.setText("Log in");
        tv_hint.setText("Don't have an account?");
        tv_login_signup.setText("Sign up");
    }

    public void showRegisterForm(){
        tv_title.setText("Welcome back!");
        tv_sub_title.setText("Enroll in courses and watch lessons from India's best educators.");
        til_name.setVisibility(View.VISIBLE);
        tv_submit.setText("Create account");
        tv_hint.setText("Already have an account?");
        tv_login_signup.setText("Log in");
    }
}
