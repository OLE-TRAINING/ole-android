package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;

public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private PreLoginViewModel preLoginViewModel;
    private Boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        preLoginViewModel = ViewModelProviders.of(this).get(PreLoginViewModel.class);

        setupObservers();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupListeners();
    }

    private void setupObservers() {
        preLoginViewModel.getEmailContainsErrorStatus().observe(this, errorStatusObserver);
        preLoginViewModel.getIsLoading().observe(this, progressBarObserver);
    }

    private void setupListeners() {
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextEmailTextChangedListener);
    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clicked = true;
            String email = preLoginViewHolder.errorEditTextEmail.getEditText().getText().toString().trim();
            preLoginViewModel.emailEntered(email);

        }
    };

    private Observer<Boolean> errorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                if(!containsErrorStatus){
                    if(clicked){
                        preLoginViewModel.serviceStarting();
                        preLoginViewModel.getUserResponse(
                                preLoginViewHolder.errorEditTextEmail.getText().toString().trim())
                                .observe(PreLoginActivity.this,serviceCallObserver);
                    }
                    } else {
                    preLoginViewModel.serviceEnding();
                    preLoginViewHolder.errorEditTextEmail.setErrorVisibility(true);
                }
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    preLoginViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(PreLoginActivity.this, preLoginViewHolder.progressBar).execute();
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    Observer<UserResponse> serviceCallObserver = new Observer<UserResponse>() {
        @Override
        public void onChanged(@Nullable UserResponse userResponse) {
            preLoginViewModel.serviceEnding();
            if (userResponse != null) {
                if (userResponse.getRegistrationStatus().equals("REGISTERED")) {
                    Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                    String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else if (userResponse.getRegistrationStatus().equals("PENDING")) {
                    Intent intent = new Intent(PreLoginActivity.this, TokenValidationActivity.class);
                    String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else if (userResponse.getRegistrationStatus().equals("INEXISTENT")) {
                    Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                    String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else if(userResponse.getKey().equals("error.invalid.email")){
                    preLoginViewHolder.errorEditTextEmail.setErrorVisibility(true);
                } else {
                    Toast.makeText(PreLoginActivity.this, userResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(PreLoginActivity.this,"Falha ao validar seu email. Verifique a conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }

    };


    private TextWatcher editTextEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            preLoginViewModel.textChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

