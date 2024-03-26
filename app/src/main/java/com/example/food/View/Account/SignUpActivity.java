package com.example.food.View.Account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.example.food.Presenter.UserPreSenter;
import com.example.food.Presenter.UserView;
import com.example.food.R;
import com.example.food.View.HomeActivity;

public class SignUpActivity extends AppCompatActivity  implements UserView , View.OnClickListener {
    private Button btndangky;
    private EditText editemail,editpass,editpass_repeat;
    private UserPreSenter userPreSenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        InitWidget();
        Init();
    }

    private void Init() {
        userPreSenter = new UserPreSenter(this);
        btndangky.setOnClickListener(this);

    }

    private void InitWidget() {
        btndangky = findViewById(R.id.btndangky);
        editemail=findViewById(R.id.editEmail);
        editpass = findViewById(R.id.editmatkhau);
        editpass_repeat = findViewById(R.id.editmatkhau_repeat);
        findViewById(R.id.text_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });
    }

    @Override
    public void OnLengthEmail() {
        Toast.makeText(this, "Email không để trống", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnValidEmail() {
        Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPass() {
        Toast.makeText(this, "Mật khẩu không để trống", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnSucess() {
        startActivity(new Intent(this, HomeActivity.class));
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void OnAuthEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

        Toast.makeText(this, "Hãy vào gamil để xác thực tài khoản của bạn !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Tài khoản đã được đăng ksý !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPassNotSame() {
        Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btndangky) {
            hideSoftKeyboard();
            String email = editemail.getText().toString();
            String pass = editpass.getText().toString().trim();
            String repass = editpass_repeat.getText().toString().trim();
            userPreSenter.HandleRegist(email, pass, repass);
        }
    }
    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}

