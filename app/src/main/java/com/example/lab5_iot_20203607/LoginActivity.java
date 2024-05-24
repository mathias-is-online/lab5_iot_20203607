package com.example.lab5_iot_20203607;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPUCPCode;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPUCPCode = findViewById(R.id.editTextPUCPCode);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String pucpCode = editTextPUCPCode.getText().toString().trim();
            if (!pucpCode.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, TaskListActivity.class);
                intent.putExtra("PUCP_CODE", pucpCode);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Por favor ingrese su código PUCP", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
