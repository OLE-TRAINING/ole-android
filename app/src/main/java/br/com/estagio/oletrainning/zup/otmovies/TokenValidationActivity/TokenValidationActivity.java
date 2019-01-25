package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

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

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;

public class TokenValidationActivity extends AppCompatActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;

    private TokenValidationViewModel tokenValidationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        tokenValidationViewHolder.textViewEmail.setText(emailAdd);

        tokenValidationViewModel = ViewModelProviders.of(this).get(TokenValidationViewModel.class);

        setupObservers();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupListeners();
    }

    private void setupListeners() {
        tokenValidationViewHolder.button.setOnClickListener(buttonOnClickListener);
        tokenValidationViewHolder.imageView.setOnClickListener(imageViewBackArrowOnClickListener);
        tokenValidationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        tokenValidationViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
    }

    private void setupObservers() {
        tokenValidationViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        tokenValidationViewModel.getIsLoading().observe(this, progressBarObserver);
    }

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                tokenValidationViewHolder.errorEditText.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    tokenValidationViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(TokenValidationActivity.this, tokenValidationViewHolder.progressBar).execute();
                } else {
                    tokenValidationViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    tokenValidationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    Observer<ResponseModel> serviceCallTokenValidation = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            tokenValidationViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(TokenValidationActivity.this,"Código confirmado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TokenValidationActivity.this, LoginActivity.class);
                    String emailInput = tokenValidationViewHolder.textViewEmail.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
                    if(errorMessage.getKey().equals("error.unauthorized.token")){
                        tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    } else if (errorMessage.getKey().equals("error.invalid.token")) {
                        tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                        tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    } else{
                        Toast.makeText(TokenValidationActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(TokenValidationActivity.this, "Falha ao validar o código. Verifique a conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }
    };

    Observer<ResponseModel> serviceCallResendObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            tokenValidationViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(TokenValidationActivity.this,"Código reenviado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TokenValidationActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(TokenValidationActivity.this, "Falha ao reenviar o código. Verifique a conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = tokenValidationViewHolder.textViewEmail.getText().toString().trim();
            String code = tokenValidationViewHolder.errorEditText.getEditText().getText().toString().trim();
            tokenValidationViewModel.tokenEntered(code);
            if(tokenValidationViewModel.isValidToken(code)) {
                tokenValidationViewModel.serviceStarting();
                tokenValidationViewModel.tokenValidation(email,code)
                        .observe(TokenValidationActivity.this, serviceCallTokenValidation);
            }
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callTokenResend();
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tokenValidationViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }

        }
    };

    private void callTokenResend(){
        String email = tokenValidationViewHolder.textViewEmail.getText().toString().trim();
        tokenValidationViewModel.serviceStarting();
        tokenValidationViewModel.resendToken(email)
                .observe(TokenValidationActivity.this, serviceCallResendObserver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}
