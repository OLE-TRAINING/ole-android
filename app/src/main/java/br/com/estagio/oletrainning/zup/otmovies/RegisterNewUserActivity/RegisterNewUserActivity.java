package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

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

import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.RegisterNewUserService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterNewUserActivity extends AppCompatActivity {

    private RegisterNewUserViewHolder registerNewUserViewHolder;

    private final Integer MAXSIZENAME = 50;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private final Integer MAXSIZEUSERNAME = 15;

    private final String NAME_VALIDATION_STATUS = "nameContainsError";
    private final String USER_NAME_VALIDATION_STATUS = "userNameContainsError";
    private final String PASSWORD_VALIDATION_STATUS = "passwordContainsError";

    private boolean nameContainsError;
    private boolean userNameContainsError;
    private boolean passwordContainsError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_register_new_user, null);
        this.registerNewUserViewHolder = new RegisterNewUserViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        registerNewUserViewHolder.textViewEmailEntered.setText(emailAdd);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NAME_VALIDATION_STATUS, nameContainsError);
        outState.putBoolean(USER_NAME_VALIDATION_STATUS, userNameContainsError);
        outState.putBoolean(PASSWORD_VALIDATION_STATUS, passwordContainsError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameContainsError = savedInstanceState.getBoolean(NAME_VALIDATION_STATUS);
        userNameContainsError = savedInstanceState.getBoolean(USER_NAME_VALIDATION_STATUS);
        passwordContainsError = savedInstanceState.getBoolean(PASSWORD_VALIDATION_STATUS);
        registerNewUserViewHolder.errorEditTextName.setErrorVisibility(nameContainsError);
        registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(userNameContainsError);

    }

    private void setupListeners() {
        registerNewUserViewHolder.errorEditTextName.getEditText().addTextChangedListener(editTextNameTextChangedListener);
        registerNewUserViewHolder.errorEditTextUserName.getEditText().addTextChangedListener(editTextUserNameTextChangedListener);
        registerNewUserViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
        registerNewUserViewHolder.imageViewBackArrow.setOnClickListener(imageViewBackArrowOnClickListener);
        registerNewUserViewHolder.buttonNextRegister.setOnClickListener(buttonNextRegisterOnClickListener);
    }

    private View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(RegisterNewUserActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };


    private View.OnClickListener buttonNextRegisterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerNewUserViewHolder.progressBar.setVisibility(View.VISIBLE);
            nameContainsError = !validateName();
            userNameContainsError = !validateUserName();
            passwordContainsError = !validatePassword();
            registerNewUserViewHolder.errorEditTextName.setErrorVisibility(nameContainsError);
            registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(userNameContainsError);
            registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
            if (validateName() && validateUserName() && validatePassword()) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new SyncProgressBar(RegisterNewUserActivity.this, registerNewUserViewHolder.progressBar).execute();
                RegisterNewUserService registerNewUserService = ServiceBuilder.buildService(RegisterNewUserService.class);
                UserDates userDates = new UserDates();
                userDates.setEmail(getIntent().getStringExtra(getString(R.string.EmailPreLogin)));
                userDates.setPassword(registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim());
                userDates.setCompleteName(registerNewUserViewHolder.errorEditTextName.getText().toString().trim());
                userDates.setUsername(registerNewUserViewHolder.errorEditTextUserName.getText().toString().trim());

                Call<Void> createNewUser = registerNewUserService.userRegister(userDates,"593c3280aedd01364c73000d3ac06d76");

                createNewUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        controlResponse(response);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        registerNewUserViewHolder.progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(t instanceof IOException){
                            Toast.makeText(RegisterNewUserActivity.this,"Ocorreu um erro na conexão", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterNewUserActivity.this,"Falha ao cadastrar o usuário", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                registerNewUserViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }

    };

    private void controlResponse(Response response){
        if(response.code() == 200){
            Toast.makeText(RegisterNewUserActivity.this,getString(R.string.registerOk), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterNewUserActivity.this, TokenValidationActivity.class);
            String emailInput = registerNewUserViewHolder.textViewEmailEntered.getText().toString().trim();
            intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
            startActivity(intent);
        } else {
            registerNewUserViewHolder.progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ErrorMessage>() {
                }.getType();
                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                switch (errorMessage.getKey()) {
                    case "error.invalid.name":
                        registerNewUserViewHolder.errorEditTextName.setErrorVisibility(true);
                        break;
                    case "error.invalid.username":
                        registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(true);
                        break;
                    case "error.invalid.password":
                        registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(true);
                        break;
                    case "error.resource.username.duplicated":
                        registerNewUserViewHolder.errorEditTextUserName.setMessageError(getString(R.string.duplicate_username));
                        registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(true);
                        break;
                    default:
                        Toast.makeText(RegisterNewUserActivity.this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(RegisterNewUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean validateName() {
        String name = registerNewUserViewHolder.errorEditTextName.getText().toString().trim();
        return (!name.isEmpty() && validateNameFormat());
    }

    private boolean validateUserName() {
        String userName = registerNewUserViewHolder.errorEditTextUserName.getText().toString().trim();
        return (!userName.isEmpty() && validateUserNameFormat());
    }

    private boolean validatePassword() {
        String password = registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim();
        return (!password.isEmpty() && validatePasswordFormat());
    }

    private boolean validateNameFormat() {
        String name = registerNewUserViewHolder.errorEditTextName.getText().toString().trim();
        return name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode));
    }

    private boolean validateUserNameFormat() {
        String userName = registerNewUserViewHolder.errorEditTextUserName.getText().toString().trim();
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.RegexOnlyNumberOrLetter));
    }

    private boolean validatePasswordFormat() {
        String password = registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim();
        return password.length() >= MINSIZEPASS && password.length() <= MAXSIZEPASS && password.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private TextWatcher editTextNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            nameContainsError = false;
            registerNewUserViewHolder.errorEditTextName.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher editTextUserNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userNameContainsError = false;
            registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(false);
            
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passwordContainsError = false;
            registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterNewUserActivity.this, PreLoginActivity.class);
        startActivity(intent);
    }
}
