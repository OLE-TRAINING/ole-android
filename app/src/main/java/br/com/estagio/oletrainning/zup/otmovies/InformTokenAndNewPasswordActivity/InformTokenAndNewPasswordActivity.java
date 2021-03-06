package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;


public class InformTokenAndNewPasswordActivity extends CommonActivity {

    private InformTokenAndNewPasswordViewHolder informTokenAndNewPasswordViewHolder;
    private InformTokenAndNewPasswordViewModel informTokenAndNewPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.informTokenAndNewPasswordViewHolder = new InformTokenAndNewPasswordViewHolder(view);
        setContentView(view);

        informTokenAndNewPasswordViewModel = ViewModelProviders.of(this).get(InformTokenAndNewPasswordViewModel.class);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(this, PreLoginActivity.class);
            startActivity(intent);
        }

        informTokenAndNewPasswordViewHolder.textViewEmail.setText(SingletonEmail.INSTANCE.getEmail());

        setupObservers();

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
        setupListeners();
    }

    private void setupObservers() {
        informTokenAndNewPasswordViewModel.getIsLoading().observe(this, progressBarObserver);
        informTokenAndNewPasswordViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        informTokenAndNewPasswordViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        informTokenAndNewPasswordViewModel.getConfirmPasswordContainsErrorStatus().observe(this, confirmPasswordContainsErrorObserver);
        informTokenAndNewPasswordViewModel.getShowPasswordConfirmationInput().observe(this,showPasswordConfirmationInput);
        informTokenAndNewPasswordViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        informTokenAndNewPasswordViewModel.getIsErrorMessageToPasswordInput().observe(this,isErrorMessageToPasswordInputObserver);
        informTokenAndNewPasswordViewModel.getPasswordChanged().observe(this,passwordChangedObserver);
        informTokenAndNewPasswordViewModel.getIsErrorMessageToTokenInput().observe(this,isErrorMessageToTokenInputObserver);
        informTokenAndNewPasswordViewModel.getIsMessageSuccessForToast().observe(this,isMessageSuccessForToastObserver);
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
            hideKeyboardFrom(InformTokenAndNewPasswordActivity.this,
                    informTokenAndNewPasswordViewHolder.errorEditTextToken);
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
            TastyToast.makeText(getApplicationContext(),getString(R.string.success_message_change_pass), TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,600);
            Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, LoginActivity.class);
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
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,600);
        }
    };

    private Observer<String> isMessageSuccessForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,700);
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
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    informTokenAndNewPasswordViewHolder.progressBar,
                    informTokenAndNewPasswordViewHolder.frameLayout,
                    informTokenAndNewPasswordViewHolder.button);
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