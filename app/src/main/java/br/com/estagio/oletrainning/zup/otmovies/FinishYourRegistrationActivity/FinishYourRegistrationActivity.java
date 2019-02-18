package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

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


public class FinishYourRegistrationActivity extends CommonActivity {

    private FinishYourRegistrationViewHolder finishYourRegistrationViewHolder;
    private FinishYourRegistrationViewModel finishYourRegistrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_finish_your_registration, null);
        this.finishYourRegistrationViewHolder = new FinishYourRegistrationViewHolder(view);
        setContentView(view);

        finishYourRegistrationViewModel = ViewModelProviders.of(this).get(FinishYourRegistrationViewModel.class);

        setupObservers();

        Bundle bundle = getIntent().getExtras();

        finishYourRegistrationViewModel.setBundle(bundle);

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
        setupListeners();
    }

    private void setupListeners() {
        finishYourRegistrationViewHolder.button.setOnClickListener(buttonOnClickListener);
        finishYourRegistrationViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        finishYourRegistrationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        finishYourRegistrationViewHolder.textViewReSend.setOnClickListener(textViewOnClickListener);
    }

    private void setupObservers() {
        finishYourRegistrationViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        finishYourRegistrationViewModel.getIsLoading().observe(this, progressBarObserver);
        finishYourRegistrationViewModel.getEmailChanged().observe(this, emailChangedObserver);
        finishYourRegistrationViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        finishYourRegistrationViewModel.getIsValidatedToken().observe(this,isValidatedTokenObserver);
        finishYourRegistrationViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
    }

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            finishYourRegistrationViewHolder.errorEditText.setMessageError(message);
            finishYourRegistrationViewHolder.errorEditText.setErrorVisibility(true);
        }
    };

    private Observer<String> isValidatedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(FinishYourRegistrationActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(FinishYourRegistrationActivity.this, LoginActivity.class);
            String emailInput = finishYourRegistrationViewHolder.textViewEmail.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(FinishYourRegistrationActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private Observer<String> emailChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String email) {
            finishYourRegistrationViewHolder.textViewEmail.setText(email);
        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                finishYourRegistrationViewHolder.errorEditText.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                loadingExecutor(
                        isLoading,
                        finishYourRegistrationViewHolder.progressBar,
                        getWindow(),
                        FinishYourRegistrationActivity.this);
            }
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(FinishYourRegistrationActivity.this,
                    finishYourRegistrationViewHolder.errorEditText);
            String code = finishYourRegistrationViewHolder.errorEditText.getEditText().getText().toString().trim();
            finishYourRegistrationViewModel.tokenEntered(code);
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishYourRegistrationViewModel.tokenForwardingRequested();
        }
    };

    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            finishYourRegistrationViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FinishYourRegistrationActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishYourRegistrationViewModel.removeObserver();
    }
}