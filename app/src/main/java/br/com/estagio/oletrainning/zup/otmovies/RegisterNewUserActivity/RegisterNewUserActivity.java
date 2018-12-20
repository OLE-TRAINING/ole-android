package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginViewHolder;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;

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

        String emailed = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        registerNewUserViewHolder.textViewEmailEntered.setText(emailed);
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
        registerNewUserViewHolder.customComponentErrorEditTextName.setErrorVisibility(nameContainsError);
        registerNewUserViewHolder.customComponentErrorEditTextUserName.setErrorVisibility(userNameContainsError);
        registerNewUserViewHolder.customComponentErrorEditTextPassword.setErrorVisibility(passwordContainsError);
    }

    private void setupListeners() {
        registerNewUserViewHolder.customComponentErrorEditTextName.getEditText().addTextChangedListener(editTextNameTextChangedListener);
        registerNewUserViewHolder.customComponentErrorEditTextUserName.getEditText().addTextChangedListener(editTextUserNameTextChangedListener);
        registerNewUserViewHolder.customComponentErrorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
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
            nameContainsError = !validateName();
            userNameContainsError = !validateUserName();
            passwordContainsError = !validatePassword();
            registerNewUserViewHolder.customComponentErrorEditTextName.setErrorVisibility(nameContainsError);
            registerNewUserViewHolder.customComponentErrorEditTextUserName.setErrorVisibility(userNameContainsError);
            registerNewUserViewHolder.customComponentErrorEditTextPassword.setErrorVisibility(passwordContainsError);
            if (validateName() && validateUserName() && validatePassword()) {
                Intent intent = new Intent(RegisterNewUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private boolean validateName() {
        String name = registerNewUserViewHolder.customComponentErrorEditTextName.getText().toString().trim();
        return (!name.isEmpty() && validateNameFormat());
    }

    private boolean validateUserName() {
        String userName = registerNewUserViewHolder.customComponentErrorEditTextUserName.getText().toString().trim();
        return (!userName.isEmpty() && validateUserNameFormat());
    }

    private boolean validatePassword() {
        String password = registerNewUserViewHolder.customComponentErrorEditTextPassword.getText().toString().trim();
        return (!password.isEmpty() && validatePasswordFormat());
    }

    private boolean validateNameFormat() {
        String name = registerNewUserViewHolder.customComponentErrorEditTextName.getText().toString().trim();
        return name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode));
    }

    private boolean validateUserNameFormat() {
        String userName = registerNewUserViewHolder.customComponentErrorEditTextUserName.getText().toString().trim();
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.RegexOnlyNumberOrLetter));
    }

    private boolean validatePasswordFormat() {
        String userName = registerNewUserViewHolder.customComponentErrorEditTextPassword.getText().toString().trim();
        return userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private TextWatcher editTextNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            nameContainsError = false;
            registerNewUserViewHolder.customComponentErrorEditTextName.setErrorVisibility(false);
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
            registerNewUserViewHolder.customComponentErrorEditTextUserName.setErrorVisibility(false);
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
            registerNewUserViewHolder.customComponentErrorEditTextPassword.setErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
