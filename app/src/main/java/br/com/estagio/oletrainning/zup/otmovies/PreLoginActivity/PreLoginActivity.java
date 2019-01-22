package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;

public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private boolean emailContainsError;
    private final String EMAIL_VALIDATION_STATUS = "emailContainsError";
    private PreLoginViewModel preLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        setupListeners();


        preLoginViewModel = ViewModelProviders.of(this).get(PreLoginViewModel.class);
        preLoginViewModel.getEmailErrorStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean errorStatus) {
                preLoginViewHolder.errorEditTextEmail.setErrorVisibility(!errorStatus);
                emailContainsError = errorStatus;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EMAIL_VALIDATION_STATUS, emailContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
    }

   private void setupListeners() {
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextEmailTextChangedListener);
    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            preLoginViewModel.emailEntered(preLoginViewHolder.errorEditTextEmail.getText().toString().trim());
        }
    };


    private TextWatcher editTextEmailTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            preLoginViewModel.textChanged();
            emailContainsError = true;

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}

