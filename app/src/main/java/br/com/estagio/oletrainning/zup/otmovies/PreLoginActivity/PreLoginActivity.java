package br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity.RegisterNewUserActivity;
import br.com.estagio.oletrainning.zup.otmovies.Services.RegisterNewUserService;
import br.com.estagio.oletrainning.zup.otmovies.Services.RetrievingUserDatesService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
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
                emailContainsError = !validateEmail();
                preLoginViewHolder.errorEditTextEmail.setErrorVisibility(emailContainsError);
                String emailEntered = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                if (validateEmail()) {
                    RetrievingUserDatesService retrievingUserDatesService = ServiceBuilder.buildService(RetrievingUserDatesService.class);
                    Call<UserDates> createNewUser = retrievingUserDatesService.getUsersDate(emailEntered,"593c3280aedd01364c73000d3ac06d76");

                    createNewUser.enqueue(new Callback<UserDates>() {
                        @Override
                        public void onResponse(Call<UserDates> call, Response<UserDates> response) {
                            int code = response.code();
                            if(code == 200){
                                Intent intent = new Intent(PreLoginActivity.this, RegisterNewUserActivity.class);
                                String emailInput = preLoginViewHolder.errorEditTextEmail.getText().toString().trim();
                                intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                                startActivity(intent);
                            } else {
                                Toast.makeText(PreLoginActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDates> call, Throwable t) {
                            Toast.makeText(PreLoginActivity.this,"Falha ao criar usuário",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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