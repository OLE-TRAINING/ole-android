package br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

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
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.RegisterNewUserService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ResendTokenToEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.SendTokenToValidade;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenValidationActivity extends AppCompatActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;

    private final String TOKEN_VALIDATION_STATUS = "tokenContainsError";

    private final int MAXSIZETOKEN = 6;

    private boolean tokenContainsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        tokenValidationViewHolder.textViewEmail.setText(emailAdd);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TOKEN_VALIDATION_STATUS, tokenContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tokenContainsError = savedInstanceState.getBoolean(TOKEN_VALIDATION_STATUS);
        tokenValidationViewHolder.errorEditText.setErrorVisibility(tokenContainsError);
    }

    private void setupListeners() {
        tokenValidationViewHolder.button.setOnClickListener(buttonOnClickListener);
        tokenValidationViewHolder.imageView.setOnClickListener(imageViewBackArrowOnClickListener);
        tokenValidationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        tokenValidationViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tokenValidationViewHolder.progressBar.setVisibility(View.VISIBLE);
            tokenContainsError = !validateTokenSize();
            if(validateTokenSize()){
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(TokenValidationActivity.this, tokenValidationViewHolder.progressBar).execute();

                SendTokenToValidade sendTokenToValidade = ServiceBuilder.buildService(SendTokenToValidade.class);

                Call<Void> createNewUser = sendTokenToValidade.confirmToken(tokenValidationViewHolder.textViewEmail.getText().toString().trim(),
                        tokenValidationViewHolder.errorEditText.getText().toString().trim(),
                        "593c3280aedd01364c73000d3ac06d76");

                createNewUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        tokenValidationViewHolder.progressBar.setProgress(100);
                        controlResponseValidateToken(response);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        tokenValidationViewHolder.progressBar.setProgress(100);
                        if(t instanceof IOException){
                            Toast.makeText(TokenValidationActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TokenValidationActivity.this,"Falha ao validar o código", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                tokenContainsError = true;
            }
        }
    };

    private void controlResponseValidateToken(Response response){
        if(response.code() == 200){
            Toast.makeText(TokenValidationActivity.this,"Código confirmado com sucesso!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TokenValidationActivity.this, LoginActivity.class);
            intent.putExtra("emailToken",tokenValidationViewHolder.textViewEmail.getText());
            startActivity(intent);
        } else {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                if(errorMessage.getKey().equals("error.unauthorized.token")){
                    tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                    tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    tokenContainsError = true;
                } else if (errorMessage.getKey().equals("error.invalid.token")) {
                    tokenValidationViewHolder.errorEditText.setMessageError(errorMessage.getMessage());
                    tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
                    tokenContainsError = true;
                } else{
                    Toast.makeText(TokenValidationActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(TokenValidationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            tokenValidationViewHolder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new SyncProgressBar(TokenValidationActivity.this, tokenValidationViewHolder.progressBar).execute();
            tokenValidationViewHolder.progressBar.setVisibility(View.VISIBLE);

            ResendTokenToEmail resendTokenToEmail = ServiceBuilder.buildService(ResendTokenToEmail.class);

            Call<Void> createNewUser = resendTokenToEmail.resendToken(tokenValidationViewHolder.textViewEmail.getText().toString().trim(),
                    "593c3280aedd01364c73000d3ac06d76");

            createNewUser.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    tokenValidationViewHolder.progressBar.setProgress(100);
                    controlResponseResendToken(response);
                    tokenValidationViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    tokenValidationViewHolder.progressBar.setProgress(100);
                    if(t instanceof IOException){
                        Toast.makeText(TokenValidationActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TokenValidationActivity.this,"Falha ao reenviar o código", Toast.LENGTH_LONG).show();
                    }
                }
            });
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    };

    private void controlResponseResendToken(Response response){
        if(response.code() == 200){
            Toast.makeText(TokenValidationActivity.this,"Código reenviado com sucesso!", Toast.LENGTH_LONG).show();
        } else {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                Toast.makeText(TokenValidationActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(TokenValidationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            tokenValidationViewHolder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean validateTokenSize() {
        String tokenEntered = tokenValidationViewHolder.errorEditText.getText().toString().trim();
        return (tokenEntered.length() == MAXSIZETOKEN);
    }

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tokenContainsError = false;
            tokenValidationViewHolder.errorEditText.setErrorVisibility(false);
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
}
