package br.com.estagio.oletrainning.zup.otmovies.RegisterNewUserActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.PostRegisterNewUserService.CallbackNewUser;
import br.com.estagio.oletrainning.zup.otmovies.Services.RegisterNewUserService;
import br.com.estagio.oletrainning.zup.otmovies.Services.ServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;

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
        registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
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
            nameContainsError = !validateName();
            userNameContainsError = !validateUserName();
            passwordContainsError = !validatePassword();
            registerNewUserViewHolder.errorEditTextName.setErrorVisibility(nameContainsError);
            registerNewUserViewHolder.errorEditTextUserName.setErrorVisibility(userNameContainsError);
            registerNewUserViewHolder.errorEditTextPassword.setErrorVisibility(passwordContainsError);
            if (validateName() && validateUserName() && validatePassword()) {
                RegisterNewUserService registerNewUserService = ServiceBuilder.buildService(RegisterNewUserService.class);
                UserDates userDates = new UserDates();
                userDates.setEmail(getIntent().getStringExtra(getString(R.string.EmailPreLogin)));
                userDates.setPassword(registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim());
                userDates.setCompleteName(registerNewUserViewHolder.errorEditTextName.getText().toString().trim());
                userDates.setUsername(registerNewUserViewHolder.errorEditTextUserName.getText().toString().trim());

                CallbackNewUser postNewUserService =
                        new CallbackNewUser(RegisterNewUserActivity.this);



                Call<Void> callNewUser = registerNewUserService.userRegister(userDates,"593c3280aedd01364c73000d3ac06d76");

                callNewUser.enqueue(postNewUserService);

                Toast.makeText(RegisterNewUserActivity.this,postNewUserService.getTextErrorMessage()[1],Toast.LENGTH_SHORT).show();

                /*createNewUser.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.raw().code() == 200){
                                Intent intent = new Intent(RegisterNewUserActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<ErrorMessage>() {}.getType();
                                    ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(),type);
                                    Toast.makeText(RegisterNewUserActivity.this,errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(RegisterNewUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(RegisterNewUserActivity.this,"Falha ao criar usuário",Toast.LENGTH_SHORT).show();
                    }
                });
*/

            }
        }
    };


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
        String userName = registerNewUserViewHolder.errorEditTextPassword.getText().toString().trim();
        return userName.length() >= MINSIZEPASS && userName.length() <= MAXSIZEPASS && userName.matches(getString(R.string.RegexOnlyNumberAndLetter));
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
}
