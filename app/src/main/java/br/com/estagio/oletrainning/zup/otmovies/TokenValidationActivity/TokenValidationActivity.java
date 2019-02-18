package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class TokenValidationActivity extends CommonActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;
    private TokenValidationViewModel tokenValidationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        tokenValidationViewModel = ViewModelProviders.of(this).get(TokenValidationViewModel.class);

        setupObservers();

        Bundle bundle = getIntent().getExtras();

        tokenValidationViewModel.setBundle(bundle);

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
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
        tokenValidationViewModel.getEmailChanged().observe(this, emailChangedObserver);
        tokenValidationViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        tokenValidationViewModel.getIsValidatedToken().observe(this,isValidatedTokenObserver);
        tokenValidationViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
    }

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            tokenValidationViewHolder.errorEditText.setMessageError(message);
            tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
        }
    };

    private Observer<String> isValidatedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(TokenValidationActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TokenValidationActivity.this, LoginActivity.class);
            String emailInput = tokenValidationViewHolder.textViewEmail.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(TokenValidationActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private Observer<String> emailChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String email) {
            tokenValidationViewHolder.textViewEmail.setText(email);
        }
    };

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
                loadingExecutor(
                        isLoading,
                        tokenValidationViewHolder.progressBar,
                        getWindow(),
                        TokenValidationActivity.this);
            }
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(TokenValidationActivity.this,
                    tokenValidationViewHolder.errorEditText);
            String code = tokenValidationViewHolder.errorEditText.getEditText().getText().toString().trim();
            tokenValidationViewModel.tokenEntered(code);
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tokenValidationViewModel.tokenForwardingRequested();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tokenValidationViewModel.removeObserver();
    }
}