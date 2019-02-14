package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

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


public class InformTokenAndNewPasswordActivity extends AppCompatActivity {

    private InformTokenAndNewPasswordViewHolder informTokenAndNewPasswordViewHolder;
    private InformTokenAndNewPasswordViewModel informTokenAndNewPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.informTokenAndNewPasswordViewHolder = new InformTokenAndNewPasswordViewHolder(view);
        setContentView(view);

        informTokenAndNewPasswordViewModel = ViewModelProviders.of(this).get(InformTokenAndNewPasswordViewModel.class);

        setupObservers();

        Bundle bundle = getIntent().getExtras();

        informTokenAndNewPasswordViewModel.setBundle(bundle);

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
        colorStatusBar();
        setupListeners();
    }

    private void setupObservers() {
        informTokenAndNewPasswordViewModel.getIsLoading().observe(this, progressBarObserver);
        informTokenAndNewPasswordViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        informTokenAndNewPasswordViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        informTokenAndNewPasswordViewModel.getConfirmPasswordContainsErrorStatus().observe(this, confirmPasswordContainsErrorObserver);
        informTokenAndNewPasswordViewModel.getEmailChanged().observe(this, emailChangedObserver);
        informTokenAndNewPasswordViewModel.getShowPasswordConfirmationInput().observe(this,showPasswordConfirmationInput);
        informTokenAndNewPasswordViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        informTokenAndNewPasswordViewModel.getIsErrorMessageToPasswordInput().observe(this,isErrorMessageToPasswordInputObserver);
        informTokenAndNewPasswordViewModel.getPasswordChanged().observe(this,passwordChangedObserver);
        informTokenAndNewPasswordViewModel.getIsErrorMessageToTokenInput().observe(this,isErrorMessageToTokenInputObserver);
    }

    private void setupListeners() {
        informTokenAndNewPasswordViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        informTokenAndNewPasswordViewHolder.errorEditTextToken.getEditText().addTextChangedListener(errorEditTextTextWatcherToken);
        informTokenAndNewPasswordViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherPassword);
        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherConfirmPassword);
        informTokenAndNewPasswordViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
        informTokenAndNewPasswordViewHolder.button.setOnClickListener(buttonOnClickListener);
    }

    private View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }

        }
    };

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            informTokenAndNewPasswordViewModel.tokenForwardingRequested();
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(InformTokenAndNewPasswordActivity.this,informTokenAndNewPasswordViewHolder.errorEditTextToken);
            String code = informTokenAndNewPasswordViewHolder.errorEditTextToken.getText().toString().trim();
            String password = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            String confirmPassword = informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim();
            informTokenAndNewPasswordViewModel.completedForm(code,password,confirmPassword);
        }
    };

    private Observer<String> isErrorMessageToTokenInputObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError(message);
            informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
        }
    };

    private Observer<String> passwordChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            Toast.makeText(InformTokenAndNewPasswordActivity.this, getString(R.string.success_message_change_pass), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, LoginActivity.class);
            String emailInput = informTokenAndNewPasswordViewHolder.textViewEmail.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageToPasswordInputObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("");
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(message);
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            Toast.makeText(InformTokenAndNewPasswordActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private Observer<Boolean> showPasswordConfirmationInput = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean show) {
            if(show){
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(false);
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.VISIBLE);
            } else {
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);
            }
        }
    };
    private Observer<String> emailChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String email) {
            informTokenAndNewPasswordViewHolder.textViewEmail.setText(email);
        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> confirmPasswordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(InformTokenAndNewPasswordActivity.this, informTokenAndNewPasswordViewHolder.progressBar).execute();
                } else {
                    informTokenAndNewPasswordViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private TextWatcher errorEditTextTextWatcherToken = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            informTokenAndNewPasswordViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            informTokenAndNewPasswordViewModel.showPasswordConfirmationInput(password);
            informTokenAndNewPasswordViewModel.passwordTextChanged();
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getEditText().setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherConfirmPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            informTokenAndNewPasswordViewModel.confirmPasswordTextChanged();
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
        Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        informTokenAndNewPasswordViewModel.removeObserver();
    }
}