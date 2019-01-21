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
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.ResendTokenToEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.RetrievingUserDatesService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import br.com.estagio.oletrainning.zup.otmovies.Services.ViewModel.UserDatesViewModel;
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

        final UserDatesViewModel viewModel = ViewModelProviders
                .of(this).get(UserDatesViewModel.class);
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
        preLoginViewHolder.errorEditTextEmail.getEditText().addTextChangedListener(editTextEmailTextChangedListener);

    }

    private View.OnClickListener buttonNextPreLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            preLoginViewHolder.progressBar.setVisibility(View.VISIBLE);
            emailContainsError = !validateEmail();
            preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
            String emailEntered = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
            if (validateEmail()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(PreLoginActivity.this, preLoginViewHolder.progressBar).execute();
                String gwKey = "593c3280aedd01364c73000d3ac06d76";
                    if (getStatus(emailEntered,gwKey).equals("REGISTERED")) {
                        Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    } else if (getStatus(emailEntered,gwKey).equals("PENDING")) {
                        Intent intent = new Intent(PreLoginActivity.this, TokenValidationActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    } else if (getStatus(emailEntered,gwKey).equals("INEXISTENT")) {
                        Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                        intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                        startActivity(intent);
                    } else {
                        preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                /*APIRequest retrievingUserDatesService = ServiceBuilder.buildService(APIRequest.class);
                Call<UserResponse> userDatesCall = retrievingUserDatesService.getUsersDate(emailEntered, "593c3280aedd01364c73000d3ac06d76");
                userDatesCall.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse userDates = response.body();
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
                            preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            try {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {}.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(),type);
                                if(errorMessage.getKey().equals("error.invalid.username")){
                                    preLoginViewHolder.errorEditTextEmail.setErrorVisibility(true);
                                } else {
                                    Toast.makeText(PreLoginActivity.this,errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(PreLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(t instanceof IOException){
                            Toast.makeText(PreLoginActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PreLoginActivity.this,"Falha ao inserir e-mail", Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
            } else {
                preLoginViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void observeViewModel(UserDatesViewModel viewModel)
    {
        viewModel.getNewsResponseObservable()
                .observe(this, new Observer<UserResponse>() {
                    @Override
                    public void onChanged(@Nullable UserResponse newsResponse) {
                        if (newsResponse != null) {
                            String response = newsResponse.getRegistrationStatus();
                        }
                    }
                });
    }

    private boolean validateEmail() {
        String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
        return (!emailInput.isEmpty() && validateEmailFormat(emailInput));
    }

    private boolean validateEmailFormat(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private TextWatcher editTextEmailTextChangedListener = new TextWatcher() {
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