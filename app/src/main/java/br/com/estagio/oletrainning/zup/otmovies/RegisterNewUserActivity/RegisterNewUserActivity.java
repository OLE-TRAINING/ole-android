package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.SyncProgressBar;
import br.com.estagio.oletrainning.zup.otmovies.TokenValidationActivity.TokenValidationActivity;


public class RegisterNewUserActivity extends AppCompatActivity {

    private RegisterNewUserViewHolder registerNewUserViewHolder;

    private RegisterNewUserViewModel registerNewUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_register_new_user, null);
        this.registerNewUserViewHolder = new RegisterNewUserViewHolder(view);
        setContentView(view);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        registerNewUserViewHolder.textViewEmailEntered.setText(emailAdd);

        registerNewUserViewModel = ViewModelProviders.of(this).get(RegisterNewUserViewModel.class);

        setupObservers();
    }

    private void colorStatusBar(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorBackground));
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar();
        setupListeners();
    }


    private void setupObservers() {
        registerNewUserViewModel.getIsLoading().observe(this, progressBarObserver);
        registerNewUserViewModel.getNameContainsErrorStatus().observe(this, nameContainsErrorObserver);
        registerNewUserViewModel.getUserNameContainsErrorStatus().observe(this, usernameContainsErrorObserver);
        registerNewUserViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);

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

            String email = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
            String name = registerNewUserViewHolder.errorEditTextName.getText().toString().trim();
            String username = registerNewUserViewHolder.errorEditTextUserName.getText().toString().trim();
            String password = registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim();
            registerNewUserViewModel.nameEntered(name);
            registerNewUserViewModel.userNameEntered(username);
            registerNewUserViewModel.passwordEntered(password);
            if (registerNewUserViewModel.isValidName(name)
                    && registerNewUserViewModel.isValidUserName(username)
                    && registerNewUserViewModel.isValidPassword(password)) {
                registerNewUserViewModel.serviceStarting();
                registerNewUserViewModel.postUserRegister(email, name, username, password)
                        .observe(RegisterNewUserActivity.this, serviceCallObserver);
            }

        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isLoading) {
            if (isLoading != null) {
                if (isLoading) {
                    registerNewUserViewHolder.progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    new SyncProgressBar(RegisterNewUserActivity.this, registerNewUserViewHolder.progressBar).execute();
                } else {
                    registerNewUserViewHolder.progressBar.setProgress(100);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    registerNewUserViewHolder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private Observer<Boolean> nameContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                registerNewUserViewHolder.errorEditTextName.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> usernameContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    Observer<ResponseModel> serviceCallObserver = new Observer<ResponseModel>() {
        @Override
        public void onChanged(@Nullable ResponseModel responseModel) {
            registerNewUserViewModel.serviceEnding();
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    Toast.makeText(RegisterNewUserActivity.this, getString(R.string.registerOk), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterNewUserActivity.this, TokenValidationActivity.class);
                    String emailInput = registerNewUserViewHolder.textViewEmailEntered.getText().toString().trim();
                    intent.putExtra(getString(R.string.EmailPreLogin), emailInput);
                    startActivity(intent);
                } else {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getKey());
                    errorMessage.setMessage(responseModel.getMessage());
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
                }
            } else {
                Toast.makeText(RegisterNewUserActivity.this, getString(R.string.service_or_connection_error_register), Toast.LENGTH_LONG).show();
            }
        }

    };

    private TextWatcher editTextNameTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            registerNewUserViewModel.nameTextChanged();
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
            registerNewUserViewModel.userNameTextChanged();
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
            registerNewUserViewModel.passwordTextChanged();
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