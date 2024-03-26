package com.example.food.View.Account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.example.food.Presenter.UserPreSenter;
import com.example.food.Presenter.UserView;
import com.example.food.R;
import com.example.food.View.Admin.SignInAdminActivity;
import com.example.food.View.HomeActivity;

import java.util.Objects;

public class SignInActivity  extends AppCompatActivity  implements UserView , View.OnClickListener {
    private Button btndangnhap;
    private EditText editemail,editpass;
    private UserPreSenter userPreSenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        InitWidget();
        progressDialog = new ProgressDialog(this);
        Init();
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
                hideSoftKeyboard();
            }
        });
    }

    private void Init() {
        userPreSenter = new UserPreSenter(this);
        btndangnhap.setOnClickListener(this);
        findViewById(R.id.txtdangky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));

            }
        });
        findViewById(R.id.txtadmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( SignInActivity.this, SignInAdminActivity.class));

            }
        });

    }

    private void InitWidget() {
        btndangnhap = findViewById(R.id.btndangnhap);
        editemail=findViewById(R.id.editEmail);
        editpass = findViewById(R.id.editmatkhau);
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
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void OnAuthEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
        Toast.makeText(this, "Làm ơn hãy vào gmail xác thực !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Sai tài khoản/mật khẩu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPassNotSame() {
        Toast.makeText(this, "Tài khoản mật khẩu không khớp", Toast.LENGTH_SHORT).show();
    }
/// ẤN đang nhap
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btndangnhap:
              //  hideSoftKeyboard();
                String email=editemail.getText().toString();
                String pass =editpass.getText().toString().trim();
                userPreSenter.HandleLoginUser(email,pass);
        }
    }
    private void onClickForgotPassword() {
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = editemail.getText().toString().trim();
        String valid_email = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailAddress.length() > 0) {
            if (emailAddress.matches(valid_email)) {
                new AlertDialog.Builder(this)
                        .setTitle("Quên mật khẩu")
                        .setMessage("Bạn có chắc muốn đặt lại mật khẩu?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.dismiss();
                                auth.sendPasswordResetEmail(emailAddress)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignInActivity.this, "Hãy kiểm tra email của bạn!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }else {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(SignInActivity.this, "Nhập Email của bạn", Toast.LENGTH_SHORT).show();
        }
    }
    // Ẩn bàn phím
    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}

