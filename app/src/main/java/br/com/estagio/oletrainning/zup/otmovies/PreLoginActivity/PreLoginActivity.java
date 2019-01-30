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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import br.com.estagio.oletrainning.zup.otmovies.FinishYourRegistrationActivity.FinishYourRegistrationActivity;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.CustomComponents.AsyncTaskProgressBar.SyncProgressBar;

public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private PreLoginViewModel preLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);

        preLoginViewModel = ViewModelProviders.of(this).get(PreLoginViewModel.class);

        setupObservers();
    }

    private void colorStatusBar(){
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
            String email = preLoginViewHolder.errorEditTextEmail.getEditText().getText().toString().trim();
            preLoginViewModel.emailEntered(email);
            if (preLoginViewModel.isValidEmail(email)) {
                preLoginViewModel.serviceStarting();
                preLoginViewModel.getUserResponse(
                        preLoginViewHolder.errorEditTextEmail.getText().toString().trim())
                        .observe(PreLoginActivity.this, serviceCallObserver);
            }
        }
    };

    private Observer<Boolean> errorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                preLoginViewHolder.errorEditTextEmail.setErrorVisibility(containsErrorStatus);
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
                    preLoginViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    Observer<ResponseModel> serviceCallObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            preLoginViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getRegistrationStatus() != null) {
                    if (responseModel.getRegistrationStatus().equals(getString(R.string.registered))) {
                        Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    } else if (responseModel.getRegistrationStatus().equals(getString(R.string.pending))) {
                        Intent intent = new Intent(PreLoginActivity.this, FinishYourRegistrationActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    } else if (responseModel.getRegistrationStatus().equals(getString(R.string.inexistent))) {
                        Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    }
                } else {
                    if (responseModel.getKey().equals(getString(R.string.invalid_email_key))) {
                        preLoginViewHolder.errorEditTextEmail.setErrorVisibility(true);
                    } else {
                        Toast.makeText(PreLoginActivity.this, responseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(PreLoginActivity.this, getString(R.string.service_or_connection_validate_email), Toast.LENGTH_LONG).show();
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