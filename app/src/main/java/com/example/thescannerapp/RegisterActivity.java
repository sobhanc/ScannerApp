package com.example.thescannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    //The variables are created
    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //instantiation and binding of views
        db = new DatabaseHelper(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
        mButtonRegister = findViewById(R.id.button_register);
        mTextViewLogin = findViewById(R.id.textview_login);
        //the onClickListener will launch the login activity if the user
        //wishes to go back to that activity
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
                finish();
            }
        });
        //Upon request the register button will validate
        //and if correct it will launch the login activity, this time with the user
        //info in the database
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                //user validation
                if(user.isEmpty()) {
                    mTextUsername.setError("Please enter a username!");
                } else if(pwd.isEmpty()) {
                    mTextPassword.setError("Please enter a password!");
                } else if(cnf_pwd.isEmpty()) {
                    mTextCnfPassword.setError("Please confirm password!");
                } else if (pwd.equals(cnf_pwd))  {
                    //addUser will add the user
                    long val = db.addUser(user,pwd);
                    Toast.makeText(RegisterActivity.this,"You have registered an account",Toast.LENGTH_SHORT).show();
                    Intent moveToLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(moveToLogin);
                    finish();
                } else {
                    //UI interaction with toast
                    Toast.makeText(RegisterActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}