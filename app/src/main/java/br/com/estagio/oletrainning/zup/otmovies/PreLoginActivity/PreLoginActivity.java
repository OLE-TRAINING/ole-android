package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;


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

import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.RetrievingUserDatesService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PreLoginActivity extends AppCompatActivity {

    private PreLoginViewHolder preLoginViewHolder;
    private boolean emailContainsError;
    private final String EMAIL_VALIDATION_STATUS = "emailContainsError";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_pre_login, null);
        this.preLoginViewHolder = new PreLoginViewHolder(view);
        setContentView(view);



        setupListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EMAIL_VALIDATION_STATUS, emailContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        emailContainsError = savedInstanceState.getBoolean(EMAIL_VALIDATION_STATUS);
        preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
    }


    private void setupListeners() {
        preLoginViewHolder.buttonNextPreLogin.setOnClickListener(buttonNextPreLoginOnClickListener);
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);

    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new SyncProgressBar(PreLoginActivity.this, preLoginViewHolder.progressBar).execute();
            emailContainsError = !validateEmail();
            preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
            String emailEntered = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
            if (validateEmail()) {
                RetrievingUserDatesService retrievingUserDatesService = ServiceBuilder.buildService(RetrievingUserDatesService.class);
                Call<UserDates> userDatesCall = retrievingUserDatesService.getUsersDate(emailEntered, "593c3280aedd01364c73000d3ac06d76");
                userDatesCall.enqueue(new Callback<UserDates>() {
                    @Override
                    public void onResponse(Call<UserDates> call, Response<UserDates> response) {
                        preLoginViewHolder.progressBar.setProgress(100);
                        UserDates userDates = response.body();
                        if (response.isSuccessful() && userDates != null) {
                            if (userDates.getRegistrationStatus().equals("REGISTERED")) {
                                Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                                String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                                intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                                startActivity(intent);
                            } else if (userDates.getRegistrationStatus().equals("PENDING")) {
                                Intent intent = new Intent(PreLoginActivity.this, TokenValidationActivity.class);
                                String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                                intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                                startActivity(intent);
                            } else if (userDates.getRegistrationStatus().equals("INEXISTENT")) {
                                Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                                String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                                intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                                startActivity(intent);
                            }
                        } else {
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {}.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(),type);
                                Toast.makeText(PreLoginActivity.this,errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PreLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserDates> call, Throwable t) {
                        preLoginViewHolder.progressBar.setProgress(100);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(PreLoginActivity.this, "Falha ao identificar o usuário, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };



    private boolean validateEmail() {
        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
        return (!emailInput.isEmpty() && validateEmailFormat(emailInput));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            emailContainsError = false;
            preLoginViewHolder.errorEditTextEmail.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}