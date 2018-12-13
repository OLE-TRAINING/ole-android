package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;


import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;

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

        setNameErrorVisibility(false);
        setUserNameErrorVisibility(false);
        setPasswordErrorVisibility(false);
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
        setNameErrorVisibility(!nameValidationStatus);
        usernameValidationStatus = savedInstanceState.getBoolean(USER_NAME_VALIDATION_STATUS);
        setUserNameErrorVisibility(!usernameValidationStatus);
        passwordValidationStatus = savedInstanceState.getBoolean(PASSWORD_VALIDATION_STATUS);
        setPasswordErrorVisibility(!passwordValidationStatus);
    }

    private void setNameErrorVisibility(Boolean visible) {
        if (visible) {
            registerNewUserViewHolder.editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.textViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.imageViewEnterNameRegisterError.setVisibility(View.VISIBLE);
            setNameValidationStatus(false);
        } else {
            registerNewUserViewHolder.editTextEnterNameRegister.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder.textViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.imageViewEnterNameRegisterError.setVisibility(View.INVISIBLE);
            setNameValidationStatus(true);
        }
    }

    private void setUserNameErrorVisibility(Boolean visible) {
        if (visible) {
            registerNewUserViewHolder.editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.textViewEnterUserNameError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.imageViewEnterUserNameError.setVisibility(View.VISIBLE);
            setUsernameValidationStatus(false);
        } else {
            registerNewUserViewHolder.editTextEnterUserName.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder.textViewEnterUserNameError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.imageViewEnterUserNameError.setVisibility(View.INVISIBLE);
            setUsernameValidationStatus(true);
        }
    }

    private void setPasswordErrorVisibility(Boolean visible) {
        if (visible) {
            registerNewUserViewHolder.editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input_error));
            registerNewUserViewHolder.textViewEnterPasswordError.setVisibility(View.VISIBLE);
            registerNewUserViewHolder.imageViewEnterPasswordError.setVisibility(View.VISIBLE);
            setPasswordValidationStatus(false);
        } else {
            registerNewUserViewHolder.editTextEnterPassword.setBackground(getDrawable(R.drawable.border_email_input));
            registerNewUserViewHolder.textViewEnterPasswordError.setVisibility(View.INVISIBLE);
            registerNewUserViewHolder.imageViewEnterPasswordError.setVisibility(View.INVISIBLE);
            setPasswordValidationStatus(true);
        }
    }

    private void setupListeners() {
        registerNewUserViewHolder.editTextEnterNameRegister.addTextChangedListener(editTextEnterNameTextChangedListener);
        registerNewUserViewHolder.editTextEnterUserName.addTextChangedListener(editTextEnterUserNameTextChangedListener);
        registerNewUserViewHolder.editTextEnterPassword.addTextChangedListener(editTextEnterPasswordTextChangedListener);
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
            setNameErrorVisibility(!nameValidationStatus);
            usernameValidationStatus = validateUserName();
            setUserNameErrorVisibility(!usernameValidationStatus);
            passwordValidationStatus = validatePassword();
            setPasswordErrorVisibility(!passwordValidationStatus);
            if (nameValidationStatus && usernameValidationStatus && passwordValidationStatus) {

            }
        }
    };

    private TextWatcher editTextEnterNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setNameErrorVisibility(false);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validateName() {
        String name = registerNewUserViewHolder.editTextEnterNameRegister.getText().toString().trim();
        return (!name.isEmpty() && validateNameFormat());

    }

    private boolean validateNameFormat() {
        String name = registerNewUserViewHolder.editTextEnterNameRegister.getText().toString().trim();
        return name.length() <= MAXSIZENAME && name.matches(getString(R.string.RegexForNameUnicode));
    }

    private boolean validateUserNameFormat() {
        String userName = registerNewUserViewHolder.editTextEnterUserName.getText().toString().trim();
        return userName.length() <= MAXSIZEUSERNAME && userName.matches(getString(R.string.RegexOnlyNumberOrLetter));
    }

    private boolean validateUserName() {
        String userName = registerNewUserViewHolder.editTextEnterUserName.getText().toString().trim();
        return (!userName.isEmpty() && validateUserNameFormat());

    }

    private TextWatcher editTextEnterUserNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setUserNameErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean validatePasswordFormat() {
        String userName = registerNewUserViewHolder.editTextEnterPassword.getText().toString().trim();
        return userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.RegexOnlyNumberAndLetter));
    }

    private boolean validatePassword() {
        String password = registerNewUserViewHolder.editTextEnterPassword.getText().toString().trim();
        return (!password.isEmpty() && validatePasswordFormat());
    }

    private TextWatcher editTextEnterPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setPasswordErrorVisibility(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}