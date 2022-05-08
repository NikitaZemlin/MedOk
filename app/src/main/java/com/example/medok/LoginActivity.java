package com.example.medok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ETLogin, ETPassword;
    Button BTNLogin;
    TextView TVNoAccount;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETLogin = findViewById(R.id.ETLogin);
        ETPassword = findViewById(R.id.ETPassword);
        BTNLogin = findViewById(R.id.BTNLogin);
        BTNLogin.setOnClickListener(this);
        TVNoAccount = findViewById(R.id.TVNoAccount);
        TVNoAccount.setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BTNLogin:
                String login = ETLogin.getText().toString().trim();
                String password = ETPassword.getText().toString().trim();

                if (TextUtils.isEmpty(login)){
                    ETLogin.setError("Некорректный логин");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    ETPassword.setError("Некорретный пароль");
                }

                fAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Medical.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.TVNoAccount:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }

    }
}