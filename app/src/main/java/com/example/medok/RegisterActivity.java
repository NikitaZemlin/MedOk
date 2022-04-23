package com.example.medok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ETLoginReg, ETPasswordReg;
    Button BTNLoginReg;
    TextView TVYesAccount;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETLoginReg = findViewById(R.id.ETLoginReg);
        ETPasswordReg = findViewById(R.id.ETPasswordReg);
        BTNLoginReg = findViewById(R.id.BTNLoginReg);
        BTNLoginReg.setOnClickListener(this);
        TVYesAccount = findViewById(R.id.TVYesAccount);
        TVYesAccount.setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.BTNLoginReg:
                String login = ETLoginReg.getText().toString().trim();
                String password = ETPasswordReg.getText().toString().trim();
                fAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Пользователь создан!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
                break;

            case R.id.TVYesAccount:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }

    }
}