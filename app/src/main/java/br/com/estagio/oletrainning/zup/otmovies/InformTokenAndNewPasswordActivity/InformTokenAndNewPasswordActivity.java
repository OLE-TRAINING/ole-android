package br.com.estagio.oletrainning.zup.otmovies.InformTokenAndNewPasswordActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.ResendTokenToEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.SendTokenToValidade;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import br.com.estagio.oletrainning.zup.otmovies.Services.ValidateTokenAndChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformTokenAndNewPasswordActivity extends AppCompatActivity {

    private InformTokenAndNewPasswordViewHolder informTokenAndNewPasswordViewHolder;

    private final String TOKEN_VALIDATION_STATUS = "tokenContainsError";
    private final String PASSWORDS_VALIDATION_STATUS = "passwordsContainsError";
    private final String CONFIRM_PASSWORDS_VALIDATION_STATUS = "confirmPasswordConstainsError";

    private final int MAXSIZETOKEN = 6;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;

    private boolean tokenContainsError;
    private boolean passwordsContainsError;
    private boolean confirmPasswordConstainsError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_token_and_new_password);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.informTokenAndNewPasswordViewHolder = new InformTokenAndNewPasswordViewHolder(view);
        setContentView(view);

        setupListeners();

        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        informTokenAndNewPasswordViewHolder.textViewEmail.setText(emailAdd);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOKEN_VALIDATION_STATUS, tokenContainsError);
        outState.putBoolean(PASSWORDS_VALIDATION_STATUS, passwordsContainsError);
        outState.putBoolean(CONFIRM_PASSWORDS_VALIDATION_STATUS, confirmPasswordConstainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tokenContainsError = savedInstanceState.getBoolean(TOKEN_VALIDATION_STATUS);
        informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(tokenContainsError);
        passwordsContainsError = savedInstanceState.getBoolean(PASSWORDS_VALIDATION_STATUS);
        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(passwordsContainsError);
        confirmPasswordConstainsError = savedInstanceState.getBoolean(CONFIRM_PASSWORDS_VALIDATION_STATUS);
        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(confirmPasswordConstainsError);
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

    private TextWatcher errorEditTextTextWatcherToken = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tokenContainsError = false;
            informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(false);
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
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(false);
            passwordsContainsError = false;
            if(validatePassword()){
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(false);
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.VISIBLE);
            } else {
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);
            }
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
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(false);
            passwordsContainsError = false;
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

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callTokenResend();
        }
    };

    private void callTokenResend(){
        informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new SyncProgressBar(InformTokenAndNewPasswordActivity.this, informTokenAndNewPasswordViewHolder.progressBar).execute();

        ResendTokenToEmail resendTokenToEmail = ServiceBuilder.buildService(ResendTokenToEmail.class);

        Call<Void> createNewUser = resendTokenToEmail.resendToken(informTokenAndNewPasswordViewHolder.textViewEmail.getText().toString().trim(),
                "593c3280aedd01364c73000d3ac06d76");

        createNewUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                controlResponseResendToken(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(t instanceof IOException){
                    Toast.makeText(InformTokenAndNewPasswordActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InformTokenAndNewPasswordActivity.this,"Falha ao reenviar o código", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void controlResponseResendToken(Response response){
        if(response.code() == 200){
            Toast.makeText(InformTokenAndNewPasswordActivity.this,"Código reenviado com sucesso!", Toast.LENGTH_LONG).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                Toast.makeText(InformTokenAndNewPasswordActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(InformTokenAndNewPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateTokenSize() {
        String tokenEntered = informTokenAndNewPasswordViewHolder.errorEditTextToken.getText().toString().trim();
        return (tokenEntered.length() == MAXSIZETOKEN);
    }

    private boolean validateMatchNewPassword() {
        String newPassword  = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
        String confirmNewPassword = informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim();
        return (newPassword.equals(confirmNewPassword));
    }

    private boolean validateNewPasswordFormat() {
        String password = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
        return password.length() >= MINSIZEPASS && password.length() <= MAXSIZEPASS && password.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private boolean validatePassword() {
        String newPassword  = informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
        return (!newPassword.isEmpty() && validateNewPasswordFormat());
    }

    private boolean validateConfirmPassword() {
        String confirmNewPassword = informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim();
        return (!confirmNewPassword.isEmpty() && validateMatchNewPassword());
    }

    private boolean isEmptyTokenInput(){
        return informTokenAndNewPasswordViewHolder.errorEditTextToken.getText().toString().trim().isEmpty();
    }

    private boolean isEmptyPasswordInput(){
        return informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim().isEmpty();
    }

    private boolean isEmptyConfirmPasswordInput(){
        return informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim().isEmpty();
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.VISIBLE);
            passwordsContainsError = !validatePassword();
            confirmPasswordConstainsError = !validateConfirmPassword();
            tokenContainsError = !validateTokenSize();
            informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(passwordsContainsError);
            informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(passwordsContainsError);
            BodyChangePassword bodyChangePassword = new BodyChangePassword();
            bodyChangePassword.setEmail(getIntent().getStringExtra(getString(R.string.EmailPreLogin)));
            bodyChangePassword.setConfirmationToken(informTokenAndNewPasswordViewHolder.errorEditTextToken.getText().toString().trim());
            bodyChangePassword.setNewPassword(informTokenAndNewPasswordViewHolder.errorEditTextPassword.getText().toString().trim());
            bodyChangePassword.setNewPasswordConfirmation(informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim());
            if(isEmptyTokenInput()){

                informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError("Não são permitidos campos nulos ou vazios");
                informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
            }
            if(isEmptyPasswordInput()){
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError("Não são permitidos campos nulos ou vazios");
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
            }
            if(isEmptyConfirmPasswordInput()){
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("Não são permitidos campos nulos ou vazios");
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
            }
            if(!validateMatchNewPassword() && !isEmptyConfirmPasswordInput() ){
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError("");
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("As senhas não batem");
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
                informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
            }
            if(!validatePassword() && !isEmptyPasswordInput()){
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(getString(R.string.errorValidadePassword));
                informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
            }
            if(validateTokenSize() && validatePassword() && validateConfirmPassword()){
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(InformTokenAndNewPasswordActivity.this, informTokenAndNewPasswordViewHolder.progressBar).execute();

                ValidateTokenAndChangePassword validateTokenAndChangePassword = ServiceBuilder.buildService(ValidateTokenAndChangePassword.class);

                Call<Void> confirmTokenAndChangePassword = validateTokenAndChangePassword.validateTokenAndChangePass(bodyChangePassword,
                        "593c3280aedd01364c73000d3ac06d76");

                confirmTokenAndChangePassword.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        controlResponseValidateTokenAndChangePassword(response);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(t instanceof IOException){
                            Toast.makeText(InformTokenAndNewPasswordActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(InformTokenAndNewPasswordActivity.this,"Falha ao alterar a senha", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void controlResponseValidateTokenAndChangePassword(Response response){
        if(response.code() == 200){
            Toast.makeText(InformTokenAndNewPasswordActivity.this,"Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InformTokenAndNewPasswordActivity.this, LoginActivity.class);
            String emailInput = informTokenAndNewPasswordViewHolder.textViewEmail.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        } else {
            informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                switch (errorMessage.getKey()) {
                    case "error.invalid.password.mismatch":
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError(errorMessage.getMessage());
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError("");
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
                        break;
                    case "error.invalid.password":
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("");
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setMessageError(errorMessage.getMessage());
                        informTokenAndNewPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
                        informTokenAndNewPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
                        break;
                    case "error.unauthorized.token":
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError(errorMessage.getMessage());
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
                        break;
                    case "error.resource.token":
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setMessageError(errorMessage.getMessage());
                        informTokenAndNewPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
                        break;
                    default:
                        Toast.makeText(InformTokenAndNewPasswordActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(InformTokenAndNewPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            informTokenAndNewPasswordViewHolder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
