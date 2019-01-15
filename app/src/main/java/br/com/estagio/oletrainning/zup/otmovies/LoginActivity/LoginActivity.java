package br.com.estagio.oletrainning.zup.otmovies.LoginActivity;

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

        import br.com.estagio.oletrainning.zup.otmovies.ConfirmInformationActivity.ConfirmInformationActivity;
        import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
        import br.com.estagio.oletrainning.zup.otmovies.R;
        import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
        import br.com.estagio.oletrainning.zup.otmovies.Services.PasswordValidate;
        import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
        import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
        import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewHolder loginViewHolder;

    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;

    private final String PASSWORD_VALIDATION_STATUS = "passwordContainsError";

    private boolean passwordContainsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.loginViewHolder = new LoginViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        loginViewHolder.textViewEmailEntered.setText(emailAdd);

        hideToast();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PASSWORD_VALIDATION_STATUS, passwordContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        passwordContainsError = savedInstanceState.getBoolean(PASSWORD_VALIDATION_STATUS);
        loginViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
        loginViewHolder.errorEditTextPassword.setMessageError("");
        if(passwordContainsError){
            showToast();
        } else {
            hideToast();
        }
    }

    private void setupListeners() {
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.imageViewBackArrow.setOnClickListener(backArrowOnClickListener);
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
        loginViewHolder.textViewForgetPassword.setOnClickListener(textViewForgetPasswordOnClickListener);
    }

    View.OnClickListener buttonSignInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginViewHolder.progressBar.setVisibility(View.VISIBLE);
            passwordContainsError = !validatePassword();
            loginViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
            loginViewHolder.errorEditTextPassword.setMessageError("");
            if (passwordContainsError) {
                showToast();
            } else {
                hideToast();
            }
            if (validatePassword()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(LoginActivity.this, loginViewHolder.progressBar).execute();
                PasswordValidate passwordValidate = ServiceBuilder.buildService(PasswordValidate.class);
                UserDates userDates = new UserDates();
                userDates.setEmail(getIntent().getStringExtra(getString(R.string.EmailPreLogin)));
                userDates.setPassword(loginViewHolder.errorEditTextPassword.getText().toString().trim());

                Call<Void> createNewUser = passwordValidate.userRegister(userDates,"593c3280aedd01364c73000d3ac06d76");

                createNewUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        
                        controlResponse(response);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        hideToast();
                        if(t instanceof IOException){
                            Toast.makeText(LoginActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,"Falha ao validar a senha", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                loginViewHolder.progressBar.setVisibility(View.INVISIBLE);
                loginViewHolder.textViewRedToast.setText(getString(R.string.errorUserOrPassword));
                loginViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
                loginViewHolder.errorEditTextPassword.setMessageError("");
                passwordContainsError = true;
                showToast();
            }
        }
    };

    private void controlResponse(Response response){
        if(response.code() == 200){
            hideToast();
            Toast.makeText(LoginActivity.this,"Senha confirmada, login autorizado!", Toast.LENGTH_LONG).show();
        } else {
            loginViewHolder.progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                if (errorMessage.getKey().equals("error.invalid.password")) {
                    loginViewHolder.textViewRedToast.setText(errorMessage.getMessage());
                    showToast();
                } else if (errorMessage.getKey().equals("error.unauthorized.login")) {
                    loginViewHolder.textViewRedToast.setText(errorMessage.getMessage());
                    showToast();
                } else if (errorMessage.getKey().equals("error.unauthorized.password")) {
                    loginViewHolder.textViewRedToast.setText(errorMessage.getMessage());
                    showToast();
                } else {
                        Toast.makeText(LoginActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                hideToast();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };


    private void showToast() {
        loginViewHolder.linearLayout.setVisibility(View.VISIBLE);

    }

    private void hideToast() {
        loginViewHolder.linearLayout.setVisibility(View.GONE);
    }

    View.OnClickListener textViewForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, ConfirmInformationActivity.class);
            String emailInput = loginViewHolder.textViewEmailEntered.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }

    private boolean validatePassword() {
        String password = loginViewHolder.errorEditTextPassword.getText().toString().trim();
        return (!password.isEmpty() && validatePasswordFormat());
    }

    private boolean validatePasswordFormat() {
        String userName = loginViewHolder.errorEditTextPassword.getText().toString().trim();
        return userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passwordContainsError = false;
            loginViewHolder.errorEditTextPassword.setErrorVisibility(false);
            hideToast();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
