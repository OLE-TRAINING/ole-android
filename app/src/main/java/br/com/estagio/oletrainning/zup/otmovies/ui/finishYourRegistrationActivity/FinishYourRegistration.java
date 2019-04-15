package br.com.estagio.oletrainning.zup.otmovies.ui.finishYourRegistrationActivity;

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

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.loginActivity.Login;
import br.com.estagio.oletrainning.zup.otmovies.ui.preLoginActivity.PreLogin;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;


public class FinishYourRegistration extends BaseActivity {

    private FinishYourRegistrationViewHolder finishYourRegistrationViewHolder;
    private FinishYourRegistrationViewModel finishYourRegistrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_finish_your_registration, null);
        this.finishYourRegistrationViewHolder = new FinishYourRegistrationViewHolder(view);
        setContentView(view);

        finishYourRegistrationViewModel = ViewModelProviders.of(this).get(FinishYourRegistrationViewModel.class);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(this, PreLogin.class);
            startActivity(intent);
        }

        finishYourRegistrationViewHolder.textViewEmail.setText(SingletonEmail.INSTANCE.getEmail());

        setupObservers();

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
        finishYourRegistrationViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        finishYourRegistrationViewModel.getIsValidatedToken().observe(this,isValidatedTokenObserver);
        finishYourRegistrationViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
        finishYourRegistrationViewModel.getIsMessageSuccessForToast().observe(this,isMessageSuccessForToastObserver);
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
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,500);
            Intent intent = new Intent(FinishYourRegistration.this, Login.class);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,500);
        }
    };

    private Observer<String> isMessageSuccessForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,500);
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
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    finishYourRegistrationViewHolder.progressBar,
                    finishYourRegistrationViewHolder.frameLayout,
                    finishYourRegistrationViewHolder.button);
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(FinishYourRegistration.this,
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
                Intent intent = new Intent(FinishYourRegistration.this, PreLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        Intent intent = new Intent(getApplicationContext(), PreLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishYourRegistrationViewModel.removeObserver();
    }
}