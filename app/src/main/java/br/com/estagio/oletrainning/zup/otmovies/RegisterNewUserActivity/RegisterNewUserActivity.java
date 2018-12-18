package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity;

public class RegisterNewUserActivity extends AppCompatActivity {

    private RegisterNewUserViewHolder registerNewUserViewHolder;

    private final Integer MAXSIZENAME = 50;
    private final Integer MINSIZEPASS = 6;
    private final Integer MAXSIZEPASS = 10;
    private final Integer MAXSIZEUSERNAME = 15;

    private final String NAME_VALIDATION_STATUS = "nameValidationStatus";
    private final String USER_NAME_VALIDATION_STATUS = "userNameValidationStatus";
    private final String PASSWORD_VALIDATION_STATUS = "passwordValidationStatus";

    private boolean nameValidationStatus;
    private boolean usernameValidationStatus;
    private boolean passwordValidationStatus;

    public void setNameValidationStatus(boolean nameValidationStatus) {
        this.nameValidationStatus = nameValidationStatus;
    }

    public void setUsernameValidationStatus(boolean usernameValidationStatus) {
        this.usernameValidationStatus = usernameValidationStatus;
    }

    public void setPasswordValidationStatus(boolean passwordValidationStatus) {
        this.passwordValidationStatus = passwordValidationStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_register_new_user, null);
        this.registerNewUserViewHolder = new RegisterNewUserViewHolder(view);
        setContentView(view);

        setupListeners();

        String emailEntered = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        registerNewUserViewHolder.textViewEmailEntered.setText(emailEntered);

        setNameValidationStatus(true);
        setUsernameValidationStatus(true);
        setPasswordValidationStatus(true);
        registerNewUserViewHolder.errorEditTextEnterNameRegister.setErrorVisibility(false);
        registerNewUserViewHolder.errorEditTextEnterUserName.setErrorVisibility(false);
        registerNewUserViewHolder.errorEditTextEnterPassword.setErrorVisibility(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NAME_VALIDATION_STATUS, nameValidationStatus);
        outState.putBoolean(USER_NAME_VALIDATION_STATUS, usernameValidationStatus);
        outState.putBoolean(PASSWORD_VALIDATION_STATUS, passwordValidationStatus);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nameValidationStatus = savedInstanceState.getBoolean(NAME_VALIDATION_STATUS);
        usernameValidationStatus = savedInstanceState.getBoolean(USER_NAME_VALIDATION_STATUS);
        passwordValidationStatus = savedInstanceState.getBoolean(PASSWORD_VALIDATION_STATUS);
        registerNewUserViewHolder.errorEditTextEnterNameRegister.setErrorVisibility(!nameValidationStatus);
        registerNewUserViewHolder.errorEditTextEnterUserName.setErrorVisibility(!usernameValidationStatus);
        registerNewUserViewHolder.errorEditTextEnterPassword.setErrorVisibility(!passwordValidationStatus);
    }



    private void setupListeners() {
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
            nameValidationStatus = validateName();
            usernameValidationStatus = validateUserName();
            passwordValidationStatus = validatePassword();
            registerNewUserViewHolder.errorEditTextEnterNameRegister.setErrorVisibility(!nameValidationStatus);
            registerNewUserViewHolder.errorEditTextEnterUserName.setErrorVisibility(!usernameValidationStatus);
            registerNewUserViewHolder.errorEditTextEnterPassword.setErrorVisibility(!passwordValidationStatus);
            if (nameValidationStatus && usernameValidationStatus && passwordValidationStatus) {
                Intent intent = new Intent(RegisterNewUserActivity.this, TokenValidationActivity.class);
                startActivity(intent);
            }
        }
    };


    private boolean validateName() {
        String name = registerNewUserViewHolder.errorEditTextEnterNameRegister.getText().toString().trim();
        return (!name.isEmpty() && validateNameFormat());

    }

    private boolean validateNameFormat() {
        String name = registerNewUserViewHolder.errorEditTextEnterNameRegister.getText().toString().trim();
        return name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode));
    }

    private boolean validateUserNameFormat() {
        String userName = registerNewUserViewHolder.errorEditTextEnterUserName.getText().toString().trim();
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.RegexOnlyNumberOrLetter));
    }

    private boolean validateUserName() {
        String userName = registerNewUserViewHolder.errorEditTextEnterUserName.getText().toString().trim();
        return (!userName.isEmpty() && validateUserNameFormat());

    }

    private boolean validatePasswordFormat() {
        String userName = registerNewUserViewHolder.errorEditTextEnterPassword.getText().toString().trim();
        return userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private boolean validatePassword() {
        String password = registerNewUserViewHolder.errorEditTextEnterPassword.getText().toString().trim();
        return (!password.isEmpty() && validatePasswordFormat());
    }
}
