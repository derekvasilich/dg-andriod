package com.example.dg_andriod.ui.login;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dg_andriod.NavActivity;
import com.example.dg_andriod.R;
import com.example.dg_andriod.data.model.User;
import com.example.dg_andriod.data.remote.LoginRequest;
import com.example.dg_andriod.data.remote.ApiUtils;
import com.example.dg_andriod.data.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dg_andriod.data.model.User.*;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private UserService userService;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        userService = ApiUtils.getUserService(getApplicationContext());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final ToggleButton rememberMe = findViewById(R.id.remember_me);
        final Button loginButton = findViewById(R.id.login);

        loadingProgressBar = findViewById(R.id.loading);

        String type = getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(JWT_TYPE_PREF_KEY, null);
        String token = getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(JWT_TOKEN_PREF_KEY, null);

        if (type != null && token != null) {
            Intent intent = new Intent(LoginActivity.this, NavActivity.class);
            startActivity(intent);
        }

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            Boolean remember = rememberMe.isChecked();
            doLogin(username, password, remember);
        });
    }

    private void doLogin(final String username, final String password, final boolean remember){
        Call call = userService.login(new LoginRequest(username, password));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    User user = (User) response.body();
                    if(!user.token.isEmpty() && !user.type.isEmpty()){
                        // login start main activity
                        // store token in PREFS
                        SharedPreferences prefs = getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE);
                        prefs.edit().putString(JWT_TOKEN_PREF_KEY, user.token).apply();
                        prefs.edit().putString(JWT_TYPE_PREF_KEY, user.type).apply();
                        prefs.edit().putBoolean(REMEMBER_PREF_KEY, remember).apply();

                        Intent intent = new Intent(LoginActivity.this, NavActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}