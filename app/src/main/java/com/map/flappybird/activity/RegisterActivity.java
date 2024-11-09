package com.map.flappybird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.map.flappybird.R;
import com.map.flappybird.controller.RegisterController;


public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        retypePasswordEditText = findViewById(R.id.retypePasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        TextView loginLinkTextView = findViewById(R.id.loginLinkTextView);

        registerButton.setOnClickListener(v -> onRegisterButtonClick());

        loginLinkTextView.setOnClickListener(v -> onLoginLinkClick());
    }

    private void onRegisterButtonClick() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String retypePassword = retypePasswordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(retypePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterController registerController = new RegisterController();
        registerController.registerUser(username, password, this);
    }

    private void onLoginLinkClick() {
        // Redirect to LoginActivity if the user already has an account
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
