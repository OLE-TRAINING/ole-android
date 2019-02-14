package br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.AsyncTaskProgressBar.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;

public class FinishYourRegistrationActivity extends AppCompatActivity {

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

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void colorStatusBar() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorBackground));
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupListeners();
        colorStatusBar();
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
                if (isLoading) {
                    finishYourRegistrationViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(FinishYourRegistrationActivity.this, finishYourRegistrationViewHolder.progressBar).execute();
                } else {
                    finishYourRegistrationViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finishYourRegistrationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(FinishYourRegistrationActivity.this,finishYourRegistrationViewHolder.errorEditText);
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

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(PreLoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

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